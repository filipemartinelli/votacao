package br.com.fmc.votacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmc.votacao.model.Voto;

public interface Votos extends JpaRepository<Voto, Long> {
	
	Optional<Voto> findByCpf(String cpf);
	
	Optional<List<Voto>> findByPautaId(Long id);
	
	Optional<Voto> findByCpfAndPautaId(String cpf, Long id);

}
