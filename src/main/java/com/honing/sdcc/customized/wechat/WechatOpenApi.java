package com.honing.sdcc.customized.wechat;

public class WechatOpenApi {

	/**
	 * 接口调用凭证 /获取接口调用凭据
	 */
	private String accessToken;
	/**
	 * 接口调用凭证/获取稳定版接口调用凭据
	 */
	private String stableAccessToken;

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
