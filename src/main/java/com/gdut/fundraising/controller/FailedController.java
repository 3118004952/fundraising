package com.gdut.fundraising.controller;


import com.gdut.fundraising.exception.BaseException;
import com.gdut.fundraising.util.JsonResult;

/***
 * 返回部分错误信息
 */
public interface FailedController {

    /***
     * 转发filter处产生的token错误
     * @RequestMapping("/tokenFailed")
     * @return Map
     * @throws BaseException
     */
    JsonResult requestFailed() throws BaseException;

    /***
     * 转发filter处产生的contentTypeFailed错误
     * @RequestMapping("/contentTypeFailed")
     * @return Map
     * @throws BaseException
     */
    JsonResult contentTypeFailed() throws BaseException ;

}
