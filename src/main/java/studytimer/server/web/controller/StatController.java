package studytimer.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studytimer.server.apiPayload.ApiResponse;
import studytimer.server.domain.DatePlan;
import studytimer.server.service.DatePlanQueryService;
import studytimer.server.web.dto.DatePlanResponseDTO;

import java.time.LocalDate;

@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class StatController {


    private final DatePlanQueryService datePlanQueryService;

    @Operation(summary = "특정 날짜 & 모든 날짜의 '모든 과목' 통계", description = "총 공부 시간 <- 모든 과목")
    @GetMapping("/{date}") // 2025-01-02 방식
    public ApiResponse<DatePlanResponseDTO.StatDTO> dateInfoWithAllSubjects(@PathVariable LocalDate date) {

        DatePlan datePlan = datePlanQueryService.findDatePlanByDate(date);
        DatePlanResponseDTO.StatDTO response = datePlanQueryService.getStatWithAllKeywordsDTO(datePlan);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "특정 날짜 & 모든 날짜의 '특정 과목' 통계", description = "총 공부 시간 <- 과목(키워드)별")
    @GetMapping("/{date}/{keywordId}")
    public ApiResponse<DatePlanResponseDTO.StatDTO> dateInfoBySubject(@PathVariable LocalDate date, @PathVariable Long keywordId) {

        DatePlan datePlan = datePlanQueryService.findDatePlanByDate(date);
        DatePlanResponseDTO.StatDTO response = datePlanQueryService.getStatWithKeywordDTO(datePlan, keywordId);
        return ApiResponse.onSuccess(response);
    }
}
