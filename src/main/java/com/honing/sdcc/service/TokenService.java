package com.honing.sdcc.service;

import com.honing.sdcc.dto.StableTokenDTO;
import org.springframework.lang.Nullable;

import java.util.Map;

public interface TokenService {
	Map<String, Object> getToken(@Nullable String grantType, String appid, String secret);

	Map<String, Object>  getStableToken(StableTokenDTO param);
}
