package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class ContributionException extends BaseException {
    public ContributionException(Status status) {
        super(status);
    }

    public ContributionException(Integer code, String message) {
        super(code, message);
    }
}
