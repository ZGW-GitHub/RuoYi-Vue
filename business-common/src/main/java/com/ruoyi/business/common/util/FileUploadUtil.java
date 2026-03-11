package com.ruoyi.business.common.util;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.business.common.web.exception.BizException;
import com.ruoyi.business.common.web.exception.code.BizExceptionCode;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Snow
 */
@Slf4j
public class FileUploadUtil {

    public static String fileUrlToPath(String fileUrl) {
        return fileUrl.replace(Constants.RESOURCE_PREFIX, RuoYiConfig.getProfile());
    }

    public static String filePathToUrl(String filePath) {
        return filePath.replace(RuoYiConfig.getProfile(), Constants.RESOURCE_PREFIX);
    }

    public static FileInfo fileInfo(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new BizException(BizExceptionCode.MESSAGE, "上传的文件名为空");
        }

        String fileType = StrUtil.subAfter(originalFilename, StrUtil.DOT, true);
        String fileName = StrUtil.subBefore(originalFilename, StrUtil.DOT, true);
        return new FileInfo().setFileName(fileName).setFileType(fileType);
    }

    @Data
    @Accessors(chain = true)
    public static class FileInfo {
        private String fileName;
        private String fileType;

        public String fillName() {
            return StrUtil.format("{}.{}", fileName, fileType);
        }
    }

}
