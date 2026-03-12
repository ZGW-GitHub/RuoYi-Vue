package com.ruoyi.business.archive.service;

/**
 * @author Snow
 */
public interface ConsumerMonitorService {

    public static final String CONFIG_KEY_ARCHIVE_VIEW_COUNT = "consumerMonitor-archiveViewCount";
    public static final String CONFIG_KEY_APPLICATION_TOTAL_RUNTIME = "consumerMonitor-applicationTotalRuntime";

    void incrementDictValue(String dictLabel);

    void decrementDictValue(String dictLabel);

    Integer getDictValue(String dictLabel);

}
