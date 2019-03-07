package br.com.fmc.votacao.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

public class VotoServiceTest {

	private VotoService votoService;
	@Mock
	private Votos votos;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private Sender sender;
	@Mock
	private VotacaoService votacaoService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		votoService = new VotoService(votos, restTemplate, sender, votacaoService);
		votoService.setUrlCpfValidator("");
	}

	@Test(expected = SessaoTimeOutException.class)
	public void verifyVotoTest() {
		Sessao sessao = new Sessao();
		sessao.setDataInicio(LocalDateTime.now());
		sessao.setMinutosValidade(-1l);

		Voto voto = new Voto();
		PautaV1 pauta = new PautaV1();
		pauta.setId(1l);
		voto.setPauta(pauta);

		when(votacaoService.buildVotacaoPauta(anyLong())).thenReturn(VotacaoDto.builder().build());

		votoService.verifyVoto(sessao, voto);
	}

	@Test(expected = InvalidCpfException.class)
	public void cpfAbleToVoteTest() {
		Voto voto = new Voto();
		voto.setCpf("1234");

		CpfValidationDto cpf = new CpfValidationDto();
		cpf.setStatus("TESTE");

		ResponseEntity<CpfValidationDto> response = new ResponseEntity<CpfValidationDto>(cpf, HttpStatus.NOT_FOUND);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CpfValidationDto.class)))
				.thenReturn(response);

		votoService.cpfAbleToVote(voto);
	}

	@Test(expected = UnableCpfException.class)
	public void cpfAbleToVote2Test() {
		Voto voto = new Voto();
		voto.setCpf("1234");

		CpfValidationDto cpf = new CpfValidationDto();
		cpf.setStatus("UNABLE_TO_VOTE");

		ResponseEntity<CpfValidationDto> response = new ResponseEntity<CpfValidationDto>(cpf, HttpStatus.OK);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CpfValidationDto.class)))
				.thenReturn(response);

		votoService.cpfAbleToVote(voto);
	}

	@Test
	public void cpfAbleToVote3Test() {
		Voto voto = new Voto();
		voto.setCpf("1234");

		CpfValidationDto cpf = new CpfValidationDto();
		cpf.setStatus("ABLE_TO_VOTE");

		ResponseEntity<CpfValidationDto> response = new ResponseEntity<CpfValidationDto>(cpf, HttpStatus.OK);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CpfValidationDto.class)))
				.thenReturn(response);

		votoService.cpfAbleToVote(voto);
	}

	@Test(expected = VotoAlreadyExistsException.class)
	public void votoAlreadyExistsTest() {
		Voto voto = new Voto();
		voto.setCpf("1234");
		PautaV1 pauta = new PautaV1();
		pauta.setId(1l);
		voto.setPauta(pauta );
		when(votos.findByCpfAndPautaId(anyString(), anyLong())).thenReturn(Optional.of(new Voto()));
		votoService.votoAlreadyExists(voto);
	}
	
	@Test
	public void votoAlreadyExistssTest() {
		Voto voto = new Voto();
		voto.setCpf("1234");
		PautaV1 pauta = new PautaV1();
		pauta.setId(1l);
		voto.setPauta(pauta );
		
		when(votos.findByCpfAndPautaId(anyString(), anyLong())).thenReturn(Optional.ofNullable(null));
		votoService.votoAlreadyExists(voto);
	}

}
