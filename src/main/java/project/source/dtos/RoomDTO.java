package project.source.dtos;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.dtos.validations.EnumPattern;

import project.source.exceptions.ConflictException;
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.repositories.HotelRepository;


import java.math.BigDecimal;
import java.util.List;
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

    Long hotelId;

    String hotelName;

    @NotNull
    int roomNumber;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    BigDecimal price;

    @NotNull(message = "Type must not be null")
    @EnumPattern(name = "Room type", regexp = "SINGLE|DOUBLE|VIP", message = "Room type must be SINGLE, DOUBLE or VIP")
    RoomType type;

    int guests;

    public static RoomDTO fromRoom(Room room){
        return RoomDTO.builder()
                .roomId(room.getId())
                .hotelId(room.getHotel().getId())
                .hotelName(room.getHotel().getName())
                .roomNumber(room.getRoomNumber())
                .price(room.getPrice())
                .type(room.getType())
                .guests(room.getGuests())
                .build();
    }
}
