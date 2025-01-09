package studytimer.server.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TimerResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeViewDTO {

        Float totalRemainTime;

        List<TimerPreviewDTO> timerPreviewDTOList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimerPreviewDTO {

        Long id;

        String subjectName;

        Integer goalTime;

        Float remainTime;

        Integer breakTime;
    }

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemainTimeDTO {

        Float remainTime; // 일시정지/초기화 후 남은 시간
    }
}
