package br.com.mmc.bilhetinho_api.exception;

import br.com.mmc.bilhetinho_api.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.error.include-trace:false}")
    private boolean includeTrace;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {} - URL: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        log.warn("Regra de negócio violada: {} - URL: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, "Violação de regra de negócio", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String erros = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Erro de validação: {} - URL: {}", erros, request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, "Dados de requisição inválidos", erros, request, ex);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Corpo da requisição JSON inválido: {} - URL: {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, "Corpo da requisição JSON inválido ou malformatado", "O payload JSON enviado é inválido ou contém erros de sintaxe.", request, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Erro interno no servidor: {} - URL: {}", ex.getMessage(), request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", "Ocorreu um erro interno. Consulte os administradores do sistema.", request, ex);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String error, String message, HttpServletRequest request, Throwable ex) {
        String trace = null;
        if (includeTrace) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            trace = sw.toString();
        }

        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getRequestURI(),
                trace
        );

        return ResponseEntity.status(status).body(body);
    }
}
