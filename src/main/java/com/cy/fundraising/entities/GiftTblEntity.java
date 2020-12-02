package com.cy.fundraising.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftTblEntity {
    private String giftId;
    private double giftMoney;
    private Date giftTime;


}
