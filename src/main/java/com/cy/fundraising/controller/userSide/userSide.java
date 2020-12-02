package com.cy.fundraising.controller.userSide;

import com.cy.fundraising.entities.ProjectTblEntity;
import com.cy.fundraising.entities.UserTblEntity;
import com.cy.fundraising.exception.MyExceptionEnum;
import com.cy.fundraising.exception.MyWebException;
import com.cy.fundraising.service.UserService;
import com.cy.fundraising.util.JsonResult;
import com.cy.fundraising.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice//全局异常处理
@RestController
@RequestMapping(value = "/userSide")
public class userSide {
    @Autowired
    private UserService userService;


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
                throw new MyWebException(MyExceptionEnum.PHONE_EXIST);
            }
        }
        else{
            throw new MyWebException(MyExceptionEnum.REGISTER_FALSE);
        }

    }

    @PostMapping("/login")
    public Map login(@RequestBody UserTblEntity userTblEntity) throws Exception{
        return JsonResult.success(userService.login(userTblEntity)).result();
    }

    @PostMapping("/launch")
    public Map launch(@RequestHeader("AUTHORIZATION")String token,  @RequestBody ProjectTblEntity projectTblEntity) throws MyWebException {
        return JsonResult.success(userService.launch(token.substring(7), projectTblEntity)).result();
    }
    @RequestMapping("/uploadPhoto")
    public Map uploadAvatar(@RequestHeader("AUTHORIZATION")String token, @RequestParam("photo") MultipartFile file, @RequestParam("projectId") String projectId) throws Exception, IOException, MyWebException {

        return JsonResult.success(userService.uploadPhoto(token.substring(7), file, projectId)).result();
    }
}
