package project.source.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "bills")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill extends BaseEntity<Long> {
    // ???
    // reservation
    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    // room
    // total price
    @Column(name="total_price")
    private Double totalPrice;

    // user who made the reservation
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // is paid
    // is canceled
    // is checked in
    // is checked out
    // is active
    // is deleted
    // created at
    // updated at
}
