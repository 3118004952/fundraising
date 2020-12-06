package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class SetProjectStateException extends BaseException {
    public SetProjectStateException(Status status) {
        super(status);
    }

    public SetProjectStateException(Integer code, String message) {
        super(code, message);
    }
}
