package com.gdut.fundraising.service;

import com.gdut.fundraising.entities.OrderTblEntity;
import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.exception.BaseException;



import java.util.*;


public interface ManageService {


    /**
     * 按项目状态查看募捐列表
     * @param token         token验证消息
     * @param pageIndex     第几页
     * @param pageSize      一页多少条数据
     * @param state         查看的状态
     * @return  Map
     */
    Map readProjectList(String token, int pageIndex , int pageSize, int state);

    /**
     * 查看募捐详细内容
     * @param token         token验证消息
     * @param projectId     查看的项目id
     * @return
     */
    ProjectTblEntity readProjectDetail(String token, String projectId);

    /**
     * 管理某个项目的状态
     * @param token         token验证消息
     * @param nowState      当前状态
     * @param nextState     进入的下一个状态
     * @param projectId     项目id
     * @return  Map
     * @throws BaseException
     */
    Map setProjectState(String token, Integer nowState, Integer nextState, String projectId) throws BaseException;

    /**
     * 将钱分配到用户
     * @param token             token验证消息
     * @param orderTblEntity    订单信息
     * @return
     */
    OrderTblEntity expenditure(String token, OrderTblEntity orderTblEntity);


}
