package com.cy.fundraising.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTblEntity {
    private String orderId;
    private double orderMoney;
    private Date orderTime;
    private String orderExplain;


}
