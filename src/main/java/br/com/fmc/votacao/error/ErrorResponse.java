package br.com.fmc.votacao.error;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class ErrorResponse {
	
	private final int statusCode;
	
	private final List<ApiError> errors;
	
	static ErrorResponse of(HttpStatus status, List<ApiError> errors) {
		return new ErrorResponse(status.value(), errors);
	}
	
	static ErrorResponse of(HttpStatus status, ApiError error) {
		return new ErrorResponse(status.value(), Collections.singletonList(error));
	}
	
	
	@JsonAutoDetect(fieldVisibility = ANY)
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	static class ApiError{
		private final String code;
		private final String message;
	}

}
