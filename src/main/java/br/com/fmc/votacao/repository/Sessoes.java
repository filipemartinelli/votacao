package br.com.fmc.votacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmc.votacao.model.Sessao;

public interface Sessoes extends JpaRepository<Sessao, Long> {
	
	Optional<Sessao> findByIdAndPautaId(Long id, Long pautaId);

	Long countByPautaId(Long id);

}
