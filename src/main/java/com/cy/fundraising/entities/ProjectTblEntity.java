package com.cy.fundraising.entities;


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
    private Date projectStartTime;
    private Date projectFinishTime;
    private int projectState;
    private String projectName;
    private int projectPeopleNums;
    private double projectMoneyTarget;
    private double projectMoneyNow;
    private String projectPhoto;
    private String projectExplain;



}
