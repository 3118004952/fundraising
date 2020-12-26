package com.gdut.fundraising.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftTblEntity {
    private String userId;
    private String giftId;
    private double giftMoney;
    private String projectId;
    private String giftTime;

    public String getGiftTime(){
        return giftTime.substring(0, 19);
    }

}
