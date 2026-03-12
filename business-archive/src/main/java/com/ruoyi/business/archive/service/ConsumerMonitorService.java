package com.ruoyi.business.archive.service;

/**
 * @author Snow
 */
public interface ConsumerMonitorService {

    String CONFIG_KEY_ARCHIVE_VIEW_COUNT = "consumerMonitor-archiveViewCount";
    String CONFIG_KEY_APPLICATION_TOTAL_RUNTIME = "consumerMonitor-applicationTotalRuntime";

    void incrementDictValue(String dictLabel);

    void decrementDictValue(String dictLabel);

    Long getDictValue(String dictLabel);

    Boolean checkAllow();

}
