package com.projectmanager.backend.presentation.exception;

import com.projectmanager.backend.infrastructure.exception.ConflitoDeDadosException;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;
import com.projectmanager.backend.presentation.exception.dto.ErroPadraoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<ErroPadraoDTO> handleRecursoNaoEncontradoException(
                        RecursoNaoEncontradoException ex, HttpServletRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                ErroPadraoDTO erro = new ErroPadraoDTO(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(erro, status);
        }

        @ExceptionHandler(ConflitoDeDadosException.class)
        public ResponseEntity<ErroPadraoDTO> handleConflitoDeDadosException(
                        ConflitoDeDadosException ex, HttpServletRequest request) {
                HttpStatus status = HttpStatus.CONFLICT;
                ErroPadraoDTO erro = new ErroPadraoDTO(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(erro, status);
        }

        // Seu handler de validação, que já está excelente:
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErroPadraoDTO> handleValidationExceptions(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {
                HttpStatus status = HttpStatus.BAD_REQUEST;
                List<String> errors = ex.getBindingResult().getAllErrors().stream()
                                .map(error -> {
                                        String fieldName = ((FieldError) error).getField();
                                        String errorMessage = error.getDefaultMessage();
                                        return fieldName + ": " + errorMessage;
                                })
                                .toList();
                ErroPadraoDTO erro = new ErroPadraoDTO(
                                status.value(),
                                "Bad Request",
                                "Erro de validação nos dados da requisição.",
                                request.getRequestURI(),
                                errors);
                return new ResponseEntity<>(erro, status);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ErroPadraoDTO> handleResponseStatusException(
                        ResponseStatusException ex, HttpServletRequest request) {
                HttpStatus status = (HttpStatus) ex.getStatusCode();
                ErroPadraoDTO erro = new ErroPadraoDTO(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getReason(),
                                request.getRequestURI());
                return new ResponseEntity<>(erro, status);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErroPadraoDTO> handleGenericException(
                        Exception ex, HttpServletRequest request) {
                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                String errorId = UUID.randomUUID().toString();

                logger.error("Erro interno do servidor. ID do erro: {}", errorId, ex);

                List<String> details = List.of(
                                "Um erro inesperado ocorreu. Por favor, entre em contato com o suporte e forneça o ID do erro: "
                                                + errorId);

                ErroPadraoDTO erro = new ErroPadraoDTO(
                                status.value(),
                                status.getReasonPhrase(),
                                "Ocorreu um erro interno no servidor.",
                                request.getRequestURI(),
                                details);
                return new ResponseEntity<>(erro, status);
        }
}