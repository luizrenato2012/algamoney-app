package com.algaworks.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//pegar refresh token do cookie
		// adicionar nos parametros da requisicao
		String refreshToken = "";
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		if ("/oauth/token".equalsIgnoreCase(httpRequest.getRequestURI()) && 
				"refresh_token".equals(httpRequest.getParameter("grant_type")) &&
				httpRequest.getCookies()!=null) {
			Cookie[] cookies = httpRequest.getCookies();
			for(Cookie cookie : cookies) {
				if (cookie.getName().equals("refreshToken")) {
					refreshToken = cookie.getValue();
					httpRequest = new MyRequestWrapper(httpRequest, refreshToken);
				}
			}
		}
		chain.doFilter(httpRequest, response);
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}
	
	static class MyRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;
		
		public MyRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken=refreshToken;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> params = new ParameterMap<>(super.getRequest().getParameterMap());
			params.put("refresh_token", new String[] {this.refreshToken});
			params.setLocked(true);
			return params;
		}
		
	}

}
