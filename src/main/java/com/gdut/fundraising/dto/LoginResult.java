package com.gdut.fundraising.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    private int userManage;
    private String userPhone;
    private String userName;
    private String userAddress;
    private String userBank;
    private String token;
}
