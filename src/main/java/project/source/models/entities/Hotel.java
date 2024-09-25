package project.source.models.entities;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import project.source.models.enums.City;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@Builder
@Table(name = "hotels")
@NoArgsConstructor
@AllArgsConstructor
public class Hotel extends BaseEntity<Long>{
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "location", nullable = false)
    private String location;


    @Column(name = "city", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private City city;

    @Digits(integer = 10, fraction = 2)
    @JsonProperty(value = "min_price")
    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Digits(integer = 10, fraction = 2)
    @JsonProperty(value = "max_price")
    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "rating")
    private double rating;

    @JsonProperty(value = "no_rooms")
    @Column(name = "no_rooms")
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
        if (this.rooms == null || this.rooms.isEmpty()) {
            this.minPrice = BigDecimal.ZERO;
            this.maxPrice = BigDecimal.ZERO;
            return;
        }

        this.minPrice = rooms.stream()
                .map(Room::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        this.maxPrice = rooms.stream()
                .map(Room::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }



    public void updateAverageRating() {
        Set<Reviews> activeReviews = reviews.stream()
                .filter(review -> review.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toSet());

        if (activeReviews.isEmpty()) {
            this.rating = 0.0;
            return;
        }

        double average = activeReviews.stream()
                .mapToInt(Reviews::getRating)
                .average()
                .orElse(0.0);

        this.rating = Math.round(average * 10.0) / 10.0;
    }
}
