package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
