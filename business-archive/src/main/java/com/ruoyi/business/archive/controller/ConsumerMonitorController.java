package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.service.ConsumerMonitorService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Snow
 */
@RestController
@RequestMapping("business/archive/monitor")
public class ConsumerMonitorController {

    @Resource
    private ConsumerMonitorService consumerMonitorService;

    @GetMapping("cached")
    public Boolean allow() {
        // return false;
        return consumerMonitorService.checkAllow();
    }

}
