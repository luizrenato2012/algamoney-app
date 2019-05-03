package com.algaworks.algamoney.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository repository;
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> busca(@PathVariable Long codigo) {
		Lancamento lancamento = this.repository.findOne(codigo);
		return lancamento!=null ? 
				ResponseEntity.ok(lancamento) :
				ResponseEntity.notFound().build();
	}
	
	@GetMapping
	@ResponseStatus(org.springframework.http.HttpStatus.OK)
	public List<Lancamento> listaTodos() {
		return this.repository.findAll();
	}

}
