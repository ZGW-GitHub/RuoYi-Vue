package com.ruoyi.business;

import cn.hutool.core.io.FileUtil;
import com.ruoyi.RuoYiApplicationTest;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.util.CadreArchiveFileParseUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
public class BusinessTest extends RuoYiApplicationTest {

    @Resource
    private CadreArchiveFileParseUtil util;

    @Resource
    private CadreArchiveItemMapper cadreArchiveItemMapper;

    @Test
    public void test() {
        List<CadreArchiveItem> itemList = cadreArchiveItemMapper.listByArchiveId(Collections.singletonList(CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID), "");
        Map<String, CadreArchiveItem> itemMap = itemList.stream().collect(Collectors.toMap(CadreArchiveItem::getItemType, Function.identity(), (v1, v2) -> v1));

        try(InputStream inputStream = FileUtil.getInputStream("/Users/snow/Downloads/project-archive/test.zip")) {
            // util.parse("test.zip", inputStream, itemMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
