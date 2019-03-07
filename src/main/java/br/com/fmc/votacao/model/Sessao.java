package br.com.fmc.votacao.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sessao {

	@Id
	@SequenceGenerator(name = "sessao_seq", sequenceName = "sessao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessao_seq")
	@EqualsAndHashCode.Include
	private Long id;

	private LocalDateTime dataInicio;

	private Long minutosValidade;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Pauta pauta;


	@JsonIgnore
	public boolean isNew() {
		return getId() == null;
	}

	@JsonIgnore
	public boolean alreadyExist() {
		return getId() != null;
	}

}
