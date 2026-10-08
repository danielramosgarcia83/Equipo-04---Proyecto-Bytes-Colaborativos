package fleetcontrol.ms_vehicles.mapper;

import fleetcontrol.ms_vehicles.domain.Vehicle;
import fleetcontrol.ms_vehicles.dto.VehicleCreateRequest;
import fleetcontrol.ms_vehicles.dto.VehicleResponse;
import fleetcontrol.ms_vehicles.dto.VehicleUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponse toResponse(Vehicle vehicle);

    /** id, status, version y fechas los asigna el servicio / la auditoría. */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "plate", source = "plate")
    @Mapping(target = "make", source = "make")
    @Mapping(target = "model", source = "model")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "fuelType", source = "fuelType")
    @Mapping(target = "tankCapacityL", source = "tankCapacityL")
    @Mapping(target = "odometerKm", source = "odometerKm")
    Vehicle toEntity(VehicleCreateRequest request);

    /** Whitelist explícita: plate, status y odometerKm quedan fuera a propósito. */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "make", source = "make")
    @Mapping(target = "model", source = "model")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "fuelType", source = "fuelType")
    @Mapping(target = "tankCapacityL", source = "tankCapacityL")
    void updateEntity(VehicleUpdateRequest request, @MappingTarget Vehicle vehicle);
}

