package studytimer.server.apiPayload.exception.handler;

import studytimer.server.apiPayload.code.BaseErrorCode;
import studytimer.server.apiPayload.exception.GeneralException;

public class TimerHandler extends GeneralException {
    public TimerHandler(BaseErrorCode code) {
        super(code);
    }
}
