package fleetcontrol.ms_vehicles.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Contrato de errores común. TODO: al llegar US-03, mover a la librería/paquete de seguridad común
 * y borrar esta copia local.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(String error, String message, List<FieldDetail> details, Instant timestamp) {

    public record FieldDetail(String field, String reason) {
    }

    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(error, message, null, Instant.now());
    }

    public static ErrorResponse of(String error, String message, List<FieldDetail> details) {
        return new ErrorResponse(error, message, details, Instant.now());
    }
}
