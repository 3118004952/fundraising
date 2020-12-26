package com.gdut.fundraising.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadListResult {
    private String projectId;
    private String projectName;
    private int projectPeopleNums;
    private double projectMoneyNow;
    private String projectPhoto;
}
