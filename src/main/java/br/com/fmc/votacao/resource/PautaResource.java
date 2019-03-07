package br.com.fmc.votacao.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fmc.votacao.dto.VotacaoDto;
import br.com.fmc.votacao.model.PautaV1;
import br.com.fmc.votacao.model.PautaV2;
import br.com.fmc.votacao.model.Sessao;
import br.com.fmc.votacao.model.Voto;
import br.com.fmc.votacao.service.PautaService;
import br.com.fmc.votacao.service.SessaoService;
import br.com.fmc.votacao.service.VotacaoService;
import br.com.fmc.votacao.service.VotoService;
import br.com.fmc.votacao.service.exception.InvalidSessionException;

@RestController
public class PautaResource {

	@Autowired
	private PautaService pautaService;

	@Autowired
	private SessaoService sessaoService;

	@Autowired
	private VotoService votoService;

	@Autowired
	private VotacaoService service;

	@GetMapping("v1/pautas")
	public List<PautaV1> allV1() {
		return pautaService.findAll();
	}
	
	@GetMapping("v2/pautas")
	public List<PautaV2> allV2() {
		return pautaService.findAllV2();
	}

	@PostMapping("v1/pautas")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PautaV1 create(@Valid @RequestBody PautaV1 pauta) {
		return pautaService.save(pauta);
	}
	
	@PostMapping("v2/pautas")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PautaV2 create(@Valid @RequestBody PautaV2 pauta) {
		return pautaService.save(pauta);
	}

	@GetMapping("v1/pautas/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public PautaV1 findById(@PathVariable Long id) {
		return pautaService.findById(id);
	}

	@PostMapping("v1/pautas/{id}/sessoes")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Sessao createSession(@PathVariable Long id, @Valid @RequestBody Sessao sessao) {
		PautaV1 pauta = new PautaV1();
		pauta.setId(id);
		sessao.setPauta(pauta);
		return sessaoService.save(sessao);
	}

	@GetMapping("v1/pautas/sessoes/{idSessao}")
	@ResponseStatus(code = HttpStatus.OK)
	public Sessao findSessaoById(@PathVariable Long id) {
		return sessaoService.findById(id);
	}

	@GetMapping("v1/pautas/{id}/sessoes/{idSessao}")
	@ResponseStatus(code = HttpStatus.OK)
	public Sessao findSessaoById(@PathVariable Long id, @PathVariable Long idSessao) {
		return sessaoService.findByIdAndPautaId(idSessao, id);
	}

	@PostMapping("v1/pautas/{id}/sessoes/{idSessao}/votos")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Voto createVoto(@PathVariable Long id, @PathVariable Long idSessao, @RequestBody Voto voto) {
		Sessao sessao = sessaoService.findByIdAndPautaId(idSessao, id);
		if (!id.equals(sessao.getPauta().getId())) {
			throw new InvalidSessionException();
		}
		voto.setPauta(sessao.getPauta());
		return votoService.verifyAndSave(sessao, voto);
	}

	@GetMapping("v1/pautas/{id}/votacao")
	@ResponseStatus(code = HttpStatus.OK)
	public VotacaoDto findVotosByPautaId(@PathVariable Long id) {
		return service.buildVotacaoPauta(id);
	}

}
