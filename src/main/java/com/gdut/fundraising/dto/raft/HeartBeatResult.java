package com.gdut.fundraising.dto.raft;

/**
 * 心跳包的回复
 */
public class HeartBeatResult {
    /**
     * 任期
     */
    private long term;
    private boolean success;

    public long getTerm() {
        return term;
    }

    public void setTerm(long term) {
        this.term = term;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
