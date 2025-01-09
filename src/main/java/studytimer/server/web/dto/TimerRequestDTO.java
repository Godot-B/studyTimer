package studytimer.server.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;


public class TimerRequestDTO {

    @Getter
    public static class AddTimerDTO {

        String subjectName; // 타이머 이름

        Integer goalHour;       // 목표 공부 시간 (시간 단위)

        Integer goalMinute;       // 목표 공부 시간 (분 단위)
    }

    @Getter
    public static class StudyLogDTO {

        LocalDateTime startTime;

        LocalDateTime endTime;
    }
}
