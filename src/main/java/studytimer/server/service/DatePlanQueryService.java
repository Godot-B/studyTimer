package studytimer.server.service;

import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Timer;
import studytimer.server.web.dto.DatePlanResponseDTO;

import java.util.List;

public interface DatePlanQueryService {

    Timer getTimerByIndex(Integer timerIdx, DatePlan datePlan);

    List<Timer> getTodayTimers();

    DatePlanResponseDTO.StatDTO getStatWithAllKeywordsDTO(DatePlan datePlan);

    DatePlanResponseDTO.StatDTO getStatWithKeywordDTO(DatePlan datePlan, Long keywordId);
}
