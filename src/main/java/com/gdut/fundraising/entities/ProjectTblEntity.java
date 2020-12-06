package com.gdut.fundraising.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTblEntity {
    private String userId;
    private String projectId;
    private String projectStartTime;
    private String projectFinishTime;
    private int projectState;
    private String projectName;
    private int projectPeopleNums;
    private double projectMoneyTarget;
    private double projectMoneyNow;
    private String projectPhoto;
    private String projectExplain;

    public String checkTime(){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(projectStartTime);
            Date date2 = formatter.parse(projectFinishTime);
            if(!projectStartTime.equals(formatter.format(date1) ) || !projectFinishTime.equals(formatter.format(date2)) || date1.after(date2))
                return "time false";
        } catch (Exception e) {
            return "time false";
        }

        return null;
    }

}
