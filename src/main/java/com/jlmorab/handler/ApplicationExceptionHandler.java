package com.jlmorab.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.jlmorab.data.dto.MetaDTO;
import com.jlmorab.data.dto.WebResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {
	
	@ExceptionHandler( ResponseStatusException.class )
	public ResponseEntity<WebResponseDTO> handleResponseStatusException( ResponseStatusException exception ) {
		return executeFlow(
				HttpStatus.valueOf( exception.getStatusCode().value() ) ,
				exception.getReason() );
	}//end handleResponseStatusException()
	
	@ExceptionHandler( { MethodArgumentNotValidException.class, IllegalArgumentException.class, NullPointerException.class } )
	public ResponseEntity<WebResponseDTO> handleMethodArgumentNotValid( Exception exception ) {
		return customArgumentResponse( exception );
	}//end handleMethodArgumentNotValid()
	
	@ExceptionHandler( HttpRequestMethodNotSupportedException.class )
	public ResponseEntity<WebResponseDTO> handleResponseStatusException( HttpRequestMethodNotSupportedException exception ) {
		return executeFlow(
				HttpStatus.METHOD_NOT_ALLOWED ,
				exception.getMessage() );
	}//end handleResponseStatusException()
	
	@ExceptionHandler( HttpMessageNotReadableException.class )
	public ResponseEntity<WebResponseDTO> handleHttpMessageNotReadableException( HttpMessageNotReadableException exception ) {
		return executeFlow(
				HttpStatus.BAD_REQUEST ,
				exception.getMessage() );
	}//end handleHttpMessageNotReadableException()
	
	@ExceptionHandler( Exception.class )
	public ResponseEntity<WebResponseDTO> handleGenericException( Exception exception ) {
		return executeFlow(
				HttpStatus.INTERNAL_SERVER_ERROR,
				exception.getMessage()
				);
	}//end handleGenericException()
	

	private ResponseEntity<WebResponseDTO> customArgumentResponse( Exception exception ) {
		
		String message = "";
		if( exception instanceof MethodArgumentNotValidException ex ) {
			Map<String, String> detail = new HashMap<>();
			ex.getFieldErrors().forEach( error -> detail.put( error.getField(), error.getDefaultMessage() ) );
			message = ex.getFieldErrors().stream()
					.map( FieldError::getDefaultMessage )
					.collect( Collectors.joining("|") );
		} else if( exception instanceof IllegalArgumentException ex ) {
			message= ex.getMessage();
		}//end if
		
		return executeFlow(
				HttpStatus.BAD_REQUEST,
				String.format( "ArgumentException: %s", message ) );
		
	}//end customArgumentResponse()
	
	private WebResponseDTO buildResponse( HttpStatus httpStatus, String message ) {
		
		MetaDTO meta = new MetaDTO( httpStatus );
		meta.addMessage( message );
		return WebResponseDTO.builder()
			.meta( meta )
			.build();
		
	}//end buildResponse()
	
	private ResponseEntity<WebResponseDTO> executeFlow( HttpStatus httpStatus, String message ) {
		WebResponseDTO response = buildResponse( httpStatus, message );
		log.error(message);
		return new ResponseEntity<>( response, httpStatus );
	}//end executeFlow()
	
}
