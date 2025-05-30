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
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Snow
 * @date 2024/2/28
 */
@Slf4j
public class TraceScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	public TraceScheduledThreadPoolExecutor(final int corePoolSize) {
		super(corePoolSize);
	}

	public TraceScheduledThreadPoolExecutor(final int corePoolSize, final ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
	}

	public TraceScheduledThreadPoolExecutor(final int corePoolSize, final RejectedExecutionHandler handler) {
		super(corePoolSize, handler);
	}

	public TraceScheduledThreadPoolExecutor(final int corePoolSize, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
	}

	@Override
	public @Nonnull ScheduledFuture<?> schedule(@Nonnull Runnable runnable, long delay, @Nonnull TimeUnit unit) {
		if (!RequestContextHelper.hasContext()) {
			return super.schedule(runnable, delay, unit);
		}

		final RequestContext requestContext = RequestContextHelper.currentContext();

		Runnable runnableWrap = () -> {
			try {
				RequestContext ignore = requestContext != null
						? RequestContextHelper.startChildContext(requestContext, true)
						: RequestContextHelper.startContext(MDCUtil.generateTraceId(), true);

				runnable.run();
			} finally {
				RequestContextHelper.clear(true);
			}
		};
		return super.schedule(runnableWrap, delay, unit);
	}

	@Override
	public @Nonnull ScheduledFuture<?> scheduleAtFixedRate(@Nonnull Runnable runnable, long initialDelay, long period, @Nonnull TimeUnit unit) {
		if (!RequestContextHelper.hasContext()) {
			return super.scheduleAtFixedRate(runnable, initialDelay, period, unit);
		}

		final RequestContext requestContext = RequestContextHelper.currentContext();

		Runnable runnableWrap = () -> {
			try {
				RequestContext ignore = requestContext != null
						? RequestContextHelper.startChildContext(requestContext, true)
						: RequestContextHelper.startContext(MDCUtil.generateTraceId(), true);

				runnable.run();
			} finally {
				RequestContextHelper.clear(true);
			}
		};
		return super.scheduleAtFixedRate(runnableWrap, initialDelay, period, unit);
	}

	@Override
	public @Nonnull ScheduledFuture<?> scheduleWithFixedDelay(@Nonnull Runnable runnable, long initialDelay, long delay, @Nonnull TimeUnit unit) {
		if (!RequestContextHelper.hasContext()) {
			return super.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
		}

		final RequestContext requestContext = RequestContextHelper.currentContext();

		Runnable runnableWrap = () -> {
			try {
				RequestContext ignore = requestContext != null
						? RequestContextHelper.startChildContext(requestContext, true)
						: RequestContextHelper.startContext(MDCUtil.generateTraceId(), true);

				runnable.run();
			} finally {
				RequestContextHelper.clear(true);
			}
		};
		return super.scheduleWithFixedDelay(runnableWrap, initialDelay, delay, unit);
	}

	@Override
	public @Nonnull <V> ScheduledFuture<V> schedule(@Nonnull Callable<V> callable, long delay, @Nonnull TimeUnit unit) {
		if (!RequestContextHelper.hasContext()) {
			return super.schedule(callable, delay, unit);
		}

		final RequestContext requestContext = RequestContextHelper.currentContext();

		Callable<V> callableWrap = () -> {
			try {
				RequestContext ignore = requestContext != null
						? RequestContextHelper.startChildContext(requestContext, true)
						: RequestContextHelper.startContext(MDCUtil.generateTraceId(), true);

				return callable.call();
			} finally {
				RequestContextHelper.clear(true);
			}
		};
		return super.schedule(callableWrap, delay, unit);
	}

}
