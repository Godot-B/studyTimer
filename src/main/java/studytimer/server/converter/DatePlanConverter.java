package studytimer.server.converter;

import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Timer;
import studytimer.server.web.dto.DatePlanResponseDTO;
import studytimer.server.web.dto.TimerResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class DatePlanConverter {

    public static DatePlanResponseDTO.SetResultDTO toSetResultDTO(DatePlan datePlan) {
        return DatePlanResponseDTO.SetResultDTO.builder()
                .datePlanId(datePlan.getId())
                .date(datePlan.getDate())
                .createdAt(datePlan.getCreatedAt())
                .build();
    }

    public static TimerResponseDTO.HomeViewDTO toHomeViewDTO(DatePlan todayPlan) {

        List<TimerResponseDTO.TimerPreviewDTO> timerPreviewDTOList = new ArrayList<>();
        for (Timer timer : todayPlan.getTimerList()) {
            Integer timerGoalTime = timer.getTimerGoalTime();
            Float timerStudyTime = timer.getTimerStudyTime();
            timerPreviewDTOList.add(TimerResponseDTO.TimerPreviewDTO.builder()
                    .id(timer.getId())
                    .subjectName(timer.getTimerName())
                    .goalTime(timerGoalTime)
                    .remainTime(timerGoalTime - timerStudyTime)
                    .breakTime(timer.getBreakTime())
                    .build());
        }

        return TimerResponseDTO.HomeViewDTO.builder()
                .totalRemainTime(todayPlan.getGoalTime() - todayPlan.getTotalStudyTime())
                .timerPreviewDTOList(timerPreviewDTOList)
                .build();
    }
}
