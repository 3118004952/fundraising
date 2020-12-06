package com.gdut.fundraising;


import com.gdut.fundraising.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class FundraisingApplicationTests {

    @Resource
    UserMapper userMapper;
    @Test
    void contextLoads() throws IOException {
        File file = new File("D:\\程序\\java\\fundraising\\photo\\9aa177ae-e33f-4747-a2a4-dc9415cfd8c0.jpg");
        FileInputStream q = new FileInputStream(file);
        String md5 = DigestUtils.md5Hex(q);
        q.close();
        System.out.println(md5);
        file = new File("D:\\程序\\java\\fundraising\\photo\\ee2d9a2b-6ad0-42fb-af8d-1b3cfe17e99f.jpg");
        md5 = DigestUtils.md5Hex(new FileInputStream(file));
        System.out.println(md5);
        file = new File("D:\\程序\\java\\fundraising\\photo\\f0e53c38-d17d-46d4-8853-0c5cb15ddaff.png");
        md5 = DigestUtils.md5Hex(new FileInputStream(file));
        System.out.println(md5);
    }
}
