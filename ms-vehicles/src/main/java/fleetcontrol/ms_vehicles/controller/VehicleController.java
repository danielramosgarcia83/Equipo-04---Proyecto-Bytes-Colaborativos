package fleetcontrol.ms_vehicles.controller;

import fleetcontrol.ms_vehicles.domain.VehicleStatus;
import fleetcontrol.ms_vehicles.domain.VehicleType;
import fleetcontrol.ms_vehicles.dto.PageResponse;
import fleetcontrol.ms_vehicles.dto.VehicleCreateRequest;
import fleetcontrol.ms_vehicles.dto.VehicleResponse;
import fleetcontrol.ms_vehicles.dto.VehicleUpdateRequest;
import fleetcontrol.ms_vehicles.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Inventario de vehículos de la flota")
public class VehicleController {

    private final VehicleService service;

    @GetMapping
    @Operation(summary = "Lista paginada de vehículos, filtrable por estado, tipo o placa (parcial)")
    @ApiResponse(responseCode = "200", description = "Página de vehículos")
    @ApiResponse(responseCode = "400", description = "VALIDATION_ERROR: filtro con valor no válido")
    public PageResponse<VehicleResponse> list(
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) String plate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.list(status, type, plate, page, size);
    }

    @GetMapping("/{vehicleId}")
    @Operation(summary = "Detalle de un vehículo")
    @ApiResponse(responseCode = "200", description = "Vehículo encontrado")
    @ApiResponse(responseCode = "404", description = "VEHICLE_NOT_FOUND")
    public VehicleResponse getById(@PathVariable UUID vehicleId) {
        return service.getById(vehicleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Da de alta un vehículo (siempre nace AVAILABLE)")
    @ApiResponse(responseCode = "201", description = "Vehículo creado")
    @ApiResponse(responseCode = "400", description = "VALIDATION_ERROR")
    @ApiResponse(responseCode = "409", description = "VEHICLE_ALREADY_EXISTS: la placa ya existe")
    public VehicleResponse create(@Valid @RequestBody VehicleCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{vehicleId}")
    @Operation(summary = "Actualiza los datos descriptivos (no modifica placa, estado ni odómetro)")
    @ApiResponse(responseCode = "200", description = "Vehículo actualizado")
    @ApiResponse(responseCode = "400", description = "VALIDATION_ERROR")
    @ApiResponse(responseCode = "404", description = "VEHICLE_NOT_FOUND")
    public VehicleResponse update(@PathVariable UUID vehicleId,
                                  @Valid @RequestBody VehicleUpdateRequest request) {
        return service.update(vehicleId, request);
    }
}
