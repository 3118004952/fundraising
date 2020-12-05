package com.cy.fundraising.mapper;

import com.cy.fundraising.dto.LoginResult;
import com.cy.fundraising.dto.ReadListResult;
import com.cy.fundraising.entities.GiftTblEntity;
import com.cy.fundraising.entities.ProjectTblEntity;
import com.cy.fundraising.entities.UserTblEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("select count(project_id) from project_tbl")
    int projectCount();

    @Select("select project_id, project_photo, project_people_nums, project_money_now, project_name from project_tbl limit ${pageIndex * pageSize}, #{pageSize} where project_state>2 and project_state<6")
    List<ReadListResult> readList(int pageIndex, int pageSize);

    @Select("select * from project_tbl where project_id=#{projectId} where project_state>2 and project_state<6")
    ProjectTblEntity readDetail(String projectId);

    @Update("update project_tbl set project_money_now=project_money_now+#{money}, project_people_nums=project_people_nums+1 where project_id=#{projectId} and project_state=3")
    int contributionUpdateProject(double money, String projectId);

    @Update("insert into gift_tbl(user_id, gift_id, gift_money, project_id, gift_time) values(#{userId}, #{giftId}, #{giftMoney}, #{projectId}, #{giftTime})")
    int contributionUpdateGiftTbl(GiftTblEntity giftTblEntity);
}
