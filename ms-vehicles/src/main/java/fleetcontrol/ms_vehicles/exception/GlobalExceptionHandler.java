package fleetcontrol.ms_vehicles.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_MESSAGE = "La petición contiene campos no válidos";

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(VehicleNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ErrorResponse.of("VEHICLE_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(VehicleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(VehicleAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ErrorResponse.of("VEHICLE_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldDetail(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, ErrorResponse.of("VALIDATION_ERROR", VALIDATION_MESSAGE, details));
    }

    /** Enum inválido en un query param (?status=FOO) o UUID mal formado en la ruta. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var detail = new ErrorResponse.FieldDetail(ex.getName(), "valor no válido: '" + ex.getValue() + "'");
        return build(HttpStatus.BAD_REQUEST,
                ErrorResponse.of("VALIDATION_ERROR", VALIDATION_MESSAGE, List.of(detail)));
    }

    /** JSON mal formado o enum inválido en el body. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST,
                ErrorResponse.of("VALIDATION_ERROR", "El cuerpo de la petición no es válido o está mal formado"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Error no controlado", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorResponse.of("INTERNAL_ERROR", "Error interno del servidor"));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, ErrorResponse body) {
        return ResponseEntity.status(status).body(body);
    }
}
