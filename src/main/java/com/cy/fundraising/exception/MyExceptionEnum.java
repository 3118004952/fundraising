package com.cy.fundraising.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyExceptionEnum  {


        TOKEN_NOT_FOUND(101, "Token not found."),
        CONTENT_TYPE_FALSE(111, "Content type failed."),

        LOGIN_MESSAGE_FALSE(200, "Phone or password false."),
        PHONE_EXIST(201, "Phone is exist."),
        MISS_HEADER(202, "Miss header or auth type false."),
        REGISTER_FALSE(203,"Incomplete registration information."),
        REQUEST_FIELD_ERROR(300, "Request field error."),
        REQUEST_BODY_EMPTY(301, "The request body is empty."),
        UPLOAD_PHOTO_FALSE(313,"Upload photo false,"),
        UNDEFINED(666,"Server error.")
        ;

        /**
         * 返回码
         */
        private int code;
        /**
         * 返回消息
         */
        private String message;
}
