package com.cy.fundraising.controller;


import com.cy.fundraising.exception.MyExceptionEnum;
import com.cy.fundraising.exception.MyWebException;
import com.cy.fundraising.util.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice//全局异常处理
@ResponseBody//返回json
@RestController
public class FailedController {
    @RequestMapping("/tokenFailed")
    public JsonResult requestFailed() throws MyWebException {
        throw new MyWebException(MyExceptionEnum.TOKEN_NOT_FOUND);
    }

    @RequestMapping("/contentTypeFailed")
    public JsonResult contentTypeFailed() throws MyWebException {
        throw new MyWebException(MyExceptionEnum.CONTENT_TYPE_FALSE);
    }


}
