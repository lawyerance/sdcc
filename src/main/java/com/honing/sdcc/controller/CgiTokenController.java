package com.honing.sdcc.controller;


import com.honing.sdcc.dto.StableTokenDTO;
import com.honing.sdcc.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/cgi-bin")
@RequiredArgsConstructor
public class CgiTokenController {

	private final TokenService tokenService;

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getToken(@RequestParam(value = "grant_type", defaultValue =
		"client_credential", required = false) String grantType, @RequestParam(value = "appid") String appid,
														@RequestParam(value = "secret") String secret) {
		Map<String, Object> token = tokenService.getToken(grantType, appid, secret);
		return ResponseEntity.ok(token);
	}

	@RequestMapping(value = "/stable_token", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getStableToken(@RequestBody StableTokenDTO param) {
		Map<String, Object> token = tokenService.getStableToken(param);
		return ResponseEntity.ok(token);
	}
}
