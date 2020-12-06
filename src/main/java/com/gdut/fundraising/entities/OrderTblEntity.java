package com.gdut.fundraising.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTblEntity {
    private String orderId;
    private double orderMoney;
    private String orderTime;
    private String orderExplain;


}
