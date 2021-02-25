package com.gdut.fundraising.service;

import com.gdut.fundraising.entities.raft.HeartBeatRequest;
import com.gdut.fundraising.entities.raft.Request;
import com.gdut.fundraising.util.JsonResult;

public interface NetworkService {
    public <R extends Request> JsonResult post(String ip, String port, R data);

}
