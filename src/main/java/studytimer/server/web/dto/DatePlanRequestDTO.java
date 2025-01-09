package studytimer.server.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class DatePlanRequestDTO {

    @Getter
    public static class SetGoalDTO {

        Integer goalHour;

        Integer goalMinute;       // 목표 공부 시간 (분 단위)
    }
}
