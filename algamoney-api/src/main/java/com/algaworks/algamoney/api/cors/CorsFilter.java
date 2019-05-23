package com.algaworks.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final String ORIGEM_PERMITIDA="localhost:8000";
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
	
		res.setHeader("Access-Controll-Allow-Origin", ORIGEM_PERMITIDA);
		res.setHeader("Access-Controll-Allow-Credentials", "true"); 
		
		if (req.getMethod().equals("OPTIONS") && ORIGEM_PERMITIDA.equals(req.getHeader("Origin"))) {
			res.setHeader("Access-Controll-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
			res.setHeader("Access-Controll-Allow-Headers", "Authorization, Content-Type, Accept");
			res.setHeader("Access-Controll-Allow-Max-Age", "3600");
			
			res.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}

}
