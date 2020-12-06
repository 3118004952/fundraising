package com.gdut.fundraising.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gdut.fundraising.entities.UserTblEntity;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class TokenUtil {

    static public String getToken(UserTblEntity userTblEntity) throws UnsupportedEncodingException {
        String token="";
        token= JWT.create().withClaim("time",new Date()).withAudience("" + userTblEntity.getUserId())
                .sign(Algorithm.HMAC256(userTblEntity.getUserPassword()));
        return token;
    }
}
