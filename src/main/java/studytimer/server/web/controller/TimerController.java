package studytimer.server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import studytimer.server.apiPayload.ApiResponse;
import studytimer.server.converter.TimerConverter;
import studytimer.server.domain.Timer;
import studytimer.server.service.DatePlanCommandService;
import studytimer.server.service.KeywordService;
import studytimer.server.service.TimerService;
import studytimer.server.web.dto.TimerRequestDTO;
import studytimer.server.web.dto.TimerResponseDTO;

@RestController
@RequestMapping("/timer")
@RequiredArgsConstructor
public class TimerController {

    private final TimerService timerService;
    private final KeywordService keywordService;
    private final DatePlanCommandService datePlanCommandService;

    /**
     * 타이머 화면
     */
    @Operation(summary = "타이머 조회", description = "타이머를 조회합니다.")
    @GetMapping("/{subjectIdx}")
    public ApiResponse<TimerResponseDTO.TimerViewDTO> getTimer(@PathVariable Integer subjectIdx) {

        TimerResponseDTO.TimerViewDTO response = timerService.getTimerViewDTOByIdx(subjectIdx);
        return ApiResponse.onSuccess(response);
    }


    /**
     * 타이머 시작 & 선택적 키워드 저장
     */
    @Operation(summary = "타이머 시작 & 선택적 키워드 저장", description = "과목을 처음 공부할 때만 키워드가 저장이 됩니다.")
    @PostMapping("/{subjectIdx}/start")
    public ApiResponse<String> postKeyword(@PathVariable Integer subjectIdx) {

        return ApiResponse.onSuccess("타이머 시작 성공" +
                keywordService.optionalSaveKeyword(subjectIdx));
    }

    /**
     * 타이머 일시정지
     */
    @Operation(summary = "타이머 일시정지", description = "타이머를 일시정지하고, 공부 시간이 업데이트됩니다." +
            " 단, 타이머 동작 중 자정을 지나면 날짜 및 일부 과목이 복제됩니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    @PatchMapping("/{subjectIdx}/pause")
    public ApiResponse<TimerResponseDTO.RemainTimeDTO> pause(@PathVariable Integer subjectIdx, @RequestBody TimerRequestDTO.StudyLogDTO request) {
        Timer currentTimer = datePlanCommandService.calculateStudyTime(subjectIdx, request);
        return ApiResponse.onSuccess(TimerConverter.toRemainTimeDTO(currentTimer));
    }


    /**
     * 타이머 초기화
     */
    @Operation(summary = "타이머 초기화", description = "타이머를 초기화하고, 공부 시간이 업데이트되며 과목의 공부가 완료됩니다." +
            " 단, 타이머 동작 중 자정을 지나면 날짜 및 일부 과목이 복제됩니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    @PatchMapping("/{subjectIdx}/stop")
    public ApiResponse<TimerResponseDTO.RemainTimeDTO> stop(@PathVariable Integer subjectIdx, @RequestBody TimerRequestDTO.StudyLogDTO request) {
        Timer currentTimer = datePlanCommandService.completeTimer(subjectIdx, request);
        return ApiResponse.onSuccess(TimerConverter.toRemainTimeDTO(currentTimer));
    }


}
