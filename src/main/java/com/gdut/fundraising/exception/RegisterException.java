package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class RegisterException extends BaseException {
    public RegisterException(Status status) {
        super(status);
    }

    public RegisterException(Integer code, String message) {
        super(code, message);
    }
}
