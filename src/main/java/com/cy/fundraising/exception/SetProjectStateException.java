package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class SetProjectStateException extends BaseException {
    public SetProjectStateException(Status status) {
        super(status);
    }

    public SetProjectStateException(Integer code, String message) {
        super(code, message);
    }
}
