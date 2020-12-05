package com.cy.fundraising.controller.managerSide;

import com.cy.fundraising.exception.BaseException;
import com.cy.fundraising.service.ManageService;
import com.cy.fundraising.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
@RestController
@RequestMapping("/manage")
public class Manager {

    @Autowired
    ManageService manageService;


    @PostMapping("/setProjectState")
    public Map setProjectState(@RequestHeader("AUTHORIZATION") String token, @RequestBody Map<String, String> param) throws BaseException {
        manageService.setProjectState(token.substring(7), param.get("state"), param.get("projectId"));
        return JsonResult.success(null).result();
    }
}
