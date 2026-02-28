package com.ruoyi;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RuoYiApplication {
    public static void main(String[] args) {

        SpringApplication.run(RuoYiApplication.class, args);
        String os = System.getProperty("os.name").toLowerCase();
        System.err.println(StrUtil.format("\n程序启动成功, 操作系统: {}\n", os));

    }
}
