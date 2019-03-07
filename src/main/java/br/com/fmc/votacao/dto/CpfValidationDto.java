package br.com.fmc.votacao.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CpfValidationDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 633031857370234293L;
	
	private String status;
}
