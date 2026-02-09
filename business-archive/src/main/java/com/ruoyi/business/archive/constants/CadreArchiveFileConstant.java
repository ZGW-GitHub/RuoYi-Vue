package com.ruoyi.business.archive.constants;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.business.archive.dal.enums.ArchiveItemTypeEnum;

/**
 * @author Snow
 */
public class CadreArchiveFileConstant {

    /**
     * 数字档案：人员基本信息
     */
    public static final String USER_BASIC_INFO = "人员基本信息";
    public static final String USER_NAME = "姓名";
    public static final String USER_SEX = "性别";
    public static final String USER_ETHNIC = "民族";
    public static final String USER_BIRTHDAY = "出生日期";
    public static final String USER_ID_NUMBER = "公民身份号码";
    public static final String USER_THIRD_ID = "人员ID";

    /**
     * 数字档案：档案目录
     */
    public static final String DIRECTORY_INFO = "目录信息";
    public static final String DIRECTORY_RECORD = "档案目录条目";
    public static final String TYPE_NO = "类号";
    public static final String SERIAL_NO = "序号";
    public static final String MATERIAL_NAME = "材料名称";
    public static final String MATERIAL_FORMATION_TIME = "材料形成时间";
    public static final String PAGE_COUNT = "页数";
    public static final String REMARK = "备注";
    public static final String IMAGE_DATA = "图像数据";
    public static final String ORIGINAL_IMAGE_DATA = "原始图像数据";
    public static final String ORIGINAL_IMAGE_DATA_TEXT = "原始图像数据_Text";
    public static final String OPTIMIZE_IMAGE_DATA = "优化图像数据";
    public static final String OPTIMIZE_IMAGE_DATA_TEXT = "优化图像数据_Text";
    public static final String IMAGE_PATH_TAG = "data";

    public static String getImageUrl(String archiveUrl, String imageName, ArchiveItemTypeEnum itemType) {
        if (itemType == ArchiveItemTypeEnum.ORIGINAL_IMAGE) {
            return StrUtil.format("{}/{}/{}/{}", archiveUrl, IMAGE_DATA, ORIGINAL_IMAGE_DATA, imageName);
        } else if (itemType == ArchiveItemTypeEnum.OPTIMIZE_IMAGE) {
            return StrUtil.format("{}/{}/{}/{}", archiveUrl, IMAGE_DATA, OPTIMIZE_IMAGE_DATA, imageName);
        }
        return StrUtil.EMPTY;
    }

}
