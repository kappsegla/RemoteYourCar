package se.iths.remoteyourcar.repositories;

import org.springframework.data.repository.CrudRepository;
import se.iths.remoteyourcar.entities.Vehicle;

public interface VehicleCrudRepository extends CrudRepository<Vehicle, Long> {
}
