package studytimer.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studytimer.server.apiPayload.code.status.ErrorStatus;
import studytimer.server.apiPayload.exception.handler.TimerHandler;
import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Keyword;
import studytimer.server.domain.Timer;
import studytimer.server.repository.DatePlanRepository;
import studytimer.server.repository.KeywordRepository;
import studytimer.server.repository.TimerRepository;
import studytimer.server.web.dto.TimerRequestDTO;
import studytimer.server.web.dto.TimerResponseDTO;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimerService {

    private final TimerRepository timerRepository;
    private final KeywordRepository keywordRepository;
    private final DatePlanRepository datePlanRepository;

    @Transactional
    public Timer addTimer(TimerRequestDTO.AddTimerDTO request) {

        int timerGoalTime = request.getGoalHour() * 60 + request.getGoalMinute();
        DatePlan todayPlan = datePlanRepository.findByDateAndThrow(LocalDate.now());
        Keyword keyword = keywordRepository.findByKeywordName(request.getSubjectName());

        Timer newTimer = Timer.builder()
                .timerName(request.getSubjectName())
                .timerGoalTime(timerGoalTime)
                .datePlan(todayPlan)
                .keyword(keyword)
                .build();

        return timerRepository.save(newTimer);
    }

    @Transactional
    public String deleteTimer(Long timerId) {
        Timer findTimer = timerRepository.findById(timerId)
                .orElseThrow(() -> new TimerHandler(ErrorStatus.TIMER_NOT_FOUND));

        Keyword keyword = findTimer.getKeyword();

        timerRepository.delete(findTimer);

        if (keyword.getTimers().isEmpty()) {

            keywordRepository.delete(keyword);
            return "(키워드 포함)" + findTimer.getTimerName();
        } else {
            return findTimer.getTimerName();
        }
    }

    public List<Timer> getTodayTimers() {
        DatePlan todayPlan = datePlanRepository.findByDateAndThrow(LocalDate.now());
        return todayPlan.getTimerList();
    }

    public TimerResponseDTO.TimerViewDTO getTimerViewDTOByIdx(Integer timerIdx) {

        DatePlan todayPlan = datePlanRepository.findByDateAndThrow(LocalDate.now());

        Timer timer = getTodayTimers().get(timerIdx);
        Integer timerGoalTime = timer.getTimerGoalTime();
        Float timerStudyTime = timer.getTimerStudyTime();

        return TimerResponseDTO.TimerViewDTO.builder()
                .subjectName(timer.getTimerName())
                .remainTime(timerGoalTime - timerStudyTime)
                .goalTime(todayPlan.getGoalTime())
                .totalStudyTime(todayPlan.getTotalStudyTime())
                .subjectGoalTime(timerGoalTime)
                .build();
    }
}
