package com.honing.sdcc.autoconfigure.http;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		restTemplateBuilder.additionalMessageConverters(new SourceHttpMessageConverter<>());
		restTemplateBuilder.additionalInterceptors(Collections.singleton(new ClientHttpRequestLoggingInterceptor()));
		RestTemplate restTemplate = restTemplateBuilder.build();
		// 解决spring6.x针对json类型入参，不再设置Content-Length问题
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
		return restTemplate;
	}
}
