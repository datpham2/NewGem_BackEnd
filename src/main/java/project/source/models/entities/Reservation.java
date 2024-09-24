package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;


@Entity
@Table(name = "reservations") /* old table name: bookrooms */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Reservation extends BaseEntity<Long>{
    // start date of the reservation
    @Column(name="check_in")
    @Future(message = "Start date must be in the future")
    private LocalDate checkIn; /* old column name: startDate */

    // end date of the reservation
    @Column(name="check_out")
    @Future(message = "End date must be in the future")
    private LocalDate checkOut; /* old column name: endDate */

    // hotel where the reservation is made
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    // room in the range of room available in Room class
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    // user who made the reservation
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // number of adults
    @Column(name="adults")
    @Min(value = 1, message = "Number of adults must be greater than or equal to 1")
    @Value("1")
    private int adults; /* old column name: noAdults */

    // number of children
    @Column(name="children")
    @Value("0")
    @Min(value = 0, message = "Number of children must be greater than or equal to 0")
    @Max(value = 10, message = "Number of children must be less than or equal to 10")
    private int children; /* old column name: noChild */

    // bill for the reservation
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    // status of the reservation
    @Column(name="status")
    private String status;

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
