package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.controller.domain.CadreArchiveImportResp;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.common.domain.resp.PageResp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 干部档案 Controller
 * 
 * @author Snow
 */
@Slf4j
@RestController
@RequestMapping("business/archive/cadreArchive")
public class CadreArchiveController {

    @Resource
    private CadreArchiveService cadreArchiveService;

    @GetMapping("list")
    public PageResp<CadreArchiveResp> list(CadreArchivePageReq req) {
        return cadreArchiveService.page(req);
    }

    @PostMapping("import")
    public CadreArchiveImportResp importArchive(List<MultipartFile> fileList, boolean updateSupport) {
        return cadreArchiveService.importArchive(fileList, updateSupport);
    }

}