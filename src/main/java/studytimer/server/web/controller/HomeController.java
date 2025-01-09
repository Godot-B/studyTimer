package studytimer.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import studytimer.server.apiPayload.ApiResponse;
import studytimer.server.converter.DatePlanConverter;
import studytimer.server.converter.TimerConverter;
import studytimer.server.domain.DatePlan;
import studytimer.server.domain.Timer;
import studytimer.server.service.DatePlanService;
import studytimer.server.service.KeywordService;
import studytimer.server.service.TimerService;
import studytimer.server.web.dto.DatePlanRequestDTO;
import studytimer.server.web.dto.DatePlanResponseDTO;
import studytimer.server.web.dto.TimerRequestDTO;
import studytimer.server.web.dto.TimerResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final DatePlanService datePlanService;
    private final KeywordService keywordService;
    private final TimerService timerService;

    /**
     * 홈 첫 계획 화면
     */
    @Operation(summary = "오늘의 총 목표 시간 설정", description = "홈 화면에서 총 목표 시간을 설정, 또는 수정합니다.")
    @PostMapping("/set")
    public ApiResponse<DatePlanResponseDTO.SetResultDTO> makeDatePlan(@RequestBody DatePlanRequestDTO.SetGoalDTO request) {

        DatePlan datePlan = datePlanService.createOrUpdateDatePlan(request);
        return ApiResponse.onSuccess(DatePlanConverter.toSetResultDTO(datePlan));
    }

    /**
     * 더 자세한 계획 세우기
     *  - 타이머 리스트 조회
     */
    @Operation(summary = "오늘의 과목 리스트 조회", description = "오늘의 과목 리스트를 조회합니다.")
    @GetMapping("/lists")
    public ApiResponse<TimerResponseDTO.HomeViewDTO> getTimersList() {

        DatePlan datePlan = datePlanService.getTodayPlan();
        return ApiResponse.onSuccess(DatePlanConverter.toHomeViewDTO(datePlan));
    }

    /**
     * 추천 키워드 목록 조회
     */
    @Operation(summary = "[도전 과제] 추천 키워드 목록", description = "과목 이름 입력값마다 키워드를 조회합니다.")
    @GetMapping("/lists/add")
    public ApiResponse<List<String>> searchKeywords(@RequestParam String userInput) {

        List<String> keywordNameList = keywordService.searchKeywords(userInput);
        return ApiResponse.onSuccess(keywordNameList);
    }

    /**
     * 더 자세한 계획 세우기
     *  - 타이머 추가
     */
    @Operation(summary = "과목 추가", description = "새로운 과목을 추가합니다.")
    @PostMapping("/lists/add")
    public ApiResponse<TimerResponseDTO.TimerPreviewDTO> addTimer(@RequestBody TimerRequestDTO.AddTimerDTO request) {
        Timer timer = timerService.addTimer(request);
        return ApiResponse.onSuccess(TimerConverter.toTimerPreviewDTO(timer));
    }


    /**
     * 더 자세한 계획 세우기
     *  - 타이머 삭제
     */
    @Operation(summary = "과목(타이머) 삭제", description = "지정한 과목(타이머)를 삭제합니다." +
            "단, 시작한 적이 있는 과목이라면 계획에서만 삭제합니다.")
    @PatchMapping("/lists/{subjectId}")
    public ApiResponse<DatePlanResponseDTO.DeleteResultDTO> deleteTimer(@PathVariable Long subjectId) {
        String deleteTimerName = timerService.deleteTimer(subjectId);
        return ApiResponse.onSuccess(TimerConverter.toDeleteResultDTO(subjectId, deleteTimerName));
    }
}
