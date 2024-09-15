package com.honing.sdcc.service.impl;

import com.honing.sdcc.customized.wechat.WechatOpenApi;
import com.honing.sdcc.service.TokenService;
import com.honing.sdcc.dto.StableTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

	private final RestTemplate restTemplate;
	private final WechatOpenApi wechatOpenApi;

	public TokenServiceImpl(RestTemplate restTemplate, WechatOpenApi wechatOpenApi) {
		this.restTemplate = restTemplate;
		this.wechatOpenApi = wechatOpenApi;
	}

	@Override
	@Cacheable(cacheNames = {"public:wechat"}, key = "#appid", unless = "!#result.containsKey('access_token')")
	public Map<String, Object> getToken(String grantType, String appid, String secret) {
		Map<String, Object> uriVariables = new LinkedHashMap<>();
		uriVariables.put("grantType", grantType);
		uriVariables.put("appid", appid);
		uriVariables.put("secret", secret);

		logger.info("Cache is invalid, will using appid = {} to request -token url:  {}", appid,
			wechatOpenApi.getAccessToken());
		return doObtainToken(uriVariables);
	}

	@Override
	@Cacheable(cacheNames = {"public:wechat"}, key = "#param.appid", unless = "!#result.containsKey('access_token')")
	public Map<String, Object> getStableToken(StableTokenDTO param) {
		logger.info("Cache is invalid, will using appid = {} to request stable-token url:  {}", param.getAppid(),
			wechatOpenApi.getStableAccessToken());
		return doObtainStableToken(param);
	}


	@Retryable(maxAttempts = 5, label = "Retry token restful.", retryFor = {TimeoutException.class,
		InterruptedException.class})
	public Map<String, Object> doObtainToken(Map<String, Object> uriVariables) {
		String accessToken = wechatOpenApi.getAccessToken();
		ResponseEntity<Map<String, Object>> exchange = restTemplate.exchange(accessToken, HttpMethod.GET, null,
			new ParameterizedTypeReference<>() {
			}, uriVariables);
		return exchange.getBody();
	}

	@Retryable(maxAttempts = 5, label = "Retry token restful.", retryFor = {TimeoutException.class,
		InterruptedException.class})
	public Map<String, Object> doObtainStableToken(StableTokenDTO param) {
		String stableAccessToken = wechatOpenApi.getStableAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Map<String, Object>> exchange = restTemplate.exchange(stableAccessToken, HttpMethod.POST,
			new HttpEntity<>(param, headers), new ParameterizedTypeReference<>() {
			});
		return exchange.getBody();
	}
}
