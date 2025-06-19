/*
 * Copyright (C) <2023> <Snow>
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

package com.ruoyi.business.common.domain.resp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * @author Snow
 * @date 2023/5/19 11:28
 */
@Data
@Accessors(chain = true)
public class PageResp<T> {

	private static final PageResp EMPTY = new PageResp<>();

	/**
	 * 总数
	 */
	private long total = 0;

	/**
	 * 查询数据列表
	 */
	private List<T> records = Collections.emptyList();

	private PageResp() {
	}

	private PageResp(long total, List<T> records) {
		this.total = total;
		this.records = records;
	}

	public static <T> PageResp<T> of(long total, List<T> records) {
		return new PageResp<>(total, records);
	}

	public static <T> PageResp<T> of(IPage<T> page) {
		return of(page.getTotal(), page.getRecords());
	}

	@SuppressWarnings("unchecked")
	public static <T> PageResp<T> empty() {
		return (PageResp<T>) EMPTY;
	}

}
