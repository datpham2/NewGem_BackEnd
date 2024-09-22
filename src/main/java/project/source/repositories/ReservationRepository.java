package project.source.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Reservation;

import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findAllReservationByRoomId(Long roomId, PageRequest pageRequest);

    Set<Reservation> findAllReservationByUserId(Long userId);
}
