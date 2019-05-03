package com.algaworks.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida",null, LocaleContextHolder.getLocale());
		String mensageDesenvolvedor = ex.getCause().getMessage();
		
		return this.handleExceptionInternal(ex, Arrays.asList(new Erro(mensagemUsuario, mensageDesenvolvedor)) , 
				headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> listaErros = this.criaListaErros(ex.getBindingResult());
		ex.printStackTrace();
		
		return handleExceptionInternal(ex, listaErros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handlerEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado",null, 
				LocaleContextHolder.getLocale());
		String mensageDesenvolvedor = ex.getMessage();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensageDesenvolvedor));
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	private List<Erro> criaListaErros (BindingResult bindingResult) {
		List<Erro> listaErros = new ArrayList<>();
		String msgUsuario = "";
		String msgDesenvolvedor="";
		
		for( FieldError fieldError :bindingResult.getFieldErrors()) {
			msgUsuario = this.messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			msgDesenvolvedor = fieldError.toString();
			listaErros.add(new Erro(msgUsuario, msgDesenvolvedor));
		}
		return listaErros;
	}
	
	public static class Erro {
		String mensagemUsuario ;
		String mensageDesenvolvedor ;
		
		public Erro(String mensagemUsuario, String mensageDesenvolvedor) {
			super();
			this.mensagemUsuario = mensagemUsuario;
			this.mensageDesenvolvedor = mensageDesenvolvedor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}

		public String getMensageDesenvolvedor() {
			return mensageDesenvolvedor;
		}
		
	}
 
}