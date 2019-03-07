package br.com.fmc.votacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmc.votacao.model.PautaV2;

public interface PautasV2 extends JpaRepository<PautaV2, Long> {
	
	Optional<PautaV2> findByNome(String nome);

}
