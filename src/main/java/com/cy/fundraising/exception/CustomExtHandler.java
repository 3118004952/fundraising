package com.cy.fundraising.exception;

import com.cy.fundraising.util.JsonResult;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice
public class CustomExtHandler {
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    Map HttpMessageNotReadableException(Exception e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(MyExceptionEnum.REQUEST_BODY_EMPTY.getCode(), MyExceptionEnum.REQUEST_BODY_EMPTY.getMessage()).result();
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    Map MissingServletRequestParameterException(Exception e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(MyExceptionEnum.REQUEST_FIELD_ERROR.getCode(), MyExceptionEnum.REQUEST_FIELD_ERROR.getMessage()).result();
    }

    @ExceptionHandler(value = MyWebException.class)
    Map MyWebException(MyWebException e, HttpServletRequest request){
        e.printStackTrace();

        return JsonResult.error(e.myExceptionEnum.getCode(),e.myExceptionEnum.getMessage()).result();
    }


    @ExceptionHandler(value = Exception.class)
    Map Exception(MyWebException e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(MyExceptionEnum.UNDEFINED.getCode(),MyExceptionEnum.UNDEFINED.getMessage()).result();
    }

}
