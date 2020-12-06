package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class LoginException extends BaseException {
    public LoginException(Status status) {
        super(status);
    }

    public LoginException(Integer code, String message) {
        super(code, message);
    }
}
