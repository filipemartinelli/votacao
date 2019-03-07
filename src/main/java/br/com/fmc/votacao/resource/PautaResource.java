package br.com.fmc.votacao.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fmc.votacao.dto.VotacaoDto;
import br.com.fmc.votacao.model.Pauta;
import br.com.fmc.votacao.model.Sessao;
import br.com.fmc.votacao.model.Voto;
import br.com.fmc.votacao.service.PautaService;
import br.com.fmc.votacao.service.SessaoService;
import br.com.fmc.votacao.service.VotacaoService;
import br.com.fmc.votacao.service.VotoService;
import br.com.fmc.votacao.service.exception.InvalidSessionException;

@RestController
@RequestMapping("/v1/pautas")
public class PautaResource {

	@Autowired
	private PautaService pautaService;

	@Autowired
	private SessaoService sessaoService;

	@Autowired
	private VotoService votoService;

	@Autowired
	private VotacaoService service;

	@GetMapping
	public List<Pauta> all() {
		return pautaService.findAll();
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Pauta create(@Valid @RequestBody Pauta pauta) {
		return pautaService.save(pauta);
	}

	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Pauta findById(@PathVariable Long id) {
		return pautaService.findById(id);
	}

	@PostMapping("/{id}/sessoes")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Sessao createSession(@PathVariable Long id, @Valid @RequestBody Sessao sessao) {
		Pauta pauta = new Pauta();
		pauta.setId(id);
		sessao.setPauta(pauta);
		return sessaoService.save(sessao);
	}

	@GetMapping("/sessoes/{idSessao}")
	@ResponseStatus(code = HttpStatus.OK)
	public Sessao findSessaoById(@PathVariable Long id) {
		return sessaoService.findById(id);
	}

	@GetMapping("/{id}/sessoes/{idSessao}")
	@ResponseStatus(code = HttpStatus.OK)
	public Sessao findSessaoById(@PathVariable Long id, @PathVariable Long idSessao) {
		return sessaoService.findByIdAndPautaId(idSessao, id);
	}

	@PostMapping("/{id}/sessoes/{idSessao}/votos")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Voto createVoto(@PathVariable Long id, @PathVariable Long idSessao, @RequestBody Voto voto) {
		Sessao sessao = sessaoService.findByIdAndPautaId(idSessao, id);
		if (!id.equals(sessao.getPauta().getId())) {
			throw new InvalidSessionException();
		}
		voto.setPauta(sessao.getPauta());
		return votoService.verifyAndSave(sessao, voto);
	}

	@GetMapping("/{id}/votacao")
	@ResponseStatus(code = HttpStatus.OK)
	public VotacaoDto findVotosByPautaId(@PathVariable Long id) {
		return service.buildVotacaoPauta(id);
	}

}
