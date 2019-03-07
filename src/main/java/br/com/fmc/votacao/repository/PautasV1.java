package br.com.fmc.votacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmc.votacao.model.PautaV1;

public interface PautasV1 extends JpaRepository<PautaV1, Long> {
	
	Optional<PautaV1> findByNome(String nome);

}
