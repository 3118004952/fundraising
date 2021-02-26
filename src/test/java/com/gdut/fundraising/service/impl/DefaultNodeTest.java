//package com.gdut.fundraising.service.impl;
//
//import com.gdut.fundraising.constant.raft.NodeStatus;
//import com.gdut.fundraising.entities.raft.DefaultNode;
//import com.gdut.fundraising.entities.raft.NodeInfo;
//import com.gdut.fundraising.entities.raft.NodeInfoSet;
//import org.testng.Assert;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class DefaultNodeTest {
//
//
//    @BeforeMethod
//    public void setUp() {
//    }
//
//
//    @Test
//    public void testTestHeartBeatTask() throws InterruptedException {
//        DefaultNode nodeService = new DefaultNode();
//        nodeService.status = NodeStatus.LEADER;
//        nodeService.setNodeInfoSet(buildServiceNode());
//        HashMap<NodeInfo, Long> map=new HashMap<NodeInfo, Long>();
//        nodeService.nextIndexs=map;
//        for(NodeInfo node:nodeService.getNodeInfoSet().getNodeExceptSelf()){
//            map.put(node,0L);
//        }
//        nodeService.testHeartBeat();
//
//        //延时个3000ms
//        TimeUnit.MILLISECONDS.sleep(3000);
//        //在其他节点没启动的情况下返回false
//        Assert.assertFalse(nodeService.getNodeInfoSet().getNodeExceptSelf().get(0).isAlive());
//        Assert.assertFalse(nodeService.getNodeInfoSet().getNodeExceptSelf().get(1).isAlive());
//    }
//
//    private NodeInfoSet buildServiceNode() {
//        NodeInfoSet nodeInfoSet = new NodeInfoSet();
//        NodeInfo ld = new NodeInfo("localhost", "8088");
//        NodeInfo f1 = new NodeInfo("localhost", "8089");
//        NodeInfo f2 = new NodeInfo("localhost", "8090");
//        f1.setAlive(true);
//        nodeInfoSet.setLeader(ld);
//        nodeInfoSet.setSelf(ld);
//        List<NodeInfo> list = new ArrayList<>();
//        list.add(f1);
//        list.add(f2);
//        nodeInfoSet.setAll(list);
//        return nodeInfoSet;
//    }
//}