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

package com.ruoyi.business.framework.web.component.concurrent;

import com.ruoyi.business.framework.web.request.RequestContext;
import com.ruoyi.business.framework.web.request.RequestContextHelper;
import com.ruoyi.business.framework.web.util.MDCUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Snow
 */
@Slf4j
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

	public TraceThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public TraceThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public TraceThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public TraceThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	@Override
	public void execute(@NonNull final Runnable runnable) {
		final RequestContext requestContext = RequestContextHelper.currentContext();

		Runnable runnableWrap = () -> {
			try {
				RequestContext ignore = requestContext == null
						? RequestContextHelper.startContext(MDCUtil.generateTraceId(), true)
						: RequestContextHelper.startChildContext(requestContext, true);

				runnable.run();
			} catch (Exception e) {
				log.error("Trace 线程池执行任务发生异常: {}", e.getMessage(), e);
				throw e;
			} finally {
				RequestContextHelper.clear(true);
			}
		};

		super.execute(runnableWrap);
	}

}
