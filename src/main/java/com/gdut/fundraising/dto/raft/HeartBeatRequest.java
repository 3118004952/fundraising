package com.gdut.fundraising.dto.raft;

import com.gdut.fundraising.constant.raft.MessageType;

/**
 * 心跳检测
 */
public class HeartBeatRequest extends Request {

    public HeartBeatRequest() {

    }

    public HeartBeatRequest(long term) {
        this.setTerm(term);
        this.setType(MessageType.HEART_BEAT.getValue());
    }

}
