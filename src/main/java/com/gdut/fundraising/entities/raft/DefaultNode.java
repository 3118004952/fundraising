package com.gdut.fundraising.entities.raft;


import com.gdut.fundraising.constant.raft.NodeStatus;
import com.gdut.fundraising.manager.RaftConsensusManager;
import com.gdut.fundraising.manager.RaftLogManager;
import com.gdut.fundraising.manager.impl.RaftConsensusManagerImpl;
import com.gdut.fundraising.manager.impl.RaftLogManagerImpl;
import com.gdut.fundraising.service.NetworkService;
import com.gdut.fundraising.service.impl.NetworkServiceImpl;
import com.gdut.fundraising.task.ElectionTask;
import com.gdut.fundraising.task.HeartBeatTask;
import com.gdut.fundraising.task.RaftThreadPool;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;



/**
 * 节点服务
 */
public abstract class DefaultNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNode.class);
    /**
     * 节点信息
     */
    private NodeInfoSet nodeInfoSet;

    /**
     * 选举时间间隔基数 15S
     */
    public volatile long electionTime = 15 * 1000;
    /**
     * 上一次选举时间
     */
    public volatile long preElectionTime = 0;

    /**
     * 上次一心跳时间戳
     */
    public volatile long preHeartBeatTime = 0;
    /**
     * 心跳间隔基数 5S
     */
    public final long heartBeatTick = 5 * 1000;

    /**
     * 节点当前状态
     *
     * @see NodeStatus
     */
    public volatile int status = NodeStatus.FOLLOWER;


    /* ============ 所有服务器上持久存在的 ============= */

    /**
     * 服务器最后一次知道的任期号（初始化为 0，持续递增）
     */
    volatile long currentTerm = 0;
    /**
     * 在当前获得选票的候选人的 Id
     */
    volatile String votedFor;



    /* ============ 所有服务器上经常变的 ============= */

    /**
     * 已知的最大的已经被提交的日志条目的索引值
     */
    volatile long commitIndex;

    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增)
     */
    volatile long lastApplied = 0;

    /* ========== 在领导人里经常改变的(选举后重新初始化) ================== */

    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    Map<NodeInfo, Long> nextIndexs;

    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    Map<NodeInfo, Long> matchIndexs;



    /* ============ 所有服务器上持久存在的 ============= */


    private NetworkService networkService = new NetworkServiceImpl();

    /**
     * 心跳任务
     */
    protected HeartBeatTask heartBeatTask;

    /**
     * 选举任务
     */
    protected ElectionTask electionTask;

    /**
     * 一致性服务
     */
    private RaftConsensusManager raftConsensusManager;

    /**
     * 日志服务
     */
    private RaftLogManager raftLogManager;

    DefaultNode() {
        //TODO 添加其他网络节点
        init();
    }

    abstract protected void init();


    /**
     * 设置心跳任务
     */
    abstract protected void setHeartBeatTask();


    /**
     * 设置选举任务
     */
    abstract protected void setElectionTask();

    protected void start() {
        synchronized (this) {

            raftConsensusManager = new RaftConsensusManagerImpl();
            raftLogManager = new RaftLogManagerImpl();

            //延时启动心跳
            RaftThreadPool.scheduleWithFixedDelay(heartBeatTask, 500);
            //第一次启动的选举任务延时为6S
            RaftThreadPool.scheduleAtFixedRate(electionTask, 6000, 500);
//            //复制
//            RaftThreadPool.execute(replicationFailQueueConsumer);

            LogEntry logEntry = raftLogManager.getLastLogEntry();
            if (logEntry != null) {
                currentTerm = logEntry.getTerm();
            }
//
//            LOGGER.info("start success, selfId : {} ", nodeInfoSet.getSelf());
        }
    }

    public void testHeartBeat() {
        heartBeatTask.run();
    }


    public NodeInfoSet getNodeInfoSet() {
        return nodeInfoSet;
    }

    public void setNodeInfoSet(NodeInfoSet nodeInfoSet) {
        this.nodeInfoSet = nodeInfoSet;
    }

    public long getElectionTime() {
        return electionTime;
    }

    public void setElectionTime(long electionTime) {
        this.electionTime = electionTime;
    }

    public long getPreElectionTime() {
        return preElectionTime;
    }

    public void setPreElectionTime(long preElectionTime) {
        this.preElectionTime = preElectionTime;
    }

    public long getPreHeartBeatTime() {
        return preHeartBeatTime;
    }

    public void setPreHeartBeatTime(long preHeartBeatTime) {
        this.preHeartBeatTime = preHeartBeatTime;
    }

    public long getHeartBeatTick() {
        return heartBeatTick;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(String votedFor) {
        this.votedFor = votedFor;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(long commitIndex) {
        this.commitIndex = commitIndex;
    }

    public long getLastApplied() {
        return lastApplied;
    }

    public void setLastApplied(long lastApplied) {
        this.lastApplied = lastApplied;
    }

    public Map<NodeInfo, Long> getNextIndexs() {
        return nextIndexs;
    }

    public void setNextIndexs(Map<NodeInfo, Long> nextIndexs) {
        this.nextIndexs = nextIndexs;
    }

    public Map<NodeInfo, Long> getMatchIndexs() {
        return matchIndexs;
    }

    public void setMatchIndexs(Map<NodeInfo, Long> matchIndexs) {
        this.matchIndexs = matchIndexs;
    }


    public NetworkService getNetworkService() {
        return networkService;
    }

    public void setNetworkService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public HeartBeatTask getHeartBeatTask() {
        return heartBeatTask;
    }

    public ElectionTask getElectionTask() {
        return electionTask;
    }


    public RaftConsensusManager getRaftConsensusManager() {
        return raftConsensusManager;
    }

    public void setRaftConsensusManager(RaftConsensusManager raftConsensusManager) {
        this.raftConsensusManager = raftConsensusManager;
    }

    public RaftLogManager getRaftLogManager() {
        return raftLogManager;
    }

    public void setRaftLogManager(RaftLogManager raftLogManager) {
        this.raftLogManager = raftLogManager;
    }
}
