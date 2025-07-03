package com.ruoyi.business.common.web.configuration;

import com.ruoyi.business.common.web.controller.advice.CustomResponseBodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Snow
 */
@Slf4j
@Order(1)
@Configuration(proxyBeanMethods = false)
public class CustomWebMvcConfiguration implements WebMvcConfigurer {

	/**
	 * 配置消息转换器<br/><br/>
	 * 先删除 {@link StringHttpMessageConverter} 再添加，是为了让 {@link StringHttpMessageConverter} 排在 {@link MappingJackson2HttpMessageConverter} 后面。<br/>
	 * <ol>
	 * <li>删除 {@link StringHttpMessageConverter} 的原因：<a href="https://blog.csdn.net/weixin_48640132/article/details/125370413">参考文章</a></li>。
	 * <li>为何不直接删除 {@link StringHttpMessageConverter} 就行了：因为有些内置接口（例如：/actuator/prometheus）的响应不会被 {@link CustomResponseBodyHandler} 处理，此时就需要这些 {@link StringHttpMessageConverter} 来处理响应了。</li>
	 * </ol>
	 *
	 * @param converters 转换器
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		List<HttpMessageConverter<?>> stringHttpMessageConverterList = converters.stream()
				.filter(converter -> StringHttpMessageConverter.class.isAssignableFrom(converter.getClass())).toList();

		converters.removeAll(stringHttpMessageConverterList);
		converters.addAll(stringHttpMessageConverterList);
	}

}
