package fleetcontrol.ms_vehicles.exception;

import java.util.UUID;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(UUID id) {
        super("No existe un vehículo con el ID proporcionado");
    }
}
