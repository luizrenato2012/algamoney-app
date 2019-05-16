package com.algaworks.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
	
		HttpServletResponse httpResponse =  ((ServletServerHttpResponse)response).getServletResponse();
		HttpServletRequest httpRequest = ((ServletServerHttpRequest)request).getServletRequest();
		
		String refreshToken = body.getRefreshToken().getValue();
		adicionaTokenAoCookie(refreshToken, httpResponse, httpRequest);
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		retiraRefreshTokenDoBody(token);
		return token;
	}

	private void retiraRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);;
	}

	private void adicionaTokenAoCookie(String refreshToken, HttpServletResponse response, 
			HttpServletRequest request) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // TODO mudar pra true em producao
		cookie.setMaxAge(2592000);
		cookie.setPath(request.getContextPath()+"/oauth/token");
		response.addCookie(cookie);
	}

	
}
