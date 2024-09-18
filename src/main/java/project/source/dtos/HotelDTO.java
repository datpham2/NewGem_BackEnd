package project.source.dtos;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

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
    private boolean status;
}
