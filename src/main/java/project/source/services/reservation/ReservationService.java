package project.source.services.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.source.dtos.ReservationDTO;
import project.source.models.entities.Reservation;
import project.source.models.entities.User;
import project.source.repositories.ReservationRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService{
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation addReservation(ReservationDTO reservationDTO, Long userId) {
        return null;
    }
}
