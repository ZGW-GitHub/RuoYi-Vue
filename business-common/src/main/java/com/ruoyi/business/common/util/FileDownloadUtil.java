package com.ruoyi.business.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.ruoyi.business.common.web.exception.BizException;
import com.ruoyi.business.common.web.exception.code.BizExceptionCode;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Snow
 */
@Slf4j
public class FileDownloadUtil {

    public static void download(HttpServletResponse response, String fileName, List<File> fileList) {
        if (CollUtil.isEmpty(fileList)) {
            throw new BizException(BizExceptionCode.MESSAGE, "没有可下载的文件");
        }

        try {
            String tempDir = RuoYiConfig.getDownloadPath() + "temp_" + LocalDate.now().format(DatePattern.NORM_DATE_FORMATTER);
            FileUtil.mkdir(tempDir);

            String zipFileName = fileName + "_" + LocalDateTime.now().format(DatePattern.PURE_DATETIME_MS_FORMATTER) + ".zip";
            String zipFilePath = StrUtil.format("{}{}{}", tempDir, FileUtil.FILE_SEPARATOR, zipFileName);
            ZipUtil.zip(new File(zipFilePath), true, fileList.toArray(new File[0]));

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            FileUtils.setAttachmentResponseHeader(response, zipFileName);

            // 写文件
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                FileUtil.writeToStream(zipFilePath, outputStream);
            }

            // 删除文件
            try {
                FileUtil.del(zipFilePath);
            } catch (Exception e) {
                log.error("删除临时文件失败，路径：{}", zipFilePath, e);
            }
        } catch (Exception e) {
            throw new BizException(BizExceptionCode.MESSAGE, "文件下载失败");
        }
    }

}
