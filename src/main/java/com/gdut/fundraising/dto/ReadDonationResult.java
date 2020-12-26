package com.gdut.fundraising.dto;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ReadDonationResult {
    private String userName;
    private String giftId;
    private double giftMoney;
    private String projectId;
    private String giftTime;

    public String getUserName() {
        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(userName);
        int i = 0;
        while(m.find()){
            i++;
            if(i==1)
                continue;
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }
    public String getGiftTime()  {
        return giftTime.substring(0, 19);
    }
}
