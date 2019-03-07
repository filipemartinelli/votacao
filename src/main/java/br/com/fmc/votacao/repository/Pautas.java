package br.com.fmc.votacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmc.votacao.model.Pauta;

public interface Pautas extends JpaRepository<Pauta, Long> {
	
	Optional<Pauta> findByNome(String nome);

}
