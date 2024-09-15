package com.honing.sdcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * {
 * "grant_type": "client_credential",
 * "appid": "APPID",
 * "secret": "APPSECRET",
 * "force_refresh": false
 * }
 */
@Getter
@Setter
@JsonPropertyOrder(value = {"grantType"})
public class StableTokenDTO implements Serializable {
	@JsonProperty(value = "grant_type")
	private String grantType = "client_credential";

	private String appid;

	private String secret;

	@JsonProperty(value = "force_refresh")
	private boolean forceRefresh = false;
}
