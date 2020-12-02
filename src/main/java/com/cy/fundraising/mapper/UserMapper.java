package com.cy.fundraising.mapper;

import com.cy.fundraising.dto.LoginResult;
import com.cy.fundraising.entities.ProjectTblEntity;
import com.cy.fundraising.entities.UserTblEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    @Select("select count(user_phone) from user_tbl where user_phone=#{phone}")
    int selectUserByPhone(String phone);

    @Insert("insert into user_tbl(user_id, user_phone, user_password, user_name, user_address, user_bank, user_token) values(#{userId}, #{userPhone}, #{userPassword}, #{userName}, #{userAddress}, #{userBank}, #{userToken})")
    void register(UserTblEntity userTblEntity);

    @Select("select * from user_tbl where user_phone=#{userPhone} and user_password=#{userPassword}")
    LoginResult login(UserTblEntity userTblEntity);

    @Update("update user_tbl set user_token=#{userToken} where user_phone=#{userPhone} and user_password=#{userPassword}")
    int updateToken(UserTblEntity userTblEntity);

    @Select("select * from user_tbl where user_token=#{token}")
    UserTblEntity selectUserByToken(String token);

    @Insert("insert into project_tbl set user_id=#{userId}, project_id=#{projectId}, project_start_time=#{projectStartTime}, project_finish_time=#{projectFinishTime}, project_state=0, project_name=#{projectName}, project_people_nums=0, project_money_target=-1, project_money_now=0, project_explain=#{projectExplain}")
    void launch(ProjectTblEntity projectTblEntity);

    @Update("update project_tbl set project_photo=#{add} where project_id=#{projectId} and user_id=#{userId}")
    int updatePhoto(String userId, String projectId, String add);
}
