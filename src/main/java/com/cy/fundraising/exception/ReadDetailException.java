package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class ReadDetailException extends BaseException {
    public ReadDetailException(Status status) {
        super(status);
    }

    public ReadDetailException(Integer code, String message) {
        super(code, message);
    }
}
