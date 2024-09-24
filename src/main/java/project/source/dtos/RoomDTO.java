package project.source.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.dtos.validations.EnumPattern;
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.services.implement.HotelService;

import java.util.Set;
import java.util.stream.Collectors;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDTO {
    Long roomId;

    @NonNull
    Long hotelId;

    @NotNull
    int roomNumber;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    double price;

    @NotNull(message = "Type must not be null")
    @EnumPattern(name = "Room type", regexp = "SINGLE|DOUBLE|VIP", message = "Room type must be SINGLE, DOUBLE or VIP")
    RoomType type;

    int guests;

    Set<ReservationDTO> reservationDTOS;

    public static RoomDTO fromRoom(Room room){
        Set<ReservationDTO> reservations = room.getReservations().stream()
                .map(ReservationDTO::fromReservation).collect(Collectors.toSet());

        return RoomDTO.builder()
                .roomId(room.getId())
                .hotelId(room.getHotel().getId())
                .roomNumber(room.getRoomNumber())
                .price(room.getPrice())
                .type(room.getType())
                .reservationDTOS(reservations)
                .build();
    }
}
