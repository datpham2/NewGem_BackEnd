package project.source.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.util.Date;

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
    @Past(message = "Start Date must to one day in future")
    private Date startDate;
    @Past(message = "End Date must to one day in future")
    private Date endDate;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
