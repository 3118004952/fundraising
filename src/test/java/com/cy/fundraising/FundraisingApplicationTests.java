package com.cy.fundraising;


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


    }
}
