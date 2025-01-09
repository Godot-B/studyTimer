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
import studytimer.server.web.dto.KeywordResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final DatePlanRepository datePlanRepository;
    private final DatePlanQueryService datePlanQueryService;

    @Transactional
    public Optional<KeywordResponseDTO.SetResultDTO> optionalSaveKeyword(Integer timerIdx) {

        DatePlan todayPlan = datePlanRepository.findByDateAndThrow(LocalDate.now());
        Timer timer = datePlanQueryService.getTimerByIndex(timerIdx, todayPlan);

        if (timer.getCompleted()) {
            throw new TimerHandler(ErrorStatus.TIMER_ALREADY_COMPLETED);

        } else if (timer.getStarted()) {
            return Optional.empty(); // 단지 시작. 키워드 저장 X

        } else {
            timer.setStarted(true);
            String keywordName = timer.getTimerName();

            Keyword newKeyword = Keyword.builder()
                    .keywordName(keywordName)
                    .keywordStudyTime(timer.getTimerStudyTime())
                    .build();

            keywordRepository.save(newKeyword);

            timer.setKeyword(newKeyword);

            return Optional.ofNullable(KeywordResponseDTO.SetResultDTO.builder()
                    .keywordId(newKeyword.getId())
                    .keywordName(newKeyword.getKeywordName())
                    .createdAt(newKeyword.getCreatedAt())
                    .build());
        }
    }

    public List<String> searchKeywords(String userInput) {
        return keywordRepository.findByKeywordNameContainingIgnoreCase(userInput).stream()
                .map(Keyword::getKeywordName).toList();
    }
}
