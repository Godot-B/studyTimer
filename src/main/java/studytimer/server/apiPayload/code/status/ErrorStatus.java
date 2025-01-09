package studytimer.server.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import studytimer.server.apiPayload.code.BaseErrorCode;
import studytimer.server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 날짜 관련 에러
    DATEPLAN_NOT_FOUND(HttpStatus.BAD_REQUEST, "DATEPLAN4001", "날짜가 존재하지 않습니다."),

    // 키워드 관련 에러
    KEYWORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "KEYWORD4001", "키워드가 존재하지 않습니다."),

    // 타이머 관련 에러
    TIMER_NOT_FOUND(HttpStatus.BAD_REQUEST, "TIMER4001", "타이머가 존재하지 않습니다."),
    INVALID_TIMER_INDEX(HttpStatus.BAD_REQUEST, "SUBJECT4002", "타이머 리스트의 인덱스를 벗어났습니다."),
    TIMER_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "TIMER4003", "이미 완료한 타이머입니다."),

    // 기타
    GONGSIN_APPEARED(HttpStatus.BAD_REQUEST, "DATEPLAN4002", "당신을 공부의 신으로 임명합니다.(연속 공부 24시간을 넘긴 자)"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
