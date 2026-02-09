package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageRecord;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeResp;
import com.ruoyi.business.archive.service.CadreArchiveItemService;
import com.ruoyi.business.common.domain.resp.PageResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 档案项表 控制层
 *
 * @author Snow
 */
@RestController
@RequestMapping("business/archive/archiveItem")
public class CadreArchiveItemController {

    @Resource
    private CadreArchiveItemService cadreArchiveItemService;

    @GetMapping("tree")
    public CadreArchiveItemTreeResp tree(CadreArchiveItemTreeReq req) {
        return cadreArchiveItemService.tree(req);
    }

    @GetMapping("page")
    public PageResp<CadreArchiveItemPageRecord> page(CadreArchiveItemPageReq req) {
        return cadreArchiveItemService.page(req);
    }

    @PostMapping("import")
    public void importArchive(List<MultipartFile> fileList) {

    }

}