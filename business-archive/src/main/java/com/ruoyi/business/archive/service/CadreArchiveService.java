package com.ruoyi.business.archive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.archive.controller.domain.CadreArchiveImportResp;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 干部档案 Service 接口
 * 
 * @author Snow
 */
public interface CadreArchiveService extends IService<CadreArchive> {

    /**
     * 列表
     *
     * @param req req
     * @return {@link PageResp }<{@link CadreArchiveResp }>
     */
    PageResp<CadreArchiveResp> page(CadreArchivePageReq req);

    /**
     * 导入档案
     *
     * @param fileList      文件列表
     * @param updateSupport 更新支持
     */
    CadreArchiveImportResp importArchive(List<MultipartFile> fileList, boolean updateSupport);

    /**
     * 删除
     *
     * @param req req
     */
    void delete(IdsReq req);

    void doDelete(IdsReq req);

}