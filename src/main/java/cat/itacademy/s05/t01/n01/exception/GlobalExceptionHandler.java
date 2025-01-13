package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<String>> handlePlayerNotFound(PlayerNotFoundException e){
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
    }

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<String>> handleGameNotFound(GameNotFoundException e){
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleIllegalArgument(IllegalArgumentException e){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<HashMap<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
