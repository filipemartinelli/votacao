package br.com.fmc.votacao.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.fmc.votacao.dto.CpfValidationDto;
import br.com.fmc.votacao.dto.VotacaoDto;
import br.com.fmc.votacao.model.PautaV1;
import br.com.fmc.votacao.model.Sessao;
import br.com.fmc.votacao.model.Voto;
import br.com.fmc.votacao.mq.Sender;
import br.com.fmc.votacao.repository.Votos;
import br.com.fmc.votacao.service.exception.InvalidCpfException;
import br.com.fmc.votacao.service.exception.SessaoTimeOutException;
import br.com.fmc.votacao.service.exception.UnableCpfException;
import br.com.fmc.votacao.service.exception.VotoAlreadyExistsException;
import br.com.fmc.votacao.service.exception.VotoNotFoundException;

@Service
public class VotoService {

	private static final String CPF_UNABLE_TO_VOTE = "UNABLE_TO_VOTE";

	@Value("${app.integracao.cpf.url}")
	private String urlCpfValidator;

	private Votos votos;

	private RestTemplate restTemplate;

	private Sender sender;

	private VotacaoService votacaoService;

	@Autowired
	public VotoService(Votos votos, RestTemplate restTemplate, Sender sender, VotacaoService votacaoService) {
		this.votos = votos;
		this.restTemplate = restTemplate;
		this.sender = sender;
		this.votacaoService = votacaoService;
	}

	public Voto verifyAndSave(final Sessao sessao, final Voto voto) {
		verifyVoto(sessao, voto);
		return votos.save(voto);
	}

	protected void verifyVoto(final Sessao sessao, final Voto voto) {

		LocalDateTime dataLimite = sessao.getDataInicio().plusMinutes(sessao.getMinutosValidade());
		if (LocalDateTime.now().isAfter(dataLimite)) {
			sendMessage(voto.getPauta());
			throw new SessaoTimeOutException();
		}

		cpfAbleToVote(voto);
		votoAlreadyExists(voto);
	}

	protected void votoAlreadyExists(final Voto voto) {
		Optional<Voto> votoByCpfAndPauta = votos.findByCpfAndPautaId(voto.getCpf(), voto.getPauta().getId());

		if (votoByCpfAndPauta.isPresent()) {
			throw new VotoAlreadyExistsException();
		}
	}

	private void sendMessage(PautaV1 pauta) {
		VotacaoDto votacaoPauta = votacaoService.buildVotacaoPauta(pauta.getId());
		sender.send(votacaoPauta);
	}

	protected void cpfAbleToVote(final Voto voto) {
		ResponseEntity<CpfValidationDto> cpfValidation = getCpfValidation(voto);
		if (HttpStatus.OK.equals(cpfValidation.getStatusCode())) {
			if (CPF_UNABLE_TO_VOTE.equalsIgnoreCase(cpfValidation.getBody().getStatus())) {
				throw new UnableCpfException();
			}
		} else {
			throw new InvalidCpfException();
		}
	}

	protected ResponseEntity<CpfValidationDto> getCpfValidation(final Voto voto) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(urlCpfValidator.concat("/").concat(voto.getCpf()), HttpMethod.GET, entity,
				CpfValidationDto.class);
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

	public void setUrlCpfValidator(String urlCpfValidator) {
		this.urlCpfValidator = urlCpfValidator;
	}

}
