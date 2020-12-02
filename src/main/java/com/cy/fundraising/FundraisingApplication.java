package com.cy.fundraising;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = "com.cy.fundraising.filter")
@MapperScan("com.cy.fundraising.mapper")
@SpringBootApplication
public class FundraisingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundraisingApplication.class, args);
    }

}
