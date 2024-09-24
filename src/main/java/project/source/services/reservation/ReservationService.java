package project.source.services.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.source.dtos.ReservationDTO;
import project.source.models.entities.Hotel;
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.models.entities.User;
import project.source.repositories.ReservationRepository;
import project.source.services.implement.HotelService;
import project.source.services.implement.UserService;
import project.source.services.room.RoomService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService{
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final HotelService hotelService;

    @Override
    public Page<Reservation> getAllReservation(PageRequest pageRequest) {
        return reservationRepository.findAll(pageRequest);
    }

    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    @Override
    public Page<Reservation> getAllReservationByUserId(Long userId, PageRequest pageRequest) {
        userService.getUserById(userId);
        return reservationRepository.findAllReservationByUserId(userId, pageRequest);
    }

    @Override
    public Page<Reservation> getAllReservationByRoomId(Long roomId, PageRequest pageRequest) {
        roomService.getRoomById(roomId);
        return reservationRepository.findAllReservationByRoomId(roomId, pageRequest);
    }

    @Override
    public List<Reservation> saveReservations(List<Reservation> reservations) {
        List<Exception> exceptions = new ArrayList<>();
        List<Reservation> newReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            try {
                Reservation newReservation = saveReservation(reservation);
                newReservations.add(newReservation);
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        return newReservations;
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation saveReservation(Reservation reservation, Long userId, Long roomId, Long hotelId) {
        User user = userService.getUserById(userId);
        Room room = roomService.getRoomById(roomId, hotelId);
        Hotel hotel = hotelService.getHotelById(hotelId);
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setHotel(hotel);
        return reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Reservation reservation, Long reservationId) {
        Reservation reservationToUpdate = reservationRepository.findById(reservationId).orElse(null);
        if (reservationToUpdate == null) {
            throw new RuntimeException("Reservation not found");
        }
        reservationToUpdate.setCheckIn(reservation.getCheckIn());
        reservationToUpdate.setCheckOut(reservation.getCheckOut());
        reservationToUpdate.setRoom(reservation.getRoom());
        reservationToUpdate.setUser(reservation.getUser());
        reservationRepository.save(reservationToUpdate);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }
        reservationRepository.delete(reservation);
    }
}
