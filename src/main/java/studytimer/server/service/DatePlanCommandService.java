package studytimer.server.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Timer;
import studytimer.server.web.dto.DatePlanRequestDTO;
import studytimer.server.web.dto.TimerRequestDTO;

public interface DatePlanCommandService {

    @Transactional
    DatePlan createOrUpdateDatePlan(DatePlanRequestDTO.SetGoalDTO request);

    @Transactional
    Timer completeTimer(Integer timerIdx, TimerRequestDTO.StudyLogDTO log);

    @Transactional
    Timer calculateStudyTime(Integer timerIdx, TimerRequestDTO.StudyLogDTO log);

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    void midnightCopyDatePlan();
}
