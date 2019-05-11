package com.algaworks.algamoney.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoney.api.event.RecursoCriadoEvento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	@GetMapping
	public List<Pessoa> listaTodas() {
		return this.pessoaRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> busca(@PathVariable("codigo") Long codigo) {
		Pessoa pessoa = this.pessoaRepository.findOne(codigo);
		return pessoa!=null ? 
				ResponseEntity.ok(pessoa): 
				ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> cria( @Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = this.pessoaRepository.save(pessoa);
		
		publisher.publishEvent(new RecursoCriadoEvento(this, pessoaSalva.getCodigo(), response));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> delete (@PathVariable("codigo") Long codigo) {
		this.pessoaService.delete(codigo);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<?> atualiza(@PathVariable("codigo") Long codigo, 
			@RequestBody @Valid Pessoa pessoa) {
		Pessoa pessoaAlterada = this.pessoaService.update(codigo, pessoa);
		return ResponseEntity.ok(pessoaAlterada);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> atualizaPropriedadeAtivo(@PathVariable ("codigo") Long codigo, @RequestBody Boolean ativo) {
		Pessoa pessoa = pessoaService.atualizaPropriedadeAtivo(codigo,ativo);
		return ResponseEntity.ok(pessoa);
	}
	

}
