package vn.vanquang239dn.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import vn.vanquang239dn.dto.response.ErrorResponse;
import org.springframework.http.MediaType;

@RestControllerAdvice
public class GlobalException {

        // Method Argument Not Valid Exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "Validation Error", summary = "Handle bad request", value = """
                                                                        {
                                                                            "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                            "status": 409,
                                                                            "path": "api/v1/...",
                                                                            "message": "{data} dont exists, Please try again !"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                        HttpServletRequest request) {

                // Get error messages
                Map<String, Object> errors = new HashMap<>();

                e.getBindingResult().getFieldErrors()
                                .forEach(error -> errors
                                                .put(error.getField(), error.getDefaultMessage()));

                // Error response instance
                ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                                request.getRequestURI(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);

                return errorResponse;
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
                                                                          "status": 409,
                                                                          "path": "api/v1/...",
                                                                          "message": "{data} not found, Please try again!"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e,
                        HttpServletRequest request) {

                // Get error messages
                Map<String, Object> errors = new HashMap<>();

                errors.put(e.getFieldName(), e.getMessage());

                // Error response instance
                ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                                request.getRequestURI(), HttpStatus.NOT_FOUND.getReasonPhrase(), errors);

                return errorResponse;
        }

        // Duplicate Resource Exception
        @ExceptionHandler(DuplicateResourceException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "404 Response", summary = "Handle resource not found exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 409,
                                                                          "path": "api/v1/...",
                                                                          "message": "{data} not found, Please try again!"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ErrorResponse handleDuplicateResourceException(DuplicateResourceException e,
                        HttpServletRequest request) {

                // Get error messages
                Map<String, Object> errors = new HashMap<>();

                errors.put(e.getFieldName(), e.getMessage());

                // Error response instance
                ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                                request.getRequestURI(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);

                return errorResponse;
        }
}
