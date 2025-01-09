package studytimer.server.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import studytimer.server.apiPayload.code.BaseErrorCode;
import studytimer.server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}

