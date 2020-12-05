package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class LaunchException extends BaseException {
    public LaunchException(Status status) {
        super(status);
    }

    public LaunchException(Integer code, String message) {
        super(code, message);
    }
}
