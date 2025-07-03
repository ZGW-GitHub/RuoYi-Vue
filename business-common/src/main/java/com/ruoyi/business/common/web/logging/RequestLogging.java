package com.ruoyi.business.common.web.logging;

import java.lang.annotation.*;

/**
 * @author Snow
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestLogging {
}
