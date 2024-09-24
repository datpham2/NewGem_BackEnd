package project.source.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.source.models.entities.Reservation;
import project.source.models.enums.Status;

import java.util.List;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findAllReservationByRoomId(Long roomId, PageRequest pageRequest);

    List<Reservation> findByStatus(Status status);

    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.id = :id")
    void deleteById(Long id);
}
