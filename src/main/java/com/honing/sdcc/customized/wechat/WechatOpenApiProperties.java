package com.honing.sdcc.customized.wechat;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wechat")
public class WechatOpenApiProperties {
	private String baseUri = "https://api.weixin.qq.com";

	private final MiniProgram miniProgram = new MiniProgram();

	public static class MiniProgram {
		/**
		 * 接口调用凭证 /获取接口调用凭据
		 */
		private String accessToken = "/cgi-bin/token?grant_type={grantType}&appid={appid}&secret={secret}";
		/**
		 * 接口调用凭证/获取稳定版接口调用凭据
		 */
		private String stableAccessToken = "/cgi-bin/stable_token";

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getStableAccessToken() {
			return stableAccessToken;
		}

		public void setStableAccessToken(String stableAccessToken) {
			this.stableAccessToken = stableAccessToken;
		}
	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public MiniProgram getMiniProgram() {
		return miniProgram;
	}
}
