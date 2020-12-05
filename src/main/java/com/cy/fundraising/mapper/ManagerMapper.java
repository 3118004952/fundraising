package com.cy.fundraising.mapper;

import com.cy.fundraising.entities.ProjectTblEntity;
import com.cy.fundraising.entities.UserTblEntity;
import org.apache.ibatis.annotations.Select;

public interface ManagerMapper {

    @Select("select * from user_tbl where user_token=#{token}")
    UserTblEntity selectUserByToken(String token);

    @Select("select * from project_tbl where project_id=#{projectId}")
    ProjectTblEntity selectProjectById(String projectId);
}
