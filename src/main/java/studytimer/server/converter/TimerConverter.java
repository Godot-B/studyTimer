package studytimer.server.converter;

import studytimer.server.domain.Timer;
import studytimer.server.web.dto.DatePlanResponseDTO;
import studytimer.server.web.dto.TimerResponseDTO;

public class TimerConverter {

    public static TimerResponseDTO.TimerPreviewDTO toTimerPreviewDTO(Timer timer) {

        Integer timerGoalTime = timer.getTimerGoalTime();

        return TimerResponseDTO.TimerPreviewDTO.builder()
                .id(timer.getId())
                .subjectName(timer.getTimerName())
                .goalTime(timerGoalTime)
                .remainTime(timerGoalTime - timer.getTimerStudyTime())
                .breakTime(timer.getBreakTime())
                .build();
    }

    public static DatePlanResponseDTO.DeleteResultDTO toDeleteResultDTO(Long deleteTimerId, String deleteTimerName) {
        return DatePlanResponseDTO.DeleteResultDTO.builder()
                .deleteSubjectId(deleteTimerId)
                .message(deleteTimerName + " 이(가) 삭제되었습니다.")
                .build();
    }

    public static TimerResponseDTO.RemainTimeDTO toRemainTimeDTO(Timer timer) {

        return TimerResponseDTO.RemainTimeDTO.builder()
                .remainTime(timer.getTimerGoalTime() - timer.getTimerStudyTime())
                .build();
    }
}
