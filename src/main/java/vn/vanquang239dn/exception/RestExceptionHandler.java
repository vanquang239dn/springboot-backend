package vn.vanquang239dn.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import vn.vanquang239dn.dto.response.ExceptionResponse;
import org.springframework.http.MediaType;

@RestControllerAdvice
public class RestExceptionHandler {

        // Method Argument Not Valid Exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "Validation Error", summary = "Handle bad request", value = """
                                                                        {
                                                                            "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                            "status": 400,
                                                                            "path": "api/v1/...",
                                                                            "message": "Bad Request"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                        HttpServletRequest request) {

                // Get error messages
                Map<String, String> errors = new HashMap<>();

                e.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return buildExceptionResponse(HttpStatus.BAD_REQUEST, "Argument invalid", errors, request);

        }

        // Resource Not Found Exception
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "404 Response", summary = "Handle resource not found exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 404,
                                                                          "path": "api/v1/...",
                                                                          "message": "Resource not found"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException e,
                        HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage(), null, request);
        }

        // Duplicate Resource Exception
        @ExceptionHandler(DuplicateResourceException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "400", description = "Duplicate resource", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "400 Response", summary = "Handle resource duplicate exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 400,
                                                                          "path": "api/v1/...",
                                                                          "message": "Duplicate resource"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleDuplicateResourceException(DuplicateResourceException e,
                        HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.CONFLICT, e.getMessage(), e.getMessage(), request);
        }

        // Response Status Exception
        @ExceptionHandler(ResponseStatusException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "401", description = "Response status exception", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "401 Response", summary = "Handle authentication exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 401,
                                                                          "path": "api/v1/...",
                                                                          "message": "Response status exception"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleResponseStatusException(ResponseStatusException e,
                        HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.UNAUTHORIZED, e.getReason(), null, request);
        }

        // Default exception
        @ExceptionHandler(Exception.class)
        public ExceptionResponse handleException(Exception e, HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null, request);
        }

        // Builder for exception response
        private ExceptionResponse buildExceptionResponse(HttpStatus status, String message, Object details,
                        HttpServletRequest request) {

                return ExceptionResponse.builder()
                                .timestamp(Instant.now())
                                .status(status.value())
                                .path(request.getRequestURI())
                                .message(message)
                                .details(details)
                                .build();
        }
}
