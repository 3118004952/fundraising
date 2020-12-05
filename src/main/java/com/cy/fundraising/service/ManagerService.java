package com.cy.fundraising.service;


import com.cy.fundraising.entities.ProjectTblEntity;
import com.cy.fundraising.entities.UserTblEntity;
import com.cy.fundraising.exception.BaseException;
import com.cy.fundraising.exception.SetProjectStateException;
import com.cy.fundraising.mapper.ManagerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ManagerService {


    @Resource
    ManagerMapper managerMapper;


    public void setProjectState(String token, String state, String projectId) throws BaseException {
        if(state == null || projectId == null)
            throw new SetProjectStateException(400, "请求内容缺失！");
        UserTblEntity userTblEntity = managerMapper.selectUserByToken(token);
        if (userTblEntity != null && "root".equals(userTblEntity.getUserId().substring(0, 4))){
            ProjectTblEntity project = managerMapper.selectProjectById(projectId);
            if(project == null)
                throw new SetProjectStateException(400, "项目未知！");
            if("next".equals(state)){
                if(project.getProjectState() < 5){

                }
                else{
                    throw new SetProjectStateException(400, "项目处于不可设置状态！");
                }
            }
            else{
                if("error".equals(state)){
                    if(project.getProjectState() == 1){

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
