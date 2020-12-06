package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class ReadDetailException extends BaseException {
    public ReadDetailException(Status status) {
        super(status);
    }

    public ReadDetailException(Integer code, String message) {
        super(code, message);
    }
}
