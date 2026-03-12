package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.business.archive.service.ConsumerMonitorService;
import com.ruoyi.business.common.mapper.SysMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Snow
 */
@Slf4j
@Service
public class ConsumerMonitorServiceImpl implements ConsumerMonitorService {

    private static final String CONFIG_TYPE = "consumerMonitor";

    @Resource
    private SysMapper sysMapper;

    @Transactional(rollbackFor = Exception.class)
    public void incrementDictValue(String dictLabel) {
        try {
            sysMapper.incrementDictValue(CONFIG_TYPE, dictLabel);
        } catch (Throwable e) {
            log.error("incrementDictValue error : {}", e.getMessage(), e);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void decrementDictValue(String dictLabel) {
        try {
            sysMapper.decrementDictValue(CONFIG_TYPE, dictLabel);
        } catch (Throwable e) {
            log.error("incrementDictValue error : {}", e.getMessage(), e);
        }
    }

    public Long getDictValue(String dictLabel) {
        String dictValue = sysMapper.getDictValue(CONFIG_TYPE, dictLabel);
        if (StrUtil.isBlank(dictValue)) {
            return -1L;
        }

        return Long.parseLong(dictValue);
    }

    public Boolean checkAllow() {
        Long archiveViewCount = getDictValue(CONFIG_KEY_ARCHIVE_VIEW_COUNT);
        Long applicationTotalRuntime = getDictValue(CONFIG_KEY_APPLICATION_TOTAL_RUNTIME);

        return archiveViewCount > 0 || applicationTotalRuntime > 0;
    }

}
