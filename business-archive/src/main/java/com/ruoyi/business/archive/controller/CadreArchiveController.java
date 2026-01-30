package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.controller.domain.CadreArchiveInfo;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.common.domain.resp.PageResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 干部档案 Controller
 * 
 * @author Snow
 */
@RestController
@RequestMapping("business/archive/cadreArchive")
public class CadreArchiveController {

    @Resource
    private CadreArchiveService cadreArchiveService;

    @GetMapping("list")
    public PageResp<CadreArchiveInfo> list(CadreArchivePageReq req) {
        return cadreArchiveService.page(req);
    }

}