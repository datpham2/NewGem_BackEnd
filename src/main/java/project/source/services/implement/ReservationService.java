package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.exceptions.ConflictException;
import project.source.exceptions.MultipleException;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.repositories.ReservationRepository;
import project.source.services.IReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReservationService implements IReservationService {
    ReservationRepository reservationRepository;
    UserService userService;
    RoomService roomService;

    @Override
    public Page<Reservation> getAllReservation(PageRequest pageRequest) {
        return reservationRepository.findAll(pageRequest);
    }

    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                ()-> new NotFoundException("Can not find reservation with id: " + reservationId));
    }

    @Override
    public Set<Reservation> getAllReservationByUserId(Long userId) {
        userService.getUserById(userId);
        return reservationRepository.findAllReservationByUserId(userId);
    }

    @Override
    public Page<Reservation> getAllReservationByRoomId(Long roomId, PageRequest pageRequest) {
        roomService.getRoomById(roomId);
        return reservationRepository.findAllReservationByRoomId(roomId, pageRequest);
    }

    @Override
    public void saveReservations(List<Reservation> reservations) {
        List<Exception> exceptions = new ArrayList<>();

        for (Reservation reservation : reservations) {
            try {
                saveReservation(reservation);
            } catch (ConflictException | NotFoundException e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            throw new MultipleException(exceptions);
        }
    }

    @Override
    public void saveReservation(Reservation reservation) {
        Room room = roomService.getRoomById(reservation.getRoom().getId());
        boolean booked = room.bookRoom(reservation);
        if(booked){
            reservationRepository.save(reservation);
        }else {
            throw new ConflictException("Can not book room " + reservation.getRoom().getRoomNumber()
                    + " of " + reservation.getRoom().getHotel().getName()
                    + " from " + reservation.getCheckIn()
                    + " to " + reservation.getCheckOut()
            );
        }
    }

    @Override
    public void updateReservation(Reservation reservation, Long reservationId) {
        Reservation updatedReservation = getReservationById(reservationId);
        updatedReservation.setAdults(reservation.getAdults());
        updatedReservation.setChildren(reservation.getChildren());

        reservationRepository.save(updatedReservation);
    }


    public void deleteReservations(List<Long> reservationIds) {
        List<Exception> exceptions = new ArrayList<>();

        for (Long id : reservationIds){
            try {
                deleteReservation(id);
            } catch (ConflictException | NotFoundException e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            throw new MultipleException(exceptions);
        }
    }

    @Override
    public void deleteReservation(Long reservationId) {
        LocalDate date = LocalDate.now();
        Reservation reservation = reservationRepository.getReferenceById(reservationId);
        if (date.isBefore(reservation.getCheckIn()) || date.isEqual(reservation.getCheckIn())) {
            reservationRepository.delete(reservation);
        } else {
            throw new ConflictException("Can not cancel reservation after check in day");
        }
    }
}
