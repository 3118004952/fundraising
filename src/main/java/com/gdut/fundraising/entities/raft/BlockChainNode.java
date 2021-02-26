package com.gdut.fundraising.entities.raft;

import com.gdut.fundraising.task.BlockChainHeartBeatTask;
import org.springframework.stereotype.Component;

/**
 * 区块链服务器节点
 */
@Component
public class BlockChainNode extends DefaultNode{


    @Override
    protected void init() {
        //初始化节点
        initNodeInfoSet();
        //设置定时任务
        setElectionTask();
        //设置选择任务
        setHeartBeatTask();
        //开始
        start();
    }

    /**
     * 初始化机器节点集
     */
    private void initNodeInfoSet() {
    }

    /**
     * 设置自定义的心跳任务
     */
    @Override
    protected void setHeartBeatTask() {
        this.heartBeatTask=new BlockChainHeartBeatTask();
    }

    @Override
    protected void setElectionTask() {

    }
}
