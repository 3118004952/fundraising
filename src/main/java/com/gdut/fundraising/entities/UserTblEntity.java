package com.gdut.fundraising.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTblEntity {
    private String userId;
    private String userPhone;
    private String userPassword;
    private String userName;
    private String userAddress;
    private String userBank;
    private String userToken;

    public String notNullRegister(){
        if(userPhone == null){
            return "miss phone";
        }
        if(userPassword == null){
            return "miss password";
        }
        if(userName==null){
            return "miss user name";
        }
        if(userAddress==null){
            return "miss user address";
        }
        if(userBank==null){
            return "miss user bank";
        }

        return null;
    }



}
