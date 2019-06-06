package com.algaworks.algamoney.api.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustonTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();
		Map<String, Object> mapInfo = new HashMap<>();
		mapInfo.put("nome", usuarioSistema.getUsuario().getNome());
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(mapInfo);
		return accessToken;
	}

}
