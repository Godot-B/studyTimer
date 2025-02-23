package studytimer.server.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DatePlanResponseDTO {

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayStatusDTO {

        LocalDate date;

        Boolean isPresent;

        Integer goalTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetResultDTO {

        Long datePlanId;
        LocalDate date;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResultDTO {

        Long deleteSubjectId;
        String message;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourlyStudyTimesDTO {

        Integer hour;

        Integer studyTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateInfoDTO {

        LocalDate date;

        Integer colorFlag; // 모든 과목의, 또는 과목 별
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatDTO {

        // 과목 전체
        List<String> allKeywords;

        // 날짜별 통계
        LocalDate date;

        Integer goalTime;

        Float totalStudyTime;

        List<HourlyStudyTimesDTO> hourlyStudyTimesByDate;

        // 모든 날짜 총 공부시간 JSON (날짜 : 총 공부 시간)
        List<DateInfoDTO> allDateInfoList;
    }
}
