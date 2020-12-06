package com.cy.fundraising.controller;


import com.cy.fundraising.exception.BaseException;
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
    public JsonResult requestFailed() throws BaseException {
        throw new BaseException(400, "未携带token！");
    }

    @RequestMapping("/contentTypeFailed")
    public JsonResult contentTypeFailed() throws BaseException {
        throw new BaseException(400, "content-type未设置！");
    }

}
