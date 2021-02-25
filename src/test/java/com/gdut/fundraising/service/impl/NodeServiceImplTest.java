package com.gdut.fundraising.service.impl;

import com.gdut.fundraising.constant.raft.NodeStatus;
import com.gdut.fundraising.entities.raft.NodeInfo;
import com.gdut.fundraising.entities.raft.ServiceNode;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class NodeServiceImplTest {


    @BeforeMethod
    public void setUp() {
    }


    @Test
    public void testTestHeartBeatTask() throws InterruptedException {
        NodeServiceImpl nodeService = new NodeServiceImpl();
        nodeService.status = NodeStatus.LEADER;
        nodeService.setServiceNode(buildServiceNode());
        HashMap<NodeInfo, Long> map=new HashMap<NodeInfo, Long>();
        nodeService.nextIndexs=map;
        for(NodeInfo node:nodeService.getServiceNode().getNodeExceptSelf()){
            map.put(node,0L);
        }
        nodeService.testHeartBeat();

        //延时个3000ms
        TimeUnit.MILLISECONDS.sleep(3000);
        //在其他节点没启动的情况下返回false
        Assert.assertFalse(nodeService.getServiceNode().getNodeExceptSelf().get(0).isAlive());
        Assert.assertFalse(nodeService.getServiceNode().getNodeExceptSelf().get(1).isAlive());
    }

    private ServiceNode buildServiceNode() {
        ServiceNode serviceNode = new ServiceNode();
        NodeInfo ld = new NodeInfo("localhost", "8088");
        NodeInfo f1 = new NodeInfo("localhost", "8089");
        NodeInfo f2 = new NodeInfo("localhost", "8090");
        f1.setAlive(true);
        serviceNode.setLeader(ld);
        serviceNode.setSelf(ld);
        List<NodeInfo> list = new ArrayList<>();
        list.add(f1);
        list.add(f2);
        serviceNode.setAll(list);
        return serviceNode;
    }
}