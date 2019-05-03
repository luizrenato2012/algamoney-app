package com.algaworks.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public void delete(Long codigo) {
		this.pessoaRepository.delete(codigo);
	}
	
	public Pessoa save(Pessoa pessoa ) {
		return this.pessoaRepository.save(pessoa);
	}
	
	public Pessoa update(Long codigo, Pessoa pessoa) {
		Pessoa pessoaOriginal =this.buscaPorCodigo(codigo);
		
		BeanUtils.copyProperties(pessoa, pessoaOriginal, "codigo");
		return pessoaRepository.save(pessoa);	
	}

	public Pessoa atualizaPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaOriginal = buscaPorCodigo(codigo);
		
		pessoaOriginal.setAtivo(ativo);
		return pessoaRepository.save(pessoaOriginal);
		
	}
	
	public Pessoa buscaPorCodigo(Long codigo) {
		Pessoa pessoa = this.pessoaRepository.findOne(codigo);
		if (pessoa==null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoa;
	}
}
