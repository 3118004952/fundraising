package com.gdut.fundraising.service;


import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.entities.UserTblEntity;
import com.gdut.fundraising.exception.BaseException;
import com.gdut.fundraising.exception.SetProjectStateException;
import com.gdut.fundraising.mapper.ManageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ManageService {


    @Resource
    ManageMapper manageMapper;


    public void setProjectState(String token, String state, String projectId) throws BaseException {
        if(state == null || projectId == null)
            throw new SetProjectStateException(400, "请求内容缺失！");
        UserTblEntity userTblEntity = manageMapper.selectUserByToken(token);
        if (userTblEntity != null && "root".equals(userTblEntity.getUserId().substring(0, 4))){
            ProjectTblEntity project = manageMapper.selectProjectById(projectId);
            if(project == null)
                throw new SetProjectStateException(400, "项目未知！");
            if("next".equals(state)){
                if(project.getProjectState() < 5){
                    if(1 != manageMapper.SetProjectToNext(projectId))
                        throw new SetProjectStateException(400 ,"项目未知！");
                }
                else{
                    throw new SetProjectStateException(400, "项目处于不可设置状态！");
                }
            }
            else{
                if("error".equals(state)){
                    if(project.getProjectState() == 1){
                        if(1 != manageMapper.SetProjectToError(projectId))
                            throw new SetProjectStateException(400 ,"项目未知！");
                    }
                    else{
                        throw new SetProjectStateException(400, "项目不可设置该状态！");
                    }
                }
                else{
                    throw new SetProjectStateException(400, "字段错误！");
                }
            }
        }
        else{
            throw new SetProjectStateException(400, "token认证失败！");
        }
    }


}
