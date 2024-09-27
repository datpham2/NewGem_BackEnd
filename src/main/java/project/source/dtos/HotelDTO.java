package project.source.dtos;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import project.source.dtos.validations.EnumPattern;
import project.source.exceptions.ConflictException;
import project.source.models.entities.Hotel;
import project.source.models.enums.City;
import project.source.models.enums.Gender;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    private Long hotelId;

    @NotNull(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Location can't be empty")
    private String location;

    @NotNull(message = "City can't be empty")
    @EnumPattern(name = "City", regexp = "HCM|HANOI", message = "Gender must be HCM or HANOI")
    private City city;

    @Digits(integer = 10, fraction = 2)
    @JsonProperty(value = "min_price")
    private BigDecimal minPrice;

    @Digits(integer = 10, fraction = 2)
    @JsonProperty(value = "max_price")
    private BigDecimal maxPrice;

    private double rating;

    @JsonProperty(value = "no_rooms")
    private int noRooms;

    private Set<VoucherDTO> vouchers;

    private Set<ReviewsDTO> reviews;

    private Status status;

    public static HotelDTO fromHotel(Hotel hotel){
        Set<VoucherDTO> vouchers = null;
        Set<ReviewsDTO> reviews = null;

        if (hotel.getVouchers() != null){
            vouchers = hotel.getVouchers().stream().map(VoucherDTO::fromVoucher).collect(Collectors.toSet());
        }

        if (hotel.getReviews() != null){
            reviews = hotel.getReviews().stream().map(ReviewsDTO::fromReview).collect(Collectors.toSet());
        }

        return HotelDTO.builder()
                .hotelId(hotel.getId())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .city(hotel.getCity())
                .minPrice(hotel.getMinPrice())
                .maxPrice(hotel.getMaxPrice())
                .rating(hotel.getRating())
                .noRooms(hotel.getNoRooms())
                .status(hotel.getStatus())
                .vouchers(vouchers)
                .reviews(reviews)
                .build();
    }
}
