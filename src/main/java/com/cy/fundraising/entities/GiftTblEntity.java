package com.cy.fundraising.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftTblEntity {
    private String userId;
    private String giftId;
    private double giftMoney;
    private String projectId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date giftTime;


}
