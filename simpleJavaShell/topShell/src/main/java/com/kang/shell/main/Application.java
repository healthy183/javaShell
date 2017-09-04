package com.kang.shell.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/4.
 * @Author Healthy
 * @Version
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.kang"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
