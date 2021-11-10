package se.iths.remoteyourcar.repositories;

import org.springframework.data.repository.CrudRepository;
import se.iths.remoteyourcar.entities.VehicleState;

public interface VehicleStateRepository extends CrudRepository<VehicleState, Long> {
}
