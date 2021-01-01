package com.gdut.fundraising.service;

import com.gdut.fundraising.dto.LoginResult;
import com.gdut.fundraising.dto.ReadDonationResult;
import com.gdut.fundraising.dto.ReadExpenditureResult;
import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.entities.UserTblEntity;
import com.gdut.fundraising.exception.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public interface UserService {

    /**
     * 注册
     * @param userTblEntity     用户信息
     * @return Map
     */
    Map<String, Object> register(UserTblEntity userTblEntity);

    /**
     * 登录
     * @param userTblEntity     登录的信息
     * @return Map
     * @throws BaseException
     */
    LoginResult login(UserTblEntity userTblEntity) throws BaseException;

    /**
     * 发起募捐活动
     * @param token             token验证消息
     * @param projectTblEntity  项目信息
     * @return
     * @throws BaseException
     */
    ProjectTblEntity launch(String token, ProjectTblEntity projectTblEntity) throws BaseException;

    /**
     * 项目封面图片上传
     * @param token     token验证消息
     * @param file      文件
     * @return
     * @throws BaseException
     */
    Map uploadPhoto(String token, MultipartFile file) throws BaseException;

    /**
     * 查看募捐列表
     * @param pageIndex     第几页
     * @param pageSize      每页数据大小
     * @return
     */
    Map readProjectList(int pageIndex , int pageSize);

    /**
     * 查看募捐详细内容
     * @param projectId     项目id
     * @return
     */
    ProjectTblEntity readProjectDetail(String projectId);

    /**
     * 捐款
     * @param token         token验证消息
     * @param projectId     项目id
     * @param money         钱的数量
     * @return
     * @throws BaseException
     */
    Map contribution(String token, String projectId, int money) throws BaseException;

    /**
     * 查看某个项目的募捐订单列表
     * @param projectId     项目id
     * @return
     */
    List<ReadDonationResult> readDonation(String projectId);

    /**
     * 查看支出的订单
     * @param projectId     项目id
     * @return
     */
    List<ReadExpenditureResult> readExpenditureResult(String projectId);
}
