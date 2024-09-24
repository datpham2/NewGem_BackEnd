package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.ReservationDTO;
import project.source.models.entities.Reservation;

import java.util.List;
import java.util.Set;


@Service
public interface IReservationService {
    Page<Reservation> getAllReservation(PageRequest pageRequest);

    Reservation getReservationById(Long reservationId);

    List<Reservation> getAllReservationByUserId(Long userId, PageRequest pageRequest);

    Set<Reservation> getAllReservationByUserId(Long userId);

    Page<Reservation> getAllReservationByRoomId(Long roomId, PageRequest pageRequest);

    List<Reservation> saveReservations(List<ReservationDTO> reservationDTOS);

    Reservation saveReservation(ReservationDTO reservationDTO);

    void updateReservation(ReservationDTO reservationDTO, Long reservationId);

    void deleteReservation(Long reservationId);
}
