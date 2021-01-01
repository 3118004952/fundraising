package com.gdut.fundraising.controller;

import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.entities.UserTblEntity;
import com.gdut.fundraising.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * 处理manager方的请求
 * @RequestMapping(value = "/user")
 */
public interface User {

    /**
     * 处理该路径下的路径未找到问题
     * @PostMapping("/**")
     * @return Map
     * @throws BaseException
     */
    Map notFound() throws BaseException;

    /**
     * 注册
     * @PostMapping("/register")
     * @param userTblEntity     用户信息
     * @return Map
     * @throws BaseException
     */
    Map register(UserTblEntity userTblEntity) throws BaseException;

    /**
     * 登录
     * @PostMapping("/login")
     * @param userTblEntity     登录的信息
     * @return Map
     * @throws BaseException
     */
    Map login(UserTblEntity userTblEntity) throws BaseException;

    /**
     * 发起募捐活动
     * @PostMapping("/launch")
     * @param token             token验证消息
     * @param projectTblEntity  项目信息
     * @return Map
     * @throws BaseException
     */
    Map launch( String token, ProjectTblEntity projectTblEntity) throws BaseException ;

    /**
     * 项目封面图片上传
     * @RequestMapping("/uploadPhoto")
     * @param token     token验证消息
     * @param file      文件
     * @return Map
     * @throws BaseException
     */
    Map uploadAvatar(String token, MultipartFile file) throws BaseException ;

    /**
     * 查看募捐列表
     * @GetMapping("/readProjectList")
     * @param pageIndex     第几页
     * @param pageSize      每页数据大小
     * @return Map
     */
    Map readProjectList(int pageIndex, int pageSize);

    /**
     * 查看募捐详细内容
     * @GetMapping("/readProjectDetail")
     * @param projectId     项目id
     * @return Map
     */
    Map readProjectDetail(String projectId);

    /**
     * 捐款
     * @GetMapping("/contribution")
     * @param token         token验证消息
     * @param projectId     项目id
     * @param money         钱的数量
     * @return Map
     * @throws BaseException
     */
    Map contribution(String token, String projectId, int money) throws BaseException ;

    /**
     * 查看某个项目的募捐订单列表
     * @GetMapping("/readDonation")
     * @param projectId     项目id
     * @return Map
     */
    Map readDonation(String projectId);

    /**
     * 查看支出的订单
     * @GetMapping("/readExpenditure")
     * @param projectId     项目id
     * @return Map
     */
    Map readExpenditureResult(String projectId);
}
