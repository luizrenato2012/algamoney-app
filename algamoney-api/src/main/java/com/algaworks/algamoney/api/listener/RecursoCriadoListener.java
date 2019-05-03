package com.algaworks.algamoney.api.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algamoney.api.event.RecursoCriadoEvento;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvento> {

	@Override
	public void onApplicationEvent(RecursoCriadoEvento evento) {
		Long codigo = evento.getCodigo();
		HttpServletResponse response = evento.getResponse();
		
		adicionaHeader(response, codigo);
	}

	private void adicionaHeader(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder
			.fromCurrentRequestUri()
			.path("/{codigo}")
			.buildAndExpand(codigo).toUri();
		response.setHeader("Localtion", uri.toString());
	}

}
