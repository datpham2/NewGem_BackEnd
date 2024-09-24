package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "vouchers")
public class Voucher extends BaseEntity<Long>{
    @DecimalMin(value = "0.5", message = "Discount must be at least 0.5")
    @DecimalMax(value = "1.0", message = "Discount must not exceed 1.0")
    private float discount;


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
