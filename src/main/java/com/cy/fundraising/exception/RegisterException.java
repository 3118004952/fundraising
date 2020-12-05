package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class RegisterException extends BaseException {
    public RegisterException(Status status) {
        super(status);
    }

    public RegisterException(Integer code, String message) {
        super(code, message);
    }
}
