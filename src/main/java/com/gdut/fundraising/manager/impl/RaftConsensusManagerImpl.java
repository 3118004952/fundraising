package com.gdut.fundraising.manager.impl;


import com.gdut.fundraising.constant.raft.NodeStatus;
import com.gdut.fundraising.dto.raft.VoteResult;
import com.gdut.fundraising.dto.raft.VoteRequest;
import com.gdut.fundraising.manager.RaftConsensusManager;


import com.gdut.fundraising.entities.raft.DefaultNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class RaftConsensusManagerImpl implements RaftConsensusManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftConsensusManagerImpl.class);


    public final ReentrantLock voteLock = new ReentrantLock();
    public final ReentrantLock appendLock = new ReentrantLock();

    /**
     * 请求投票 RPC
     * <p>
     * 接收者实现：
     * 如果term < currentTerm返回 false （5.2 节）
     * 如果 votedFor 为空或者就是 candidateId，并且候选人的日志至少和自己一样新，那么就投票给他（5.2 节，5.4 节）
     */

    @Override
    public VoteResult dealVoteRequest(VoteRequest param, final DefaultNode node) {
        try {
            if (!voteLock.tryLock()) {
                return VoteResult.fail(node.getCurrentTerm());
            }

            // 对方任期没有自己新
            if (param.getTerm() < node.getCurrentTerm()) {
                return VoteResult.fail(node.getCurrentTerm());
            }

            LOGGER.info("node {} current vote for [{}], param candidateId : {}", node.getNodeInfoSet().getSelf(),
                    node.getVotedFor(), param.getCandidateId());
            LOGGER.info("node {} current term {}, peer term : {}", node.getNodeInfoSet().getSelf(),
                    node.getCurrentTerm(), param.getTerm());
            // (当前节点并没有投票 或者 已经投票过了且是对方节点) && 对方日志和自己一样新
            if ((StringUtils.isEmpty(node.getVotedFor()) || node.getVotedFor().equals(param.getCandidateId()))) {

                if (node.getRaftLogManager().getLastLogEntry() != null) {
                    // 对方没有自己新
                    if (node.getRaftLogManager().getLastLogEntry().getTerm() > param.getLastLogTerm()) {
                        return VoteResult.fail(node.getCurrentTerm());
                    }
                    // 对方没有自己新
                    if (node.getRaftLogManager().getLastLogIndex() > param.getLastLogIndex()) {
                        return VoteResult.fail(node.getCurrentTerm());
                    }
                }
                //更新node状态
                updateNode(param,node);
                // 返回成功
                return VoteResult.ok(node.getCurrentTerm());
            }

            return VoteResult.fail(node.getCurrentTerm());

        } finally {
            voteLock.unlock();
        }
    }

    /**
     * 每轮选举后，更新node状态
     * <p>更新自身状态</p>
     * <p>更新新一轮的选举时间</p>
     * <p>更新leader</p>
     * <p>更新任期</p>
     * @param param
     * @param node
     */
    private void updateNode(VoteRequest param, DefaultNode node) {
        // 切换状态
        node.status = NodeStatus.FOLLOWER;
        //更新新的选举时间
        node.setPreElectionTime(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150);
        // 更新??
        // 这么快就要更新leader了吗
        node.getNodeInfoSet().setLeader(node.getNodeInfoSet().getNode(param.getCandidateId()));
        node.setCurrentTerm(param.getTerm());
        node.setVotedFor(param.getCandidateId());
    }


}
