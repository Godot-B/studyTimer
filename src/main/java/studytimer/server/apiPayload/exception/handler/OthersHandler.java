package studytimer.server.apiPayload.exception.handler;

import studytimer.server.apiPayload.code.BaseErrorCode;
import studytimer.server.apiPayload.exception.GeneralException;

public class OthersHandler extends GeneralException {
    public OthersHandler(BaseErrorCode code) {
        super(code);
    }
}
