package com.gdut.fundraising.service.impl;


import com.gdut.fundraising.dto.raft.Request;
import com.gdut.fundraising.exception.BaseException;
import com.gdut.fundraising.service.NetworkService;
import com.gdut.fundraising.util.JsonResult;
import com.gdut.fundraising.util.NetworkUtils;



public class NetworkServiceImpl implements NetworkService{

    @Override
    public <R extends Request> JsonResult post(String ip, String port, R data) {
        JsonResult jsonResult= NetworkUtils.postByHttp(NetworkUtils.buildUrl(ip,port,data),data);

        if(jsonResult.getCode()!=200){
            throw new BaseException(jsonResult.getCode(),jsonResult.getMsg());
        }
        return  jsonResult;
    }
}
