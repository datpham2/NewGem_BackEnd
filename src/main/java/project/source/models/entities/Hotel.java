package project.source.models.entities;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Data
@Builder
@Table(name = "hotels")
@NoArgsConstructor
@AllArgsConstructor
public class Hotel extends BaseEntity<Long>{
    @NotNull(message = "Name can't be empty")
    private String name;

    @NotNull(message = "Location can't be empty")
    private String location;

    @Digits(integer = 5, fraction = 2)
    @JsonProperty(value = "min_price")
    @Min(value = 1, message = "Min price must be greater than 0")
    private BigDecimal minPrice;

    @Digits(integer = 5, fraction = 2)
    @JsonProperty(value = "max_price")
    @Min(value = 1, message = "Max price must be greater than 0")
    private BigDecimal maxPrice;

    private double rating;

    @JsonProperty(value = "no_rooms")
    @Min(value = 0, message = "Number of rooms must be greater than or equal to 0")
    private int noRooms;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel", cascade = CascadeType.ALL)
    private Set<Reviews> reviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel", cascade = CascadeType.ALL)
    private Set<Voucher> vouchers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel", cascade = CascadeType.ALL)
    private Set<Room> rooms = new HashSet<>();

    public void setPrices() {
        if (this.rooms.isEmpty()) {
            this.minPrice = BigDecimal.ZERO;
            this.maxPrice = BigDecimal.ZERO;
            return;
        }

        this.minPrice = rooms.stream()
                .map(Room::getPrice)
                .min(Double::compare)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);

        this.maxPrice = rooms.stream()
                .map(Room::getPrice)
                .max(Double::compare)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
    }

    public void updateAverageRating() {
        if (reviews.isEmpty()) {
            this.rating = 0.0;
            return;
        }

        double average = reviews.stream()
                .mapToInt(Reviews::getRating)
                .average()
                .orElse(0.0); // Default to 0 if no reviews


        this.rating = Math.round(average * 10.0) / 10.0; 
    }
}
