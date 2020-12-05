package com.cy.fundraising;

import com.cy.fundraising.entities.UserTblEntity;
import com.cy.fundraising.exception.MyExceptionEnum;
import com.cy.fundraising.mapper.UserMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


class FundraisingApplicationTests {

    @Resource
    UserMapper userMapper;
    @Test
    void contextLoads() {
        Map ma = new HashMap();
        System.out.println(ma.get("1"));
        MyExceptionEnum.REQUEST_FIELD_ERROR.setMessage("1");
        System.out.println(MyExceptionEnum.REQUEST_FIELD_ERROR.getMessage());

    }
}
