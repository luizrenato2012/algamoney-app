package com.algaworks.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Lancamento save(Lancamento lancamento) {
		Long codigoPessoa = lancamento.getPessoa().getCodigo();
		Pessoa pessoa = this.pessoaRepository.findOne(codigoPessoa);
		if (pessoa==null || pessoa.isAtivo().equals(false)) {
			throw new PessoaInexistenteOuInativaException ();
		}
		
		return this.lancamentoRepository.save(lancamento);
	}

	public Lancamento update(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoOriginal = buscaLancamentoExistente(codigo);
		if (lancamentoOriginal==null) {
			throw new IllegalArgumentException("Lancamento codigo "+ codigo + " nao encontrado!");
		}
		
		if(!lancamento.getPessoa().equals(lancamentoOriginal.getPessoa())) {
			validarPessoa(lancamento);
		}
		BeanUtils.copyProperties(lancamento, lancamentoOriginal, "codigo");
		lancamentoRepository.save(lancamentoOriginal);
		return lancamento;
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		if (lancamento.getPessoa().getCodigo()!=null) {
			pessoa=	this.pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}
		
		if (pessoa==null || pessoa.isInativa()) {
			throw new PessoaInexistenteOuInativaException ();
		}
	}

	private Lancamento buscaLancamentoExistente(Long codigo) {
		return this.lancamentoRepository.findOne(codigo);
	}

}
