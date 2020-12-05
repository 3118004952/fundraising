package com.cy.fundraising.handler;

import com.cy.fundraising.exception.BaseException;

import com.cy.fundraising.util.JsonResult;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.cy.fundraising.constant.Status.UNKNOWN_ERROR;

@RestControllerAdvice
public class CustomExtHandler {
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    Map HttpMessageNotReadableException(Exception e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(400, "请求内容为空！").result();
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    Map MissingServletRequestParameterException(Exception e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(400, "请求字段错误！").result();
    }

    @ExceptionHandler(value = BaseException.class)
    Map MyWebException(BaseException e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(e.getCode(),e.getMessage()).result();
    }

    @ExceptionHandler(value = Exception.class)
    Map Exception(Exception e, HttpServletRequest request){
        e.printStackTrace();
        return JsonResult.error(UNKNOWN_ERROR.getCode(),UNKNOWN_ERROR.getMessage()).result();
    }

}
