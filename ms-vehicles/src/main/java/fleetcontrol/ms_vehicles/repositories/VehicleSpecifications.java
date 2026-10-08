package fleetcontrol.ms_vehicles.repositories;

import fleetcontrol.ms_vehicles.domain.Vehicle;
import fleetcontrol.ms_vehicles.domain.VehicleStatus;
import fleetcontrol.ms_vehicles.domain.VehicleType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class VehicleSpecifications {

    private VehicleSpecifications() {
    }

    /** Filtros opcionales y combinables. plate: búsqueda parcial e insensible a mayúsculas. */
    public static Specification<Vehicle> filter(VehicleStatus status, VehicleType type, String plate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (plate != null && !plate.isBlank()) {
                String pattern = "%" + escapeLike(plate.trim().toLowerCase()) + "%";
                predicates.add(cb.like(cb.lower(root.get("plate")), pattern, '\\'));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String escapeLike(String value) {
        return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
