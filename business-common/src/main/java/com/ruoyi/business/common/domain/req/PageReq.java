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

package com.ruoyi.business.common.domain.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Snow
 */
@Data
public class PageReq implements Serializable {

	/**
	 * 页码
	 */
	@Min(value = 1, message = "页码不合法，应大于等于 1")
	private long pageNum = 1;

	/**
	 * 每页显示条数，默认 10
	 */
	@Min(value = 1, message = "每页显示条数不合法，应大于等于 1")
	@Max(value = 300, message = "每页显示条数不合法，应小于等于 300")
	private long pageSize = 10;

	public long currentPage() {
		return pageNum;
	}

	public long pageSize() {
		return pageSize;
	}

	public long offset() {
		return Math.max((this.pageNum - 1) * this.pageSize, 0L);
	}

	public <T> Page<T> mybatisPage() {
		return new Page<>(pageNum, pageSize);
	}

}
