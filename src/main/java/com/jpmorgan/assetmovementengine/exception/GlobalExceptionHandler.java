package com.jpmorgan.assetmovementengine.exception;

import com.jpmorgan.assetmovementengine.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SsiNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSsiNotFound(SsiNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(SettlementNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSettlementNotFound(SettlementNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateTradeIdException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateTrade(DuplicateTradeIdException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Keep it simple: first field error message
        String msg = "validation failed";
        if (ex.getBindingResult().getFieldError() != null) {
            msg = ex.getBindingResult().getFieldError().getField() + " " +
                    ex.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ex.getMessage()));
    }
}
