package com.algaworks.algamoney.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class RecursoCriadoEvento extends ApplicationEvent {

	private static final long serialVersionUID = 3848975090158797776L;
	
	private Long codigo;
	private HttpServletResponse response;
	
	public RecursoCriadoEvento(Object source, Long codigo, HttpServletResponse response) {
		super(source);
		this.codigo=codigo;
		this.response=response;
	}

	public Long getCodigo() {
		return codigo;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

}
