package br.com.fmc.votacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fmc.votacao.model.PautaV1;
import br.com.fmc.votacao.model.PautaV2;
import br.com.fmc.votacao.repository.PautasV1;
import br.com.fmc.votacao.repository.PautasV2;
import br.com.fmc.votacao.service.exception.PautaNotFoundException;

@Service
public class PautaService {

	private PautasV1 pautas;
	private PautasV2 pautasV2;
	

	@Autowired
	public PautaService(PautasV1 pautas, PautasV2 pautasV2) {
		this.pautas = pautas;
		this.pautasV2 = pautasV2;
	}

	public PautaV1 save(final PautaV1 pauta) {
		return pautas.save(pauta);
	}
	
	public PautaV2 save(final PautaV2 pauta) {
		return pautasV2.save(pauta);
	}

	public List<PautaV1> findAll() {
		return pautas.findAll();
	}
	
	public List<PautaV2> findAllV2() {
		return pautasV2.findAll();
	}

	public void delete(PautaV1 pauta) {
		Optional<PautaV1> pautaById = pautas.findById(pauta.getId());
		if (!pautaById.isPresent()) {
			throw new PautaNotFoundException();
		}
		pautas.delete(pauta);
	}

	public PautaV1 findById(Long id) {
		Optional<PautaV1> findById = pautas.findById(id);
		if(!findById.isPresent()){
			throw new PautaNotFoundException();
		}
		return findById.get();
	}

}
