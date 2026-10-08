package fleetcontrol.ms_vehicles.dto;

import fleetcontrol.ms_vehicles.domain.FuelType;
import fleetcontrol.ms_vehicles.domain.VehicleType;
import fleetcontrol.ms_vehicles.validation.ValidYear;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record VehicleCreateRequest(
        @Schema(example = "9012-PQR")
        @NotBlank(message = "es obligatoria") @Size(max = 20, message = "máximo 20 caracteres")
        String plate,

        @Schema(example = "Mercedes-Benz")
        @NotBlank(message = "es obligatoria") @Size(max = 60, message = "máximo 60 caracteres")
        String make,

        @Schema(example = "Actros")
        @NotBlank(message = "es obligatorio") @Size(max = 60, message = "máximo 60 caracteres")
        String model,

        @Schema(example = "2023")
        @NotNull(message = "es obligatorio") @ValidYear
        Integer year,

        @Schema(example = "TRUCK")
        @NotNull(message = "es obligatorio")
        VehicleType type,

        @Schema(example = "DIESEL")
        @NotNull(message = "es obligatorio")
        FuelType fuelType,

        @Schema(example = "400")
        @NotNull(message = "es obligatoria") @Positive(message = "debe ser mayor que 0")
        Integer tankCapacityL,

        @Schema(example = "12000")
        @NotNull(message = "es obligatorio") @PositiveOrZero(message = "debe ser mayor o igual que 0")
        Integer odometerKm
) {
}
