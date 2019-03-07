package br.com.fmc.votacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fmc.votacao.dto.VotacaoDto;
import br.com.fmc.votacao.model.PautaV1;
import br.com.fmc.votacao.model.Voto;
import br.com.fmc.votacao.repository.Sessoes;
import br.com.fmc.votacao.repository.Votos;
import br.com.fmc.votacao.service.exception.BusinessException;
import br.com.fmc.votacao.service.exception.VotacaoNotFoundException;
import br.com.fmc.votacao.service.exception.VotoNotFoundException;

@Service
public class VotacaoService {

	private Votos votos;
	private Sessoes sessoes;
	
	
	@Autowired
	public VotacaoService( Votos votos, Sessoes sessoes) {
		this.votos = votos;
		this.sessoes = sessoes;
	}

	public Voto save(final Voto voto) {
		verifyIfExists(voto);
		return votos.save(voto);
	}

	private void verifyIfExists(final Voto voto) {
		Optional<Voto> votoByCpfAndPauta = votos.findByCpf(voto.getCpf());

		if (votoByCpfAndPauta.isPresent() && (voto.isNew() || isUpdatingToADifferent(voto, votoByCpfAndPauta))) {
			throw new BusinessException(null, null);
		}
	}

	private boolean isUpdatingToADifferent(Voto voto, Optional<Voto> votoByCpfAndPauta) {
		return voto.alreadyExist() && !votoByCpfAndPauta.get().equals(voto);
	}

	public List<Voto> findAll() {
		return votos.findAll();
	}

	public void delete(Voto voto) {
		Optional<Voto> votoById = votos.findById(voto.getId());
		if (!votoById.isPresent()) {
			throw new VotoNotFoundException();
		}
		votos.delete(voto);
	}

	public List<Voto> findVotosByPautaId(Long id) {
		Optional<List<Voto>> findByPautaId = votos.findByPautaId(id);

		if (!findByPautaId.isPresent()) {
			throw new VotoNotFoundException();
		}

		return findByPautaId.get();
	}

	public VotacaoDto buildVotacaoPauta(Long id) {
		Optional<List<Voto>> votosByPauta = votos.findByPautaId(id);
		if (!votosByPauta.isPresent() || votosByPauta.get().isEmpty()) {
			throw new VotacaoNotFoundException();
		}
		
		PautaV1 pauta = votosByPauta.get().iterator().next().getPauta();
		
		Long totalSessoes = sessoes.countByPautaId(pauta.getId());
		
		
		Integer total = votosByPauta.get().size();

		Integer totalSim = (int) votosByPauta.get().stream().filter(voto -> Boolean.TRUE.equals(voto.getEscolha()))
				.count();

		Integer totalNao = total - totalSim;

		return VotacaoDto.builder().pauta(pauta).totalVotos(total).totalSessoes(totalSessoes.intValue())
				.totalSim(totalSim).totalNao(totalNao).build();

	}

}
