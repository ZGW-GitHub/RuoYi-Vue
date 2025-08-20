/*
 * Copyright (C) <2024> <Snow>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ruoyi.business.common.util.excel;

import cn.hutool.core.util.PageUtil;
import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.write.metadata.WriteSheet;
import com.ruoyi.business.common.web.exception.SysException;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Snow
 * @date 2023/11/15
 */
@Slf4j
public class ExcelWriteUtil {

	public static final Integer BATCH_SIZE = 5000;

	/**
	 * 写入 Excel 数据到 OutputStream
	 *
	 * @param outputStream 输出流
	 * @param sheetName    工作表名称
	 * @param clazz        数据实体
	 * @param supplier     获取数据的 supplier
	 * @throws Exception e
	 */
	public static <E> void writeData(OutputStream outputStream, String sheetName, Class<E> clazz, Supplier<List<E>> supplier) throws Exception {
		try (ExcelWriter excelWriter = FastExcel.write(outputStream).autoCloseStream(false).build()) {
			writeData(excelWriter, 0, sheetName, clazz, supplier);
		}
	}

	/**
	 * 分批写入 Excel 数据到 OutputStream
	 *
	 * @param outputStream 输出流
	 * @param sheetName    工作表名称
	 * @param clazz        数据实体
	 * @param biFunction   获取数据的 biFunction
	 * @param totalCount   数据总数
	 * @throws SysException e
	 */
	public static <E> void batchWriteData(OutputStream outputStream, String sheetName, Class<E> clazz, BiFunction<Integer, Integer, List<E>> biFunction, Integer totalCount) throws Exception {
		batchWriteData(outputStream, sheetName, clazz, biFunction, totalCount, ExcelWriteUtil.BATCH_SIZE);
	}

	/**
	 * 分批写入 Excel 数据到 OutputStream
	 *
	 * @param outputStream 输出流
	 * @param sheetName    工作表名称
	 * @param clazz        数据实体
	 * @param biFunction   获取数据的 biFunction
	 * @param totalCount   数据总数
	 * @param batchSize    批次大小
	 * @throws SysException e
	 */
	public static <E> void batchWriteData(OutputStream outputStream, String sheetName, Class<E> clazz, BiFunction<Integer, Integer, List<E>> biFunction, Integer totalCount, Integer batchSize) throws Exception {
		try (ExcelWriter excelWriter = EasyExcel.write(outputStream).autoCloseStream(false).build()) {
			batchWriteData(excelWriter, 0, sheetName, clazz, biFunction, totalCount, batchSize);
		}
	}

	/**
	 * 写入 Excel 数据到 ExcelWriter
	 *
	 * @param excelWriter excelWriter
	 * @param sheetNo     工作表编号
	 * @param sheetName   工作表名称
	 * @param clazz       数据实体
	 * @param supplier    获取数据的 supplier
	 *
	 * @throws Exception e
	 */
	public static <E> void writeData(ExcelWriter excelWriter, int sheetNo, String sheetName, Class<E> clazz, Supplier<List<E>> supplier) throws Exception {
		try {
			WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, sheetName).head(clazz).build();

			excelWriter.write(getData(supplier), writeSheet);
		} catch (SysException e) {
			throw e;
		} catch (Throwable t) {
			throw new SysException("文件写入异常", t);
		}
	}

	/**
	 * 分批写入 Excel 数据到 ExcelWriter
	 *
	 * @param excelWriter excelWriter
	 * @param sheetNo     工作表编号
	 * @param sheetName   工作表名称
	 * @param clazz       数据实体
	 * @param biFunction  获取数据的 biFunction
	 * @param totalCount  数据总数
	 * @throws Exception e
	 */
	public static <E> void batchWriteData(ExcelWriter excelWriter, int sheetNo, String sheetName, Class<E> clazz, BiFunction<Integer, Integer, List<E>> biFunction, Integer totalCount) throws Exception {
		batchWriteData(excelWriter, sheetNo, sheetName, clazz, biFunction, totalCount, ExcelWriteUtil.BATCH_SIZE);
	}

	/**
	 * 分批写入 Excel 数据到 ExcelWriter
	 *
	 * @param excelWriter excelWriter
	 * @param sheetNo     工作表编号
	 * @param sheetName   工作表名称
	 * @param clazz       数据实体
	 * @param biFunction  获取数据的 biFunction
	 * @param totalCount  数据总数
	 * @param batchSize   批次大小
	 * @throws Exception e
	 */
	public static <E> void batchWriteData(ExcelWriter excelWriter, int sheetNo, String sheetName, Class<E> clazz, BiFunction<Integer, Integer, List<E>> biFunction, Integer totalCount, Integer batchSize) throws Exception {
		try {
			WriteSheet writeSheet = FastExcel.writerSheet(sheetNo, sheetName).head(clazz).build();

			int batchCount = PageUtil.totalPage(totalCount, ExcelWriteUtil.BATCH_SIZE);
			for (int batchIndex = 0; batchIndex < batchCount; batchIndex++) {
				int batchStart = PageUtil.getStart(batchIndex, batchSize);

				Supplier<List<E>> supplier = () -> biFunction.apply(batchStart, batchSize);

				excelWriter.write(getData(supplier), writeSheet);
			}
		} catch (SysException e) {
			throw e;
		} catch (Throwable t) {
			throw new SysException("文件写入异常", t);
		}
	}

	private static <E> List<E> getData(Supplier<List<E>> supplier) throws Exception {
		try {
			return supplier.get();
		} catch (SysException e) {
			throw e;
		} catch (Throwable t) {
			throw new SysException("查询数据异常", t);
		}
	}

}
