package com.jlmorab.config;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.ALL_VALUE;

import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlmorab.data.dto.MetaDTO;
import com.jlmorab.data.dto.WebResponseDTO;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class OpenApiConfig {

	private void mergeApiResponses( ApiResponses responses, Map<String, ApiResponse> globalResponses ) {
		globalResponses.forEach((code, globalResponse) -> {
			if( responses.containsKey( code ) ) {
				ApiResponse existing = responses.get( code );
				existing.setDescription( existing.getDescription() + " | " + globalResponse.getDescription());
				if( existing.getContent().containsKey( ALL_VALUE ) ) {
					existing.getContent().remove( ALL_VALUE );
				}//end if
				globalResponse.getContent().forEach( (mediaType, media) -> {
					if( !existing.getContent().containsKey(mediaType) ) {
						existing.getContent().addMediaType(mediaType, media);
					}//end if
				});
			} else {
				responses.addApiResponse(code, globalResponse);
			}//end if
		});
	}
	
	@Bean
    OpenApiCustomizer globalResponseOpenApiCustomiser() {
		String BAD_REQUEST 		= "400";
		String NOT_FOUND 		= "404";
		String NOT_ALLOWED 		= "405";
		String INTERNAL_ERROR 	= "500";
		
		return openApi -> {
			Schema<?> webResponseSchema = new Schema<>().$ref("#/components/schemas/WebResponseDTO");

			Map<String, ApiResponse> globalResponses = Map.of(
					BAD_REQUEST, obtainOpenApiResponse( 
							webResponseSchema, HttpStatus.BAD_REQUEST, "Solicitud inválida" ),
					NOT_FOUND, obtainOpenApiResponse( 
							webResponseSchema, HttpStatus.NOT_FOUND, "Método no encontrado" ),
					NOT_ALLOWED, obtainOpenApiResponse( 
							webResponseSchema, HttpStatus.METHOD_NOT_ALLOWED, "Método no permitido" ),
					INTERNAL_ERROR, obtainOpenApiResponse( 
							webResponseSchema, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor" )
            );
			
			openApi.getPaths().forEach((path, pathItem) ->
            	pathItem.readOperations().forEach(operation -> {
            		ApiResponses responses = operation.getResponses();
            		mergeApiResponses( responses, globalResponses );
            	})
            );
		};
		
		/*
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
            pathItem.readOperations().forEach(operation -> {
                ApiResponses responses = operation.getResponses();
                
                Schema<?> webResponseSchema = new Schema<>().$ref("#/components/schemas/WebResponseDTO");

                ApiResponse globalBadRequestResponse = obtainOpenApiResponse( 
                		webResponseSchema, HttpStatus.BAD_REQUEST, "Solicitud inválida" );
                
                ApiResponse globalNotFoundResponse = obtainOpenApiResponse( 
                		webResponseSchema, HttpStatus.NOT_FOUND, "Método no encontrado" );
                
                ApiResponse globalNotAllowedResponse = obtainOpenApiResponse( 
                		webResponseSchema, HttpStatus.METHOD_NOT_ALLOWED, "Método no permitido" );
                
                ApiResponse globalInternalErrorResponse = obtainOpenApiResponse( 
                		webResponseSchema, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor" );
                
                if( responses.containsKey( BAD_REQUEST ) ) {
                	existingDecorator( responses, BAD_REQUEST, globalBadRequestResponse );
                } else {
                	responses.addApiResponse(BAD_REQUEST, globalBadRequestResponse);
                }//end if
                
                if( responses.containsKey( NOT_FOUND ) ) {
                	existingDecorator( responses, NOT_FOUND, globalNotFoundResponse );
                } else {
                	responses.addApiResponse(NOT_FOUND, globalNotFoundResponse);
                }//end if
                
                responses.addApiResponse(NOT_ALLOWED	, globalNotAllowedResponse);
                responses.addApiResponse(INTERNAL_ERROR	, globalInternalErrorResponse);
                
            })
        );
        */
        /*
        return openApi -> {
	        // Recorre todos los paths (endpoints) definidos en OpenAPI
	        openApi.getPaths().forEach((path, pathItem) -> {
	            // Para cada operación en el endpoint (GET, POST, etc.)
	            pathItem.readOperations().forEach(operation -> {
	                
	                // Obtiene las respuestas ya definidas en el endpoint
	                ApiResponses apiResponses = operation.getResponses();
	                
	                // Definiciones generales que queremos agregar si no existen
	                ApiResponse global400 = createApiResponse("400", "Solicitud incorrecta. Ver detalles en la respuesta");
	                ApiResponse global500 = createApiResponse("500", "Error interno del servidor. Contacte al administrador.");

	                // 🔹 Si ya existe el código 400 en el endpoint, lo combinamos
	                if (apiResponses.containsKey("400")) {
	                    ApiResponse existing400 = apiResponses.get("400");
	                    existing400.setDescription(existing400.getDescription() + " | " + global400.getDescription());
	                } else {
	                    apiResponses.addApiResponse("400", global400);
	                }

	                // 🔹 Si ya existe el código 500, lo combinamos
	                if (apiResponses.containsKey("500")) {
	                    ApiResponse existing500 = apiResponses.get("500");
	                    existing500.setDescription(existing500.getDescription() + " | " + global500.getDescription());
	                } else {
	                    apiResponses.addApiResponse("500", global500);
	                }
	            });
	        });
	    };
	    */
    }//end globalResponseOpenApiCustomiser()
	
	private ApiResponse obtainOpenApiResponse( Schema<?> webResponseSchema, HttpStatus httpStatus, String message ) {
		StringBuilder jsonResponse = new StringBuilder();
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			MetaDTO meta = new MetaDTO( httpStatus );
			WebResponseDTO response = WebResponseDTO.builder()
					.meta( meta )
					.build();
			
			jsonResponse.append( mapper.writeValueAsString( response ) );
		} catch( JsonProcessingException e ) {
			jsonResponse.append("{}");
		}//end try
		
		return new ApiResponse()
                .description( message )
                .content( new Content().addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema( webResponseSchema )
                                .example( jsonResponse.toString() )));
	}//end obtainOpenApiResponse()
	/*
	private void existingDecorator( ApiResponses responses, String responseCode, ApiResponse globalResponse ) {
		ApiResponse existing = responses.get( responseCode );
		existing.setDescription( existing.getDescription() + " | " + globalResponse.getDescription() );
		existing.getContent().forEach( (mediaType, media) ->  );
	}//end existingDecorator()
	*/
	
	
}
