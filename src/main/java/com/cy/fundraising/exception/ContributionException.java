package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class ContributionException extends BaseException {
    public ContributionException(Status status) {
        super(status);
    }

    public ContributionException(Integer code, String message) {
        super(code, message);
    }
}
