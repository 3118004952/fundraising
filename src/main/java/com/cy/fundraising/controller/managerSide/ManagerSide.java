package com.cy.fundraising.controller.managerSide;

import com.cy.fundraising.exception.MyWebException;
import com.cy.fundraising.service.ManagerService;
import com.cy.fundraising.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
@RestController
@RequestMapping("/manage")
public class ManagerSide {

    @Autowired
    ManagerService managerService;


    @PostMapping("/setProjectState")
    public Map setProjectState(@RequestHeader("AUTHORIZATION") String token, @RequestBody Map<String, String> param) throws MyWebException {
        managerService.setProjectState(token.substring(7), param.get("state"), param.get("projectId"));
        return JsonResult.success(null).result();
    }
}
