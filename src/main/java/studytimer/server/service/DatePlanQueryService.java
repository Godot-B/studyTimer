package studytimer.server.service;

import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Timer;
import studytimer.server.web.dto.DatePlanResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DatePlanQueryService {

    DatePlanResponseDTO.TodayStatusDTO getTodayStatusDTO();

    Timer getTimerByIndex(Integer timerIdx, DatePlan datePlan);

    DatePlan getTodayPlan();

    DatePlan findDatePlanByDate(LocalDate date);

    DatePlanResponseDTO.StatDTO getStatWithAllKeywordsDTO(DatePlan datePlan);

    DatePlanResponseDTO.StatDTO getStatWithKeywordDTO(DatePlan datePlan, Long keywordId);
}
