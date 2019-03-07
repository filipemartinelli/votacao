package br.com.fmc.votacao.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Voto {

	@Id
	@SequenceGenerator(name="voto_seq", sequenceName="voto_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="voto_seq")
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotBlank(message = "voto-1")
	private String cpf;
	
	@NotNull(message = "voto-2")
	private Boolean escolha;
	
	@NotNull(message = "voto-3")
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
