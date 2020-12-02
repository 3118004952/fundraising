package com.cy.fundraising.exception;

public class MyWebException extends Exception{
    public MyExceptionEnum myExceptionEnum;
    public MyWebException(MyExceptionEnum myExceptionEnum){
        super(myExceptionEnum.getMessage());
        this.myExceptionEnum = myExceptionEnum;
    }
}
