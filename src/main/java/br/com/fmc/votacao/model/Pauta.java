package br.com.fmc.votacao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pauta implements Serializable{

	private static final long serialVersionUID = 3077439391578014752L;

	@Id
	@SequenceGenerator(name = "pauta_seq", sequenceName = "pauta_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pauta_seq")
	@EqualsAndHashCode.Include
	private Long id;

	@NotBlank(message = "pauta-1")
	private String nome;


	@JsonIgnore
	public boolean isNew() {
		return getId() == null;
	}

	@JsonIgnore
	public boolean alreadyExist() {
		return getId() != null;
	}

}
