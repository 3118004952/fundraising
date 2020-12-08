package com.gdut.fundraising.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadExpenditureResult {
    private String formUserId;
    private String toUserName;
    private String toUserId;
    private String orderOperator;
    private String orderId;
    private double orderMoney;
    private String projectId;
    private String orderTime;
    private String orderExplain;


    public String getOrderTime(){
        return orderTime.substring(0, 19);
    }
}
