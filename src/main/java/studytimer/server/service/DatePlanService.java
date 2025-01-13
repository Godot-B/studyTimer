package studytimer.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studytimer.server.apiPayload.code.status.ErrorStatus;
import studytimer.server.apiPayload.exception.handler.KeywordHandler;
import studytimer.server.apiPayload.exception.handler.OthersHandler;
import studytimer.server.apiPayload.exception.handler.TimerHandler;
import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Keyword;
import studytimer.server.domain.Timer;
import studytimer.server.repository.DatePlanRepository;
import studytimer.server.repository.KeywordRepository;
import studytimer.server.repository.TimerRepository;
import studytimer.server.web.dto.DatePlanRequestDTO;
import studytimer.server.web.dto.DatePlanResponseDTO;
import studytimer.server.web.dto.TimerRequestDTO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.floor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DatePlanService implements DatePlanQueryService, DatePlanCommandService {

    private final DatePlanRepository datePlanRepository;
    private final KeywordRepository keywordRepository;
    private final TimerRepository timerRepository;

    public DatePlanResponseDTO.TodayStatusDTO getTodayStatusDTO() {

        LocalDate todayDate = LocalDate.now();
        DatePlan datePlan = datePlanRepository.findByDate(todayDate);

        DatePlanResponseDTO.TodayStatusDTO todayStatusDTO = DatePlanResponseDTO.TodayStatusDTO.builder()
                .date(todayDate)
                .build();

        if (datePlan == null) {
            todayStatusDTO.setIsPresent(false);
        } else {
            todayStatusDTO.setIsPresent(true);
            todayStatusDTO.setGoalTime(datePlan.getGoalTime());
        }

        return todayStatusDTO;
    }

    /**
     * 시간 별 공부 분포 기록
     * @return start와 end의 차이 (float 분 단위)
     */
    private float calcSameDayTimeDifferAndLog(DatePlan datePlan, LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        float totalMinutes = duration.toSeconds() / 60.0f;

        int startHour = start.getHour();
        int endHour = end.getHour();
        if (startHour != 0 && endHour == 0) {
            endHour = 24; // 00:00:00 -> 24:00:00
        }

        Map<Integer, Integer> hourlyStudyTimes = datePlan.getHourlyStudyTimes();

        if (startHour == endHour) {
            hourlyStudyTimes.merge(endHour, end.getMinute() - start.getMinute(), Integer::sum);
        } else {
            // 시작 시간
            hourlyStudyTimes.merge(startHour, 60 - start.getMinute(), Integer::sum);
            // 중간 시간
            for (int iterHour = startHour + 1; iterHour < endHour; iterHour++) {
                hourlyStudyTimes.merge(iterHour, 60, Integer::sum);
            }
            // 종료 시간
            hourlyStudyTimes.merge(endHour, end.getMinute(), Integer::sum);
        }

        return totalMinutes;
    }

    private Timer calcSameDayStudyTime(Integer timerIdx, LocalDateTime startTime, LocalDateTime endTime) {

        DatePlan startDatePlan = datePlanRepository.findByDateAndThrow(startTime.toLocalDate());

        float totalMinutes = calcSameDayTimeDifferAndLog(startDatePlan, startTime, endTime);

        Timer timer = getTimerByIndex(timerIdx, startDatePlan);
        timer.updateTimerStudyTime(totalMinutes);
        return timer;
    }

    private Timer calcCrossMidnightStudyTime(Integer timerIdx, LocalDateTime startTime, LocalDateTime endTime) {
        // startTime의 다음 날 자정
        LocalDateTime midnight = startTime.toLocalDate().atStartOfDay()
                .plusDays(1);

        /*
          자정 이전 기록
         */
        DatePlan startDatePlan = datePlanRepository.findByDateAndThrow(startTime.toLocalDate());
        Timer timer = getTimerByIndex(timerIdx, startDatePlan);

        // 어제의 subject <- [startTime ~ 자정] 공부 시간 기록
        float startToMidnight = calcSameDayTimeDifferAndLog(startDatePlan, startTime, midnight);
        timer.updateTimerStudyTime(startToMidnight);

        /*
          자정 이후 기록
          : 오늘의 새로운 datePlan에서의 같은 인덱스 과목
         */
        DatePlan todayNewPlan = datePlanRepository.findByDateAndThrow(endTime.toLocalDate());
        Timer nextDayTimer = getTimerByIndex(timerIdx, todayNewPlan);

        // 오늘의 timer <- [자정 ~ endTime] 시간 기록
        float midnightToEndTime = calcSameDayTimeDifferAndLog(todayNewPlan, midnight, endTime);
        nextDayTimer.updateTimerStudyTime(midnightToEndTime);

        nextDayTimer.addStartToMidnight(startToMidnight); // 자정 이전 공부가 현재 타이머 필드에 반영돼야 함.

        return nextDayTimer;
    }

    // ** 타이머가 24시간 동작 ? 고려 X - 이스터에그 표시 **
    private void checkGongSin(LocalDateTime start, LocalDateTime end) {

        long hoursDiffer = Duration.between(start, end).toHours();
        if (hoursDiffer >= 24) {
            throw new OthersHandler(ErrorStatus.GONGSIN_APPEARED); // 이스터에그를 위한 예외
        }
    }

    // 잔디 색깔 결정
    private int checkColorFlag(float totalStudyTime) {
        int colorFlag = (int) floor(totalStudyTime / 90f); // 1시간 반마다 잔디 색이 진해진다.
        if (colorFlag >= 4)
            colorFlag = 4;

        return colorFlag;
    }

    private DatePlanResponseDTO.StatDTO getStatDTO(DatePlan datePlan, List<String> allKeywordNames, List<DatePlanResponseDTO.DateInfoDTO> allDateInfoDTOList) {
        List<DatePlanResponseDTO.HourlyStudyTimesDTO> hourlyStudyTimesDTOList = datePlan.getHourlyStudyTimes().entrySet().stream()
                .map(entry ->
                        new DatePlanResponseDTO.HourlyStudyTimesDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return DatePlanResponseDTO.StatDTO.builder()
                .allKeywords(allKeywordNames)
                .date(datePlan.getDate())
                .goalTime(datePlan.getGoalTime())
                .totalStudyTime(datePlan.getTotalStudyTime())
                .allDateInfoList(allDateInfoDTOList)
                .hourlyStudyTimesByDate(hourlyStudyTimesDTOList)
                .build();
    }

    @Transactional
    @Override
    public DatePlan createOrUpdateDatePlan(DatePlanRequestDTO.SetGoalDTO request) {

        DatePlan todayDatePlan = datePlanRepository.findByDate(LocalDate.now());
        int goalTime = request.getGoalHour() * 60 + request.getGoalMinute();

        if (todayDatePlan == null) {
            DatePlan newDatePlan = DatePlan.builder()
                    .date(LocalDate.now())
                    .goalTime(goalTime)
                    .build();
            datePlanRepository.save(newDatePlan);
            return newDatePlan;
        } else {
            todayDatePlan.updateGoalTime(goalTime);
            return todayDatePlan;
        }
    }

    @Transactional
    @Override
    public Timer completeTimer(Integer timerIdx, TimerRequestDTO.StudyLogDTO log) {
        Timer timer = calculateStudyTime(timerIdx, log);
        timer.setCompleted(true);
        return timer;
    }

    @Transactional(noRollbackFor = OthersHandler.class) // 이스터에그용 에러는 롤백 X
    @Override
    public Timer calculateStudyTime(Integer timerIdx, TimerRequestDTO.StudyLogDTO log) {

        LocalDateTime startTime = log.getStartTime();
        LocalDateTime endTime = log.getEndTime();

        Timer currentTimer;
        if (startTime.toLocalDate().equals(endTime.toLocalDate())) {
            currentTimer = calcSameDayStudyTime(timerIdx, startTime, endTime);
        } else {
            // 자정이 지나고 endTime 기록
            currentTimer = calcCrossMidnightStudyTime(timerIdx, startTime, endTime);
        }
        checkGongSin(startTime, endTime);
        return currentTimer;
    }

    @Override
    public Timer getTimerByIndex(Integer timerIdx, DatePlan datePlan) {

        if (timerIdx < 0 || timerIdx >= datePlan.getTimerList().size()) {
            throw new TimerHandler(ErrorStatus.INVALID_TIMER_INDEX);
        }

        return datePlan.getTimerList().get(timerIdx);
    }

    // 완료되지 않은 타이머가 존재한 채로 자정이 되면
    // 새로운 DatePlan 생성하고 타이머 리스트 이어받음
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void midnightCopyDatePlan() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime yesterday = now.minusDays(1);
        DatePlan yesterdayPlan = datePlanRepository.findByDate(yesterday.toLocalDate());
        if (yesterdayPlan == null) {
            return;
        }

        DatePlan newDatePlan = DatePlan.builder()
                .date(now.toLocalDate())
                .goalTime(0)
                .build();

        datePlanRepository.save(newDatePlan);

        for (Timer iterTimer : yesterdayPlan.getTimerList()) {
            Timer copyTimer = iterTimer.copy();
            copyTimer.setDatePlan(newDatePlan);
            timerRepository.save(copyTimer);
        }
    }

    @Override
    public DatePlan getTodayPlan() {
        return datePlanRepository.findByDateAndThrow(LocalDate.now());
    }

    @Override
    public DatePlan findDatePlanByDate(LocalDate date) {
        return datePlanRepository.findByDateAndThrow(date);
    }

    @Override
    public DatePlanResponseDTO.StatDTO getStatWithAllKeywordsDTO(DatePlan datePlan) {

        List<String> allKeywordNames = keywordRepository.findAll().stream().map(Keyword::getKeywordName).toList();
        List<DatePlan> allDatePlan = datePlanRepository.findAll();
        List<DatePlanResponseDTO.DateInfoDTO> allDateInfoDTOList = new ArrayList<>();

        int colorFlag;
        for (DatePlan iterDatePlan : allDatePlan) {
            colorFlag = checkColorFlag(iterDatePlan.getTotalStudyTime());
            allDateInfoDTOList.add(
                    DatePlanResponseDTO.DateInfoDTO.builder()
                            .date(iterDatePlan.getDate())
                            .colorFlag(colorFlag)
                            .build()
            );
        }

        return getStatDTO(datePlan, allKeywordNames, allDateInfoDTOList);
    }

    @Override
    public DatePlanResponseDTO.StatDTO getStatWithKeywordDTO(DatePlan datePlan, Long keywordId) {

        List<String> allKeywordNames = keywordRepository.findAll().stream().map(Keyword::getKeywordName).toList();
        List<DatePlan> allDatePlan = datePlanRepository.findAll();
        List<DatePlanResponseDTO.DateInfoDTO> allDateInfoDTOList = new ArrayList<>();

        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new KeywordHandler(ErrorStatus.KEYWORD_NOT_FOUND));

        int colorFlag;
        for (DatePlan iterDatePlan : allDatePlan) {
            double totalStudyTimesByKeyword = iterDatePlan.getTimerList().stream()
                    .filter(t -> t.getKeyword().equals(keyword))
                    .mapToDouble(Timer::getTimerStudyTime)
                    .sum();
            colorFlag = checkColorFlag((float)totalStudyTimesByKeyword);
            allDateInfoDTOList.add(
                    DatePlanResponseDTO.DateInfoDTO.builder()
                            .date(iterDatePlan.getDate())
                            .colorFlag(colorFlag)
                            .build()
            );
        }

        return getStatDTO(datePlan, allKeywordNames, allDateInfoDTOList);
    }
}
