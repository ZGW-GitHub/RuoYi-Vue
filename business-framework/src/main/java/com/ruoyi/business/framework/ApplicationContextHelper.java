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

package com.ruoyi.business.framework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Snow
 * @date 2023/5/23 16:50
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static Object getBean(String beanName) {
		return applicationContext != null ? applicationContext.getBean(beanName) : null;
	}

	public static <T> T getBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextHelper.applicationContext = applicationContext;
	}

}
