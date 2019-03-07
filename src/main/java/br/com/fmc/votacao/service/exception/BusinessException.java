package br.com.fmc.votacao.service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 2329295674665615129L;
	
	private final String code;
	private final HttpStatus status;
	
}
