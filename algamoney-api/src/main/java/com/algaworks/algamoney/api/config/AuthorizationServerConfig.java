package com.algaworks.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.algaworks.algamoney.api.security.CustonTokenEnhancer;


@Profile("oauth2-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		int VALIDADE_ACCESS_TOKEN= 60 * 5; 
		int VALIDADE_REFRESH_TOKEN= 60*60*24; 
		clients.inMemory()
			.withClient("angular").secret("@angul@r0")
				.scopes("read","write")
				.authorizedGrantTypes("password","refresh_token")	// password flow e refresh token flow
				.accessTokenValiditySeconds(VALIDADE_ACCESS_TOKEN)
				.refreshTokenValiditySeconds(VALIDADE_REFRESH_TOKEN)
				.and()
			.withClient("mobile").secret("m0b1l4")
			.scopes("read")
			.authorizedGrantTypes("password","refresh_token")	// password flow e refresh token flow
			.accessTokenValiditySeconds(VALIDADE_ACCESS_TOKEN)
			.refreshTokenValiditySeconds(VALIDADE_REFRESH_TOKEN);
			
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), criaJwtConverter() ));
		
		endpoints
			.tokenStore(getTokenStore())
			.tokenEnhancer(enhancerChain)
			.accessTokenConverter(this.criaJwtConverter())
			.reuseRefreshTokens(false)
			.authenticationManager(this.authenticationManager);
	}


	@Bean
	public TokenStore getTokenStore() {
		return new JwtTokenStore(criaJwtConverter());
	}

	@Bean
	public JwtAccessTokenConverter criaJwtConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("mySignner");
		return converter;
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustonTokenEnhancer();
	}
	
}
