package project.source.dtos;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import project.source.exceptions.ConflictException;
import project.source.models.entities.Hotel;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    @NotNull(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Location can't be empty")
    private String location;
    @Digits(integer = 5, fraction = 2)
    @JsonProperty(value = "min_price")
    @Min(value = 1, message = "Price must be greater than 1")
    private BigDecimal minPrice;
    @Digits(integer = 5, fraction = 2)
    @JsonProperty(value = "max_price")
    @Min(value = 1, message = "Price must be greater than 1")
    private BigDecimal maxPrice;
    //private Image images;
    private int rating;
    @JsonProperty(value = "no_rooms")
    @Min(value = 1, message = "Number of rooms must be greater than 1")
    private int noRooms;
    private Status status;

    public static HotelDTO fromHotel(Hotel hotel){
        return HotelDTO.builder()
                .name(hotel.getName())
                .location(hotel.getLocation())
                .minPrice(hotel.getMinPrice())
                .maxPrice(hotel.getMaxPrice())
                .rating(hotel.getRating())
                .noRooms(hotel.getNoRooms())
                .status(hotel.getStatus())
                .build();
    }

    public void validatePrices() {
        if (maxPrice.compareTo(minPrice) < 0) {
            throw new ConflictException("Max price must be greater than or equal to min price");
        }
    }
}
