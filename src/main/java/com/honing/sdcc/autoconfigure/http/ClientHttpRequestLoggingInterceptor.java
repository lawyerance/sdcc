package com.honing.sdcc.autoconfigure.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class ClientHttpRequestLoggingInterceptor implements ClientHttpRequestInterceptor {


	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		logRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		logResponse(response);

		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("--> {} {}", request.getMethod(), request.getURI());
			request.getHeaders().forEach((k, v) -> {
				logger.debug("--> {}: {}", k, String.join(";", v));
			});
			logger.debug("--> {}", new String(body, "UTF-8"));
		}
	}

	private void logResponse(ClientHttpResponse response) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("<-- {} {}", response.getStatusCode(), response.getStatusText());
			response.getHeaders().forEach((k, v) -> {
				logger.debug("<-- {}: {}", k, String.join(";", v));
			});
			logger.debug("<-- {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		}
	}
}
