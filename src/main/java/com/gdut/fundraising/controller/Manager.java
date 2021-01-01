package com.gdut.fundraising.controller;

import com.gdut.fundraising.entities.OrderTblEntity;
import com.gdut.fundraising.exception.BaseException;

import java.util.Map;



/***
 * 处理manager方的请求
 * @RequestMapping("/manage")
 */
public interface Manager {


    /**
     * 处理该路径下的路径未找到问题
     * @PostMapping("/**")
     * @return Map
     * @throws BaseException
     */
    Map notFound() throws BaseException ;

    /**
     * 按项目状态查看募捐列表
     * @GetMapping("/readProjectList")
     * @param token         token验证消息
     * @param pageIndex     第几页
     * @param pageSize      一页多少条数据
     * @param state         查看的状态
     * @return Map
     */
    Map readProjectList(String token, int pageIndex, int pageSize, int state);

    /**
     * 查看募捐详细内容
     * @GetMapping("/readProjectDetail")
     * @param token         token验证消息
     * @param projectId     查看的项目id
     * @return Map
     */
    Map readProjectDetail(String token, String projectId);

    /**
     * 管理某个项目的状态
     * @PostMapping("/setProjectState")
     * @param token     token验证消息
     * @param param     参数
     * @return Map
     * @throws BaseException
     */
    Map setProjectState(String token, Map<String, Object> param) throws BaseException;

    /**
     * 将钱分配到用户
     * @PostMapping("/expenditure")
     * @param token             token验证消息
     * @param orderTblEntity    订单信息
     * @return Map
     */
    Map expenditure(String token, OrderTblEntity orderTblEntity);


}
