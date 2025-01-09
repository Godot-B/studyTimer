package studytimer.server.apiPayload.exception.handler;

import studytimer.server.apiPayload.code.BaseErrorCode;
import studytimer.server.apiPayload.exception.GeneralException;

public class KeywordHandler extends GeneralException {
    public KeywordHandler(BaseErrorCode code) {
        super(code);
    }
}
