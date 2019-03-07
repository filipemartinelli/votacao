package br.com.fmc.votacao.dto;

import java.io.Serializable;

import br.com.fmc.votacao.model.PautaV1;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotacaoDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6641295645471857940L;
	
	private PautaV1 pauta;
	private Integer totalSim;
	private Integer totalNao;
	private Integer totalVotos;
	private Integer totalSessoes;
	
}
