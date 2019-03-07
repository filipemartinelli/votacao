package br.com.fmc.votacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fmc.votacao.model.Pauta;
import br.com.fmc.votacao.repository.Pautas;
import br.com.fmc.votacao.service.exception.PautaNotFoundException;

@Service
public class PautaService {

	private Pautas pautas;

	@Autowired
	public PautaService(Pautas pautas) {
		this.pautas = pautas;
	}

	public Pauta save(final Pauta pauta) {
		return pautas.save(pauta);
	}

	public List<Pauta> findAll() {
		return pautas.findAll();
	}

	public void delete(Pauta pauta) {
		Optional<Pauta> pautaById = pautas.findById(pauta.getId());
		if (!pautaById.isPresent()) {
			throw new PautaNotFoundException();
		}
		pautas.delete(pauta);
	}

	public Pauta findById(Long id) {
		Optional<Pauta> findById = pautas.findById(id);
		if(!findById.isPresent()){
			throw new PautaNotFoundException();
		}
		return findById.get();
	}

}
