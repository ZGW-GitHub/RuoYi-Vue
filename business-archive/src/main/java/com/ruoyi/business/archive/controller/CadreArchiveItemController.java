package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeItem;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeReq;
import com.ruoyi.business.archive.service.CadreArchiveItemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @ResponseBody
    @GetMapping("tree")
    public List<CadreArchiveItemTreeItem> tree(CadreArchiveItemTreeReq req) {
        return cadreArchiveItemService.tree(req);
    }

}