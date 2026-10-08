package fleetcontrol.ms_vehicles.exception;

public class VehicleAlreadyExistsException extends RuntimeException {

    public VehicleAlreadyExistsException(String plate) {
        super("Ya existe un vehículo con esa placa");
    }
}
