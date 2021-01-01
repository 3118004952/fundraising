package com.gdut.fundraising.controller.impl;


import com.gdut.fundraising.controller.FailedController;
import com.gdut.fundraising.exception.BaseException;
import com.gdut.fundraising.util.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody//返回json
public class FailedControllerImpl implements FailedController {
    //转发filter处产生的错误
    @RequestMapping("/tokenFailed")
    public JsonResult requestFailed() throws BaseException {
        throw new BaseException(400, "未携带token！");
    }
    //转发filter处产生的错误
    @RequestMapping("/contentTypeFailed")
    public JsonResult contentTypeFailed() throws BaseException {
        throw new BaseException(400, "content-type未设置！");
    }

}
