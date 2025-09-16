package com.projectmanager.backend.presentation.exception;

import com.projectmanager.backend.infrastructure.exception.ConflitoDeDadosException;
import com.projectmanager.backend.infrastructure.exception.RecursoNaoEncontradoException;
import com.projectmanager.backend.presentation.exception.dto.ErroPadraoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException; // Manter para compatibilidade com código antigo

import java.util.List;

@RestControllerAdvice // Anotação para tratamento global de exceções em controladores REST
public class GlobalExceptionHandler {

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

    // --- Tratamento de Erros de Validação (@Valid) ---

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
                "Bad Request", // ou status.getReasonPhrase()
                "Erro de validação nos dados da requisição.",
                request.getRequestURI(),
                errors);
        return new ResponseEntity<>(erro, status);
    }

    // --- Tratamento de outras ResponseStatusException (que podem vir do Spring,
    // como 400, 401, 403, 500) ---
    // Este handler é mais genérico e pode capturar exceções geradas diretamente
    // pelo Spring ou por outros serviços
    // que lançam ResponseStatusException (como fizemos anteriormente no
    // UsuarioService)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadraoDTO> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        ErroPadraoDTO erro = new ErroPadraoDTO(
                status.value(),
                status.getReasonPhrase(),
                ex.getReason(), // Pega a mensagem definida na ResponseStatusException
                request.getRequestURI());
        return new ResponseEntity<>(erro, status);
    }

    // --- Tratamento de exceções genéricas (catch-all) ---
    // Garante que nenhuma exceção vaze sem um tratamento padronizado. Sempre
    // retorne 500 Internal Server Error.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadraoDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        // Logar a exceção completa aqui é crucial para depuração
        ex.printStackTrace();
        ErroPadraoDTO erro = new ErroPadraoDTO(
                status.value(),
                status.getReasonPhrase(),
                "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.",
                request.getRequestURI());
        return new ResponseEntity<>(erro, status);
    }
}