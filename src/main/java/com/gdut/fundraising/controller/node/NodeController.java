package com.gdut.fundraising.controller.node;

import com.gdut.fundraising.entities.raft.HeartBeatRequest;
import com.gdut.fundraising.service.NodeService;
import com.gdut.fundraising.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
@RestController
@RequestMapping("/node")
public class NodeController {
    @Autowired
    NodeService nodeService;

//    @GetMapping("/HEART_BEAT")
//    public Map answerHeartBeat(@RequestBody HeartBeatRequest request){
//        return JsonResult.success(nodeService.(token.substring(7), projectId)).result();
//    }

}
