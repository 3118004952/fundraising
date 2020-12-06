package com.gdut.fundraising.controller.user;

import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.entities.UserTblEntity;
import com.gdut.fundraising.exception.BaseException;

import com.gdut.fundraising.exception.RegisterException;
import com.gdut.fundraising.service.UserService;
import com.gdut.fundraising.util.JsonResult;
import com.gdut.fundraising.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice//全局异常处理
@RestController
@RequestMapping(value = "/user")
public class User {
    @Autowired
    private UserService userService;

    @PostMapping("/**")
    public Map notFound() throws BaseException {
        throw new BaseException(404,"Not Found!");
    }

    @PostMapping("/register")
    public Map register(@RequestBody UserTblEntity userTblEntity) throws Exception{
        if(userTblEntity.notNullRegister() == null){
            if(!userService.judgePhoneExist(userTblEntity.getUserPhone())){
                Map<String, Object> map = new HashMap<>();
                String token = TokenUtil.getToken(userTblEntity);
                userTblEntity.setUserToken(token);
                userTblEntity.setUserId(UUID.randomUUID().toString());
                userService.register(userTblEntity);
                map.put("userPhone", userTblEntity.getUserPhone());
                map.put("token", userTblEntity.getUserToken());
                return JsonResult.success(map).result();
            }
            else{
                throw new RegisterException(400,"该手机号码已存在!");
            }
        }
        else{
            throw new RegisterException(400,"部分数据为空!");
        }

    }

    @PostMapping("/login")
    public Map login(@RequestBody UserTblEntity userTblEntity) throws Exception{
        return JsonResult.success(userService.login(userTblEntity)).result();
    }

    @PostMapping("/launch")
    public Map launch(@RequestHeader("AUTHORIZATION")String token,  @RequestBody ProjectTblEntity projectTblEntity) throws BaseException {
        return JsonResult.success(userService.launch(token.substring(7), projectTblEntity)).result();
    }
    @RequestMapping("/uploadPhoto")
    public Map uploadAvatar(@RequestHeader("AUTHORIZATION")String token, @RequestParam("photo") MultipartFile file) throws BaseException {

        return JsonResult.success(userService.uploadPhoto(token.substring(7), file)).result();
    }

    @GetMapping("/readList")
    public Map readList(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize){
        return JsonResult.success(userService.readList(pageIndex , pageSize)).result();
    }



    @GetMapping("/readDetail")
    public Map readDetail(@RequestParam("projectId") String projectId){
        return JsonResult.success(userService.readDetail(projectId)).result();
    }

    @GetMapping("/contribution")
    public Map contribution(@RequestHeader("AUTHORIZATION")String token, @RequestParam("projectId") String projectId, @RequestParam("money") int money) throws BaseException {
        return JsonResult.success(userService.contribution(token.substring(7), projectId, money)).result();
    }

    @GetMapping("/readDonation")
    public Map readDonation(@RequestParam("projectId") String projectId){
        return JsonResult.success(userService.readDonation(projectId)).result();
    }
}
