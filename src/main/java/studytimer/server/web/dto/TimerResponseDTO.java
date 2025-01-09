package studytimer.server.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TimerResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimerViewDTO {

        String subjectName;

        Float remainTime; // 타이머 별 남은 시간

        Integer goalTime;

        Float totalStudyTime; // 분단위(소수점)

        Integer subjectGoalTime; // 분단위
    }
}
