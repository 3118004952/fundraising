package com.cy.fundraising.exception;

import com.cy.fundraising.constant.Status;

public class UploadPhotoException extends BaseException {
    public UploadPhotoException(Status status) {
        super(status);
    }

    public UploadPhotoException(Integer code, String message) {
        super(code, message);
    }
}
