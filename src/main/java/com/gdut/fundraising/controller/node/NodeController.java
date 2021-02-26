package com.gdut.fundraising.controller.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
