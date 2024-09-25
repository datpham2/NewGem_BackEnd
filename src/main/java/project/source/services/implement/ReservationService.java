package project.source.services.implement;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import project.source.dtos.ReservationDTO;
import project.source.exceptions.ConflictException;
import project.source.exceptions.MultipleException;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.models.entities.User;
import project.source.models.enums.Status;
import project.source.repositories.ReservationRepository;
import project.source.services.IReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReservationService implements IReservationService {
    ReservationRepository reservationRepository;
    UserService userService;
    RoomService roomService;
    HotelService hotelService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<Reservation> getAllReservation(PageRequest pageRequest) {
        return reservationRepository.findAll(pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                ()-> new NotFoundException("Can not find reservation with id: " + reservationId));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Reservation> getAllReservationByUserId(Long userId, PageRequest pageRequest) {
        userService.getUserById(userId);
        return reservationRepository.findAllReservationByUserId(userId, pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Set<Reservation> getAllReservationByUserId(Long userId) {
        userService.getUserById(userId);
        return new HashSet<>(reservationRepository.findAllReservationByUserId(userId));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<Reservation> getAllReservationByRoomId(Long roomId, PageRequest pageRequest) {
        roomService.getRoomById(roomId);
        return reservationRepository.findAllReservationByRoomId(roomId, pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Reservation> saveReservations(List<ReservationDTO> reservationDTOS) {
        List<Exception> exceptions = new ArrayList<>();
        List<Reservation> newReservations = new ArrayList<>();

        for (ReservationDTO reservationDTO : reservationDTOS) {
            try {
                Reservation newReservation = saveReservation(reservationDTO);
                newReservations.add(newReservation);
            } catch (ConflictException | NotFoundException e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            throw new MultipleException(exceptions);
        }
        return newReservations;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Reservation saveReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getCheckOut().isBefore(reservationDTO.getCheckIn())){
            throw new ConflictException("Check out date can not be before check in date");
        }
        Room room = roomService.getRoomById(reservationDTO.getRoomId());
        User user = userService.getUserById(reservationDTO.getUserId());

        Reservation reservation = Reservation.builder()
                .user(user)
                .hotel(room.getHotel())
                .room(room)
                .status(Status.ACTIVE)
                .adults(reservationDTO.getAdults())
                .children(reservationDTO.getChildren())
                .checkIn(reservationDTO.getCheckIn())
                .checkOut(reservationDTO.getCheckOut())
                .build();
        boolean booked = room.bookRoom(reservation);
        if(booked){
            return reservationRepository.save(reservation);
        }else {
            throw new ConflictException("Can not book room " + reservation.getRoom().getRoomNumber()
                    + " of " + reservation.getRoom().getHotel().getName()
                    + " from " + reservation.getCheckIn()
                    + " to " + reservation.getCheckOut()
            );
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void updateReservation(ReservationDTO reservationDTO, Long reservationId) {
        Reservation updatedReservation = getReservationById(reservationId);
        updatedReservation.setAdults(reservationDTO.getAdults());
        updatedReservation.setChildren(reservationDTO.getChildren());

        Room room = roomService.getRoomById(reservationDTO.getRoomId());
        if (room.isBooked(reservationDTO.getCheckIn())){
            throw new ConflictException("The room is booked on check in date");
        }
        if (room.isBooked(reservationDTO.getCheckOut())){
           throw new ConflictException("The room is booked on check out date");
        };
        updatedReservation.setCheckOut(reservationDTO.getCheckOut());
        updatedReservation.setCheckIn(reservationDTO.getCheckIn());
        reservationRepository.save(updatedReservation);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void deleteReservation(Long reservationId) {
        LocalDate date = LocalDate.now();
        Reservation reservation = getReservationById(reservationId);
        if (reservation.getStatus().equals(Status.INACTIVE)){
            throw new ConflictException("Cannot cancel inactive reservation");
        }
        if (date.isBefore(reservation.getCheckIn()) || date.isEqual(reservation.getCheckIn())) {
            log.info("Deleting reservation with ID: {}", reservationId);
            reservationRepository.deleteById(reservationId);
            reservationRepository.flush();
        } else {
            throw new ConflictException("Cannot cancel reservation after check-in day");
        }
    }
}
