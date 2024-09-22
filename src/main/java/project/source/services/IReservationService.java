package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.models.entities.Reservation;

import java.util.List;
import java.util.Set;


@Service
public interface IReservationService {
    Page<Reservation> getAllReservation(PageRequest pageRequest);

    Reservation getReservationById(Long reservationId);

    Set<Reservation> getAllReservationByUserId(Long userId);

    Page<Reservation> getAllReservationByRoomId(Long roomId, PageRequest pageRequest);

    void saveReservations(List<Reservation> reservations);

    void saveReservation(Reservation reservation);

    void updateReservation(Reservation reservation, Long reservationId);

    void deleteReservation(Long reservationId);
}
