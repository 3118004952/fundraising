package com.gdut.fundraising.service;


import com.gdut.fundraising.dto.ReadListResult;
import com.gdut.fundraising.entities.OrderTblEntity;
import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.entities.UserTblEntity;
import com.gdut.fundraising.exception.BaseException;
import com.gdut.fundraising.mapper.ManageMapper;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ManageService {


    @Resource
    ManageMapper manageMapper;


    public Map readProjectList(String token, int pageIndex , int pageSize, int state){
        //根据pageSize生成页数
        UserTblEntity userTblEntity = manageMapper.selectUserByToken(token);
        if (userTblEntity != null && "root".equals(userTblEntity.getUserId().substring(0, 4))){
            int totalPage = manageMapper.projectCount(state);
            if(totalPage % pageSize == 0){
                totalPage /= pageSize;
            }
            else{
                totalPage /= pageSize;
                totalPage++;
            }
            List<ReadListResult> project = manageMapper.readProjectList(pageIndex, pageSize, state);
            Map<String, Object> res = new HashMap<>();
            res.put("totalPage", totalPage);
            res.put("pageSize", pageSize);
            res.put("pageIndex", pageIndex);
            res.put("project", project);
            switch (state){
                case 0:
                    res.put("state", "发起");
                    break;
                case 1:
                    res.put("state", "审核");
                    break;
                case 2:
                    res.put("state", "审核成功");
                    break;
                case 3:
                    res.put("state", "募捐");
                    break;
                case 4:
                    res.put("state", "执行");
                    break;
                case 5:
                    res.put("state", "结束");
                    break;
                case 6:
                    res.put("state", "审核失败");
                    break;
                default:
                    res.put("state", "未知状态");
            }
            return res;
        }
        else{
            throw new BaseException(400, "token认证失败！");
        }
    }

    public ProjectTblEntity readProjectDetail(String token, String projectId){
        UserTblEntity userTblEntity = manageMapper.selectUserByToken(token);
        if (userTblEntity != null && "root".equals(userTblEntity.getUserId().substring(0, 4))) {
            return manageMapper.readProjectDetail(projectId);
        }
        else{
            throw new BaseException(400, "token认证失败！");
        }
    }

    public void setProjectState(String token, String state, String projectId) throws BaseException {
        if(state == null || projectId == null)
            throw new BaseException(400, "请求内容缺失！");
        UserTblEntity userTblEntity = manageMapper.selectUserByToken(token);
        if (userTblEntity != null && "root".equals(userTblEntity.getUserId().substring(0, 4))){
            ProjectTblEntity project = manageMapper.selectProjectById(projectId);
            if(project == null)
                throw new BaseException(400, "项目未知！");
            if("next".equals(state)){
                if(project.getProjectState() < 5){
                    if(1 != manageMapper.SetProjectToNext(projectId))
                        throw new BaseException(400 ,"项目未知！");
                }
                else{
                    throw new BaseException(400, "项目处于不可设置状态！");
                }
            }
            else{
                if("error".equals(state)){
                    if(project.getProjectState() == 1){
                        if(1 != manageMapper.SetProjectToError(projectId))
                            throw new BaseException(400 ,"项目未知！");
                    }
                    else{
                        throw new BaseException(400, "项目不可设置该状态！");
                    }
                }
                else{
                    throw new BaseException(400, "字段错误！");
                }
            }
        }
        else{
            throw new BaseException(400, "token认证失败！");
        }
    }

    public OrderTblEntity expenditure(String token, OrderTblEntity orderTblEntity){
        UserTblEntity userTblEntity = manageMapper.selectUserByToken(token);
        if (userTblEntity != null && userTblEntity.getUserId().length() >= 4 && "root".equals(userTblEntity.getUserId().substring(0, 4))){
            orderTblEntity.setOrderOperator(userTblEntity.getUserId());
            orderTblEntity.setOrderId(UUID.randomUUID().toString());
            orderTblEntity.setOrderTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
            if(orderTblEntity.getOrderMoney() <= 0){
                throw new BaseException(400, "支出的金钱不可小于等于零！");
            }
            if(manageMapper.selectUserById(orderTblEntity) != 1){
                throw new BaseException(400, "支出的目标不存在！");
            }
            if(manageMapper.selectProjectByIdAndUser(orderTblEntity) != 1){
                throw new BaseException(400, "项目不存在或项目拥有者错误！");
            }
            if(manageMapper.expenditure(orderTblEntity) != 1){
                throw new BaseException(500, "服务器存储数据出错！");
            }
            return orderTblEntity;
        }
        else{
            throw new BaseException(400, "token认证失败！");
        }
    }


}
