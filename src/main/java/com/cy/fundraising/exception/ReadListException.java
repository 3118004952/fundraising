package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class ReadListException extends BaseException {
    public ReadListException(Status status) {
        super(status);
    }

    public ReadListException(Integer code, String message) {
        super(code, message);
    }
}
