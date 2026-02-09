package com.ruoyi.business.archive.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Snow
 */
@Component
@ConfigurationProperties(prefix = "bus.archive")
public class ArchiveConfig {

    @Getter
    private static String unzipDir;

    @Getter
    private static String storageDir;

    public void setUnzipDir(String unzipDir) {
        ArchiveConfig.unzipDir = unzipDir;
    }

    public void setStorageDir(String storageDir) {
        ArchiveConfig.storageDir = storageDir;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        if (StrUtil.isBlank(unzipDir)) {
            throw new RuntimeException("档案导入解压目录为空");
        }
        if (StrUtil.isBlank(storageDir)) {
            throw new RuntimeException("档案存储目录为空");
        }

        FileUtil.mkdir(unzipDir);
        FileUtil.mkdir(storageDir);
    }

}
