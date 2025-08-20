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

import com.ruoyi.business.common.web.exception.SysException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Snow
 * @date 2024/3/24 09:35
 */
@Slf4j
public class ExcelExportUtil {


	/**
	 * 直接下载 Excel 到 Response
	 *
	 * @param response response
	 * @param fileName 文件名
	 * @param clazz    数据实体
	 * @param supplier 获取数据的 supplier
	 *
	 * @throws Exception e
	 */
	public static <E> void download(HttpServletResponse response, final String fileName, Class<E> clazz
			, Supplier<List<E>> supplier) throws Exception {

		wrapResponse(response, fileName);

		OutputStream outputStream = getResponseOutputStream(response);

		// 写文件
		ExcelWriteUtil.writeData(outputStream, fileName, clazz, supplier);
	}

	/**
	 * 直接下载 Excel 到 Response
	 *
	 * @param response   response
	 * @param fileName   文件名
	 * @param clazz      数据实体
	 * @param biFunction 获取数据的 biFunction
	 * @param totalCount 数据总数
	 *
	 * @throws Exception e
	 */
	public static <E> void download(HttpServletResponse response, String fileName, Class<E> clazz
			, BiFunction<Integer, Integer, List<E>> biFunction, Integer totalCount) throws Exception {
		download(response, fileName, clazz, biFunction, totalCount, ExcelWriteUtil.BATCH_SIZE);
	}

	/**
	 * 直接下载 Excel 到 Response
	 *
	 * @param response   response
	 * @param fileName   文件名
	 * @param clazz      数据实体
	 * @param biFunction 获取数据的 biFunction
	 * @param totalCount 数据总数
	 * @param batchSize  批次大小
	 *
	 * @throws Exception e
	 */
	public static <E> void download(HttpServletResponse response, String fileName, Class<E> clazz
			, BiFunction<Integer, Integer, List<E>> biFunction, Integer totalCount, Integer batchSize) throws Exception {

		wrapResponse(response, fileName);

		OutputStream outputStream = getResponseOutputStream(response);

		// 写文件
		ExcelWriteUtil.batchWriteData(outputStream, fileName, clazz, biFunction, totalCount, batchSize);
	}

	private static void wrapResponse(HttpServletResponse response, final String fileName) {
		String fileNameEncoded = encodeFileName(fileName);

		// 注意：有同学反应使用 swagger 会导致各种问题，请直接用浏览器或者用 postman
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("utf-8");

		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileNameEncoded + ".xlsx");
		// 增加暴露给客户端的响应头（ 浏览器默认客户端可见的响应头有：Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma ）
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
	}

	private static OutputStream getResponseOutputStream(HttpServletResponse response) throws Exception {
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			throw new SysException("获取 Response 的 OutPutStream 异常", e);
		}
	}

	private static String encodeFileName(final String fileName) {
		String fileNameByTime = fileName + "_" + System.currentTimeMillis();

		// 这里用 URLEncoder.encode 可以防止中文乱码
		return URLEncoder.encode(fileNameByTime, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
	}
}
