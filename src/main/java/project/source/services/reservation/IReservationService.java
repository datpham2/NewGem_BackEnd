package project.source.services.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.source.dtos.ReservationDTO;
import project.source.models.entities.Reservation;

import java.util.List;
import java.util.Set;

public interface IReservationService {
    Page<Reservation> getAllReservation(PageRequest pageRequest);

    Reservation getReservationById(Long reservationId);

    Set<Reservation> getAllReservationByUserId(Long userId);

    Page<Reservation> getAllReservationByRoomId(Long roomId, PageRequest pageRequest);

    List<Reservation> saveReservations(List<Reservation> reservations);

    Reservation saveReservation(Reservation reservation);

    void updateReservation(Reservation reservation, Long reservationId);

    void deleteReservation(Long reservationId);
}
