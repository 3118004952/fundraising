package com.gdut.fundraising.manager;

import com.gdut.fundraising.dto.raft.VoteResult;
import com.gdut.fundraising.entities.raft.VoteRequest;
import com.gdut.fundraising.service.NodeService;
import com.gdut.fundraising.service.impl.NodeServiceImpl;

/**
 * 一致性管理器
 * 为raft算法提供投票跟日志添加服务
 */
public interface RaftConsensusManager {
    VoteResult dealVoteRequest(VoteRequest param, NodeServiceImpl node);
}
