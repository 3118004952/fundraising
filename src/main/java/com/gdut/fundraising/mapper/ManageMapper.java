package com.gdut.fundraising.mapper;

import com.gdut.fundraising.entities.ProjectTblEntity;
import com.gdut.fundraising.entities.UserTblEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ManageMapper {

    @Select("select * from user_tbl where user_token=#{token}")
    UserTblEntity selectUserByToken(String token);

    @Select("select * from project_tbl where project_id=#{projectId}")
    ProjectTblEntity selectProjectById(String projectId);

    @Update("update project_tbl set project_state=project_state+1 where project_id=#{projectId}")
    int SetProjectToNext(String projectId);

    @Update("update project_tbl set project_state=6 where project_id=#{projectId}")
    int SetProjectToError(String projectId);
}
