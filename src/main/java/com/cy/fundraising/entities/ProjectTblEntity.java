package com.cy.fundraising.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTblEntity {
    private String userId;
    private String projectId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectFinishTime;
    private int projectState;
    private String projectName;
    private int projectPeopleNums;
    private double projectMoneyTarget;
    private double projectMoneyNow;
    private String projectPhoto;
    private String projectExplain;



}
