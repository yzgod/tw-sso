package com.tongwei.sso.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * @author		yangz
 * @date		2018年1月30日 上午11:47:32
 * @description	
 */
@Configuration
public class JSONConfig {
	
	/**
	 * fastjson支持
	 */
	@Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
       FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
       FastJsonConfig fastJsonConfig = new FastJsonConfig();
       fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
       List<MediaType> fastMediaTypes = new ArrayList<>();
       fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//       fastMediaTypes.add(new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8")));
       fastConverter.setSupportedMediaTypes(fastMediaTypes);
       fastConverter.setFastJsonConfig(fastJsonConfig);
       HttpMessageConverter<?> converter = fastConverter;
       return new HttpMessageConverters(converter);
    }

}
