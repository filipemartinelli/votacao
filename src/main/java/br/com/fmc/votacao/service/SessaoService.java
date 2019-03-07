package br.com.fmc.votacao.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fmc.votacao.model.Sessao;
import br.com.fmc.votacao.repository.Sessoes;
import br.com.fmc.votacao.service.exception.SessaoNotFoundException;

@Service
public class SessaoService {

	private Sessoes sessoes;

	@Autowired
	public SessaoService(Sessoes sessoes) {
		this.sessoes = sessoes;
	}

	public Sessao save(final Sessao sessao) {
		if (sessao.getDataInicio() == null) {
			sessao.setDataInicio(LocalDateTime.now());
		}
		if (sessao.getMinutosValidade() == null) {
			sessao.setMinutosValidade(1l);
		}

		return sessoes.save(sessao);

	}

	public List<Sessao> findAll() {
		return sessoes.findAll();
	}

	public void delete(Sessao sessao) {
		Optional<Sessao> sessaoById = sessoes.findById(sessao.getId());
		if (!sessaoById.isPresent()) {
			throw new SessaoNotFoundException();
		}
		sessoes.delete(sessao);
	}

	public Sessao findById(Long id) {
		Optional<Sessao> findById = sessoes.findById(id);
		if(!findById.isPresent()){
			throw new SessaoNotFoundException();
		}
		return findById.get();
	}

	public Sessao findByIdAndPautaId(Long idSessao, Long pautaId) {
		Optional<Sessao> findByIdAndPautaId = sessoes.findByIdAndPautaId(idSessao, pautaId);
		if(!findByIdAndPautaId.isPresent()){
			throw new SessaoNotFoundException();
		}
		return findByIdAndPautaId.get();
	}

}
