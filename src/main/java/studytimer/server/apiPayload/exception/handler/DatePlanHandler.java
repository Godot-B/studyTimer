package studytimer.server.apiPayload.exception.handler;

import studytimer.server.apiPayload.code.BaseErrorCode;
import studytimer.server.apiPayload.exception.GeneralException;

public class DatePlanHandler extends GeneralException {
    public DatePlanHandler(BaseErrorCode code) {
        super(code);
    }
}
