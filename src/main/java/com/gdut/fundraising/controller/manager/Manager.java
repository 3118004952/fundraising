package com.gdut.fundraising.controller.manager;

import com.gdut.fundraising.entities.OrderTblEntity;
import com.gdut.fundraising.exception.BaseException;
import com.gdut.fundraising.service.ManageService;
import com.gdut.fundraising.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
@RestController
@RequestMapping("/manage")
public class Manager {

    @Autowired
    ManageService manageService;

    @PostMapping("/**")
    public Map notFound() throws BaseException {
        throw new BaseException(404,"Not Found!");
    }

    @GetMapping("/readProjectList")
    public Map readProjectList(@RequestHeader("AUTHORIZATION") String token, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize, @RequestParam int state){
        return JsonResult.success(manageService.readProjectList(token.substring(7), pageIndex , pageSize, state)).result();
    }

    @GetMapping("/readProjectDetail")
    public Map readProjectDetail(@RequestHeader("AUTHORIZATION") String token, @RequestParam("projectId") String projectId){
        return JsonResult.success(manageService.readProjectDetail(token.substring(7), projectId)).result();
    }

    @PostMapping("/setProjectState")
    public Map setProjectState(@RequestHeader("AUTHORIZATION") String token, @RequestBody Map<String, String> param) throws BaseException {
        manageService.setProjectState(token.substring(7), param.get("state"), param.get("projectId"));
        return JsonResult.success(null).result();
    }

    @PostMapping("/expenditure")
    public Map expenditure(@RequestHeader("AUTHORIZATION") String token, @RequestBody OrderTblEntity orderTblEntity){
        return JsonResult.success(manageService.expenditure(token.substring(7), orderTblEntity)).result();
    }


}
