package fleetcontrol.ms_vehicles.dto;

import fleetcontrol.ms_vehicles.domain.FuelType;
import fleetcontrol.ms_vehicles.domain.VehicleStatus;
import fleetcontrol.ms_vehicles.domain.VehicleType;

import java.time.Instant;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String plate,
        String make,
        String model,
        Integer year,
        VehicleType type,
        FuelType fuelType,
        Integer tankCapacityL,
        Integer odometerKm,
        VehicleStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
