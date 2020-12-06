package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class ReadListException extends BaseException {
    public ReadListException(Status status) {
        super(status);
    }

    public ReadListException(Integer code, String message) {
        super(code, message);
    }
}
