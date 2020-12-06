package com.gdut.fundraising.exception;

import com.gdut.fundraising.constant.Status;

public class UploadPhotoException extends BaseException {
    public UploadPhotoException(Status status) {
        super(status);
    }

    public UploadPhotoException(Integer code, String message) {
        super(code, message);
    }
}
