package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;


@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity<Long>{
    // start date of the reservation
    @Column(name="start_date")
    @Future(message = "Start date must be in the future")
    private LocalDate checkIn;

    // end date of the reservation
    @Column(name="end_date")
    @Future(message = "End date must be in the future")
    private LocalDate checkOut;

    // room in the range of room available in Room class
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // user who made the reservation
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // number of adults
    @Column(name="adults")
    @Min(value = 1, message = "Number of adults must be greater than or equal to 1")
    @Value("1")
    private int adults;

    // number of children
    @Column(name="children")
    @Value("0")
    @Min(value = 0, message = "Number of children must be greater than or equal to 0")
    @Max(value = 10, message = "Number of children must be less than or equal to 10")
    private int children;

    // total price
    // is paid
    // is canceled
    // is checked in
    // is checked out
    // is active
    // is deleted
    // created at
    // updated at
}
