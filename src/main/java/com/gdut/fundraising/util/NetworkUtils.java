package com.gdut.fundraising.util;

import com.gdut.fundraising.constant.raft.MessageType;
import com.gdut.fundraising.dto.raft.Request;
import org.springframework.web.client.RestTemplate;


public class NetworkUtils {

    public static <R> JsonResult postByHttp(String url,R data){
        RestTemplate restTemplate = new RestTemplate();

        JsonResult result = restTemplate.postForObject(url, data, JsonResult.class);

        return result;
    }

    /**
     * 通过ip地址+port端口+消息类型 去构建url
     * @param ip
     * @param port
     * @param request
     * @return
     */
    public static String buildUrl(String ip, String port, Request request){
        String url="";
        url+="http://"+ip+":"+port+"/node/"+ MessageType.getMessageType(request.getType());
        return url;
    }


}
