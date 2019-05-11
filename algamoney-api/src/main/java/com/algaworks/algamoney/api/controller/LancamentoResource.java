package com.algaworks.algamoney.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoney.api.event.RecursoCriadoEvento;
import com.algaworks.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService service;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * @param lancamentoFilter - descricao, dataVencimentoDe, dataVencimentoAte
	 * @param pageable - size e page
	 * @return
	 */
	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) { 
		return this.lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> busca(@PathVariable Long codigo) {
		Lancamento lancamento = this.lancamentoRepository.findOne(codigo);
		return lancamento!=null ? 
				ResponseEntity.ok(lancamento) :
				ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> cria(@Valid @RequestBody Lancamento lancamento, 
			HttpServletResponse response) {
		Lancamento lancamentoSalvo = this.service.save(lancamento);
		publisher.publishEvent(new RecursoCriadoEvento(this, lancamentoSalvo.getCodigo(), response));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}
	
	@ExceptionHandler(PessoaInexistenteOuInativaException.class)
	public ResponseEntity<Object> handlePessoaInexistenteOuInativa(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa-inexistente-ou-inativa",null, 
				LocaleContextHolder.getLocale());
		String mensageDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensageDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void exclui(@PathVariable("codigo") Long codigo) {
		this.lancamentoRepository.delete(codigo);
	}

}
