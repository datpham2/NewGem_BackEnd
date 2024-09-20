package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import lombok.*;
import project.source.models.enums.Status;

import java.time.LocalDate;

/**
 * @Author An Nguyen
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Voucher extends BaseEntity<Long>{
    @Min(value = 1, message = "Discount must to greater than 1")
    private int discount;
    @FutureOrPresent(message = "Start Date must to one day in future")
    private LocalDate startDate;
    @FutureOrPresent(message = "End Date must to one day in future")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
