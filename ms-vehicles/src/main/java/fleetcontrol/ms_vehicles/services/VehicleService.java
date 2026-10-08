package fleetcontrol.ms_vehicles.services;

import fleetcontrol.ms_vehicles.domain.Vehicle;
import fleetcontrol.ms_vehicles.domain.VehicleStatus;
import fleetcontrol.ms_vehicles.domain.VehicleType;
import fleetcontrol.ms_vehicles.dto.PageResponse;
import fleetcontrol.ms_vehicles.dto.VehicleCreateRequest;
import fleetcontrol.ms_vehicles.dto.VehicleResponse;
import fleetcontrol.ms_vehicles.dto.VehicleUpdateRequest;
import fleetcontrol.ms_vehicles.exception.VehicleAlreadyExistsException;
import fleetcontrol.ms_vehicles.exception.VehicleNotFoundException;
import fleetcontrol.ms_vehicles.mapper.VehicleMapper;
import fleetcontrol.ms_vehicles.repositories.VehicleRepository;
import fleetcontrol.ms_vehicles.repositories.VehicleSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    static final int MAX_PAGE_SIZE = 100;

    private final VehicleRepository repository;
    private final VehicleMapper mapper;

    @Transactional
    public VehicleResponse create(VehicleCreateRequest request) {
        String plate = normalizePlate(request.plate());
        if (repository.existsByPlate(plate)) {
            throw new VehicleAlreadyExistsException(plate);
        }

        Vehicle vehicle = mapper.toEntity(request);
        vehicle.setId(UUID.randomUUID());
        vehicle.setPlate(plate);
        vehicle.setStatus(VehicleStatus.AVAILABLE);

        try {
            // flush inmediato: si dos peticiones simultáneas pasan el exists, la restricción UNIQUE las frena aquí
            vehicle = repository.saveAndFlush(vehicle);
        } catch (DataIntegrityViolationException ex) {
            throw new VehicleAlreadyExistsException(plate);
        }
        return mapper.toResponse(vehicle);
    }

    @Transactional(readOnly = true)
    public PageResponse<VehicleResponse> list(VehicleStatus status, VehicleType type, String plate,
                                              int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        // orden determinista: sin él la paginación puede repetir u omitir filas
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("plate").ascending());

        return PageResponse.from(
                repository.findAll(VehicleSpecifications.filter(status, type, plate), pageable)
                        .map(mapper::toResponse));
    }

    @Transactional(readOnly = true)
    public VehicleResponse getById(UUID id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public VehicleResponse update(UUID id, VehicleUpdateRequest request) {
        Vehicle vehicle = findOrThrow(id);
        mapper.updateEntity(request, vehicle);
        // flush para que updatedAt (auditoría) ya esté actualizado en la respuesta
        return mapper.toResponse(repository.saveAndFlush(vehicle));
    }

    private Vehicle findOrThrow(UUID id) {
        return repository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
    }

    /** Placa en mayúsculas y sin espacios en los extremos: "4821-kdf " == "4821-KDF". */
    static String normalizePlate(String plate) {
        return plate.trim().toUpperCase(Locale.ROOT);
    }
}
