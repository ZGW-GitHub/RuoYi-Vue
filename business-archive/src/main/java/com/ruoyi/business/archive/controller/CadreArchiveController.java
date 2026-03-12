package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.controller.domain.CadreArchiveImportResp;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public CadreArchiveImportResp importArchive(List<MultipartFile> fileList,
                                                @RequestParam(required = false) Boolean updateSupport) {
        return cadreArchiveService.importArchive(fileList, updateSupport);
    }

    @GetMapping("importByDeveloper")
    public CadreArchiveImportResp importByDeveloper(@RequestParam String fileDir) {
        return cadreArchiveService.importByDeveloper(fileDir);
    }

    @DeleteMapping("delete")
    public void delete(@Valid @RequestBody IdsReq req) {
        cadreArchiveService.delete(req);
    }

    @PostMapping("export")
    public void export(@Valid IdsReq req, HttpServletResponse response) {
        cadreArchiveService.export(req, response);
    }

    @PutMapping("updateDept")
    public void updateDept(@RequestParam Long id, @RequestParam @NotNull Long deptId) {
        cadreArchiveService.updateDept(id, deptId);
    }

    @PutMapping("updateStockStatus")
    public void updateStockStatus(@RequestParam Long id, @RequestParam String archiveStockStatus) {
        cadreArchiveService.updateStockStatus(id, archiveStockStatus);
    }

}