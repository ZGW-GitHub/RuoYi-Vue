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

package com.ruoyi.business.framework.web.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * @author Snow
 * @date 2024/2/27
 */
public class RequestUtil {

	public static final List<String> WHITE_LIST = Arrays.asList(
			"/favicon.ico",
			"/assets/**",
			"/**/**.js",
			"/**/**.css",
			"/**/**.html",
			"/**/**.json"
	);

	public static boolean isWhiteList(String path) {
		return isWhiteList(WHITE_LIST, path);
	}

	public static boolean isWhiteList(HttpServletRequest request) {
		return isWhiteList(WHITE_LIST, getRequestUrl(request));
	}

	public static boolean isWhiteList(List<String> whiteList, HttpServletRequest request) {
		return isWhiteList(whiteList, getRequestUrl(request));
	}

	@SuppressWarnings("all")
	public static boolean isWhiteList(List<String> whiteList, String path) {
		if (CollUtil.isEmpty(whiteList)) {
			return false;
		}

		AntPathMatcher antPathMatcher = new AntPathMatcher();
		for (String white : whiteList) {
			if (antPathMatcher.match(white, path)) {
				return true;
			}
		}
		return false;
	}

	public static String getRequestUrl(final HttpServletRequest request) {
		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();

		if (StrUtil.isBlank(pathInfo)) {
			return servletPath;
		}
		return StrUtil.isBlank(servletPath) ? pathInfo : servletPath + pathInfo;
	}

}
