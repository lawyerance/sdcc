package com.honing.sdcc.customized.wechat;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(WechatOpenApiProperties.class)
@Configuration
public class WechatAutoconfiguration {
	@Bean
	public WechatOpenApi wechatOpenApi(WechatOpenApiProperties wechatOpenApiProperties) {
		String baseUri = wechatOpenApiProperties.getBaseUri();
		WechatOpenApiProperties.MiniProgram program = wechatOpenApiProperties.getMiniProgram();

		WechatOpenApi wechatOpenApi = new WechatOpenApi();
		wechatOpenApi.setAccessToken(baseUri + program.getAccessToken());
		wechatOpenApi.setStableAccessToken(baseUri + program.getStableAccessToken());
		return wechatOpenApi;
	}
}
