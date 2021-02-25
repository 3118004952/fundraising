package com.gdut.fundraising.service.impl;



import com.gdut.fundraising.constant.raft.NodeStatus;
import com.gdut.fundraising.dto.raft.HeartBeatResult;
import com.gdut.fundraising.dto.raft.VoteResult;
import com.gdut.fundraising.entities.raft.*;

import com.gdut.fundraising.manager.RaftConsensusManager;
import com.gdut.fundraising.manager.RaftLogManager;
import com.gdut.fundraising.manager.impl.RaftConsensusManagerImpl;
import com.gdut.fundraising.manager.impl.RaftLogManagerImpl;
import com.gdut.fundraising.service.NetworkService;
import com.gdut.fundraising.service.NodeService;
import com.gdut.fundraising.task.RaftThreadPool;

import com.gdut.fundraising.util.JsonResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * 节点服务
 */
@Service
public class NodeServiceImpl implements NodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeServiceImpl.class);
    /**
     * 节点信息
     */
    private ServiceNode serviceNode;

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

    private HeartBeatTask heartBeatTask = new HeartBeatTask();

    private ElectionTask electionTask = new ElectionTask();

    private RaftConsensusManager raftConsensusManager;

    private RaftLogManager raftLogManager;

    NodeServiceImpl() {
        //TODO 添加其他网络节点
        init();
    }

    @Override
    public void init() {

        synchronized (this) {

            raftConsensusManager = new RaftConsensusManagerImpl();
            raftLogManager = new RaftLogManagerImpl();

//            //延时启动心跳
//            RaftThreadPool.scheduleWithFixedDelay(heartBeatTask, 500);
//            //第一次启动的选举任务延时为6S
//            RaftThreadPool.scheduleAtFixedRate(electionTask, 6000, 500);
//            //复制
//            RaftThreadPool.execute(replicationFailQueueConsumer);

            LogEntry logEntry = raftLogManager.getLastLogEntry();
            if (logEntry != null) {
                currentTerm = logEntry.getTerm();
            }
//
//            LOGGER.info("start success, selfId : {} ", serviceNode.getSelf());
        }
    }

    public void testHeartBeat() {
        heartBeatTask.run();
    }

    /**
     * 心跳任务，用来检测follower节点是否存活，交换信息
     */
    class HeartBeatTask implements Runnable {

        @Override
        public void run() {

            if (status != NodeStatus.LEADER) {
                return;
            }

            long current = System.currentTimeMillis();
            if (current - preHeartBeatTime < heartBeatTick) {
                return;
            }
            LOGGER.info("===========HeartBeatTask-NextIndex =============");
            for (NodeInfo node : serviceNode.getNodeExceptSelf()) {
                LOGGER.info("node {} nextIndex={}", node.getId(), nextIndexs.get(node));
            }

           preHeartBeatTime=System.currentTimeMillis();

            HeartBeatRequest heartBeatRequest = new HeartBeatRequest(currentTerm);

            // 心跳只关心 term
            for (NodeInfo node : serviceNode.getNodeExceptSelf()) {

                RaftThreadPool.execute(() -> {
                    try {
                        //通过心跳任务去获取别人的任期，从而加强一致性
                        JsonResult response = networkService.post(node.getIp(),
                                node.getPort(), heartBeatRequest);
                        HeartBeatResult heartBeatResult = (HeartBeatResult) response.getData();
                        long term = heartBeatResult.getTerm();

                        if (term > currentTerm) {
                            LOGGER.error("self will become follower, his term : {}, but my term : {}",
                                    term, currentTerm);
                            currentTerm=currentTerm;
                            votedFor="";
                            status=NodeStatus.FOLLOWER;
                        }
                    } catch (Exception e) {
                        //发生网络故障，说明该节点暂时不存活了
                        //TODO 是否需要这一步进行判断节点情况
                        synchronized (node) {
                            node.setAlive(false);
                        }
                        LOGGER.error("HeartBeatTask  Fail, request node : {}:{}", node.getIp(),
                                node.getPort());
                    }
                }, false);
            }
        }
    }

    /**
     * this is the core of the election.
     * 1. 在转变成候选人后就立即开始选举过程
     * 自增当前的任期号（currentTerm）
     * 给自己投票
     * 重置选举超时计时器
     * 发送请求投票的 RPC 给其他所有服务器
     * 2. 如果接收到大多数服务器的选票，那么就变成领导人
     * 3. 如果接收到来自新的领导人的附加日志 RPC，转变成跟随者
     * 4. 如果选举过程超时，再次发起一轮选举
     */
    class ElectionTask implements Runnable {

        @Override
        public void run() {

            if (status == NodeStatus.LEADER) {
                return;
            }

            long current = System.currentTimeMillis();
            // 基于 RAFT 的随机时间,解决冲突.
            electionTime=electionTime + ThreadLocalRandom.current().nextInt(50);
            //如果选举时间之前都没用收到心跳包或者Candidate请求投票的，则自己成为候选人
            if (current - preHeartBeatTime < electionTime ||
                    current - electionTime < electionTime) {
                return;
            }
            status=NodeStatus.CANDIDATE;
            LOGGER.error("node {} will become CANDIDATE and start election leader, current term : [{}], LastEntry : [{}]",
                    serviceNode.getSelf(), currentTerm, raftLogManager.getLastLogEntry());
            //更新新的选举时间
            preElectionTime=(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150);
            //更新任期
            currentTerm= currentTerm + 1;
            // 推荐自己.
            votedFor = serviceNode.getSelf().getId();
            /**
             * 获取除了自己的其他人的列表
             */
            List<NodeInfo> nodes = serviceNode.getNodeExceptSelf();

            ArrayList<Future> futureArrayList = new ArrayList<>();

            LOGGER.info("nodeList size : {}, node list content : {}", nodes.size(), nodes);

            VoteRequest voteRequest = new VoteRequest(serviceNode.getSelf().getId(), raftLogManager.getLastLogIndex(),
                    raftLogManager.getLastLogEntry().getTerm(), currentTerm);

            // 发送请求
            for (NodeInfo node : nodes) {

                futureArrayList.add(RaftThreadPool.submit(() -> {
                    long lastTerm = 0L;
                    LogEntry last = raftLogManager.getLastLogEntry();
                    if (last != null) {
                        lastTerm = last.getTerm();
                    }
                    try {
                        JsonResult<VoteResult> response = networkService.post(node.getIp(), node.getPort(), voteRequest);
                        return response;

                    } catch (Exception e) {
                        //TODO 这些地方可以添加对于节点剔除的判断
                        LOGGER.error("ElectionTask  Fail, request node : {}:{}", node.getIp(),
                                node.getPort());
                        return null;
                    }
                }));
            }

            AtomicInteger success2 = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(futureArrayList.size());

            LOGGER.info("futureArrayList.size() : {}", futureArrayList.size());
            // 等待结果.
            for (Future future : futureArrayList) {
                RaftThreadPool.submit(() -> {
                    try {

                        JsonResult<VoteResult> response = (JsonResult<VoteResult>) future.get(3000, MILLISECONDS);
                        if (response == null) {
                            return -1;
                        }
                        boolean isVoteGranted = ((VoteResult) response.getData()).isVoteGranted();
                        /**
                         * 成功被投票，则增加
                         * 否则更新任期
                         */
                        if (isVoteGranted) {
                            success2.incrementAndGet();
                        } else {
                            // 更新自己的任期.
                            long resTerm = ((VoteResult) response.getData()).getTerm();
                            if (resTerm >= currentTerm) {
                                currentTerm = resTerm;
                            }
                        }
                        return 0;
                    } catch (Exception e) {
                        LOGGER.error("future.get exception , e : ", e);
                        return -1;
                    } finally {
                        //减少投票期待
                        latch.countDown();
                    }
                });
            }

            try {
                // 稍等片刻
                latch.await(3500, MILLISECONDS);
            } catch (InterruptedException e) {
                LOGGER.warn("InterruptedException By Master election Task");
            }

            int success = success2.get();
            LOGGER.info("node {} maybe become leader , success count = {} , status : {}", serviceNode.getSelf(), success, NodeStatus.Enum.value(status));
            // 如果投票期间,有其他服务器发送 appendEntry , 就可能变成 follower ,这时,应该停止.
            if (status == NodeStatus.FOLLOWER) {
                return;
            }
            // 加上自身.
            //TODO 这里就是通过投票成为主节点，可以处理一些prepareWork
            if (success >= serviceNode.getAll().size() >> 1) {
                LOGGER.warn("node {} become leader ", serviceNode.getSelf());
                status = NodeStatus.LEADER;
                serviceNode.setLeader(serviceNode.getSelf());
                votedFor = "";
                becomeLeaderToDoThing();
            } else {
                // else 重新选举
                votedFor = "";
            }

        }
    }

    /**
     * 初始化所有的 nextIndex 值为自己的最后一条日志的 index + 1. 如果下次 RPC 时, 跟随者和leader 不一致,就会失败.
     * 那么 leader 尝试递减 nextIndex 并进行重试.最终将达成一致.
     */
    private void becomeLeaderToDoThing() {
        nextIndexs = new ConcurrentHashMap<>();
        matchIndexs = new ConcurrentHashMap<>();
        for (NodeInfo node : serviceNode.getNodeExceptSelf()) {
            nextIndexs.put(node, raftLogManager.getLastLogIndex() + 1);
            matchIndexs.put(node, 0L);
        }
    }

    public ServiceNode getServiceNode() {
        return serviceNode;
    }

    public void setServiceNode(ServiceNode serviceNode) {
        this.serviceNode = serviceNode;
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

    public void setHeartBeatTask(HeartBeatTask heartBeatTask) {
        this.heartBeatTask = heartBeatTask;
    }

    public ElectionTask getElectionTask() {
        return electionTask;
    }

    public void setElectionTask(ElectionTask electionTask) {
        this.electionTask = electionTask;
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
