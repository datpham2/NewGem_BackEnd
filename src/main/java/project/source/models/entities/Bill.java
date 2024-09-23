package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bills")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Bill extends BaseEntity<Long> {
    // ???
    // reservation
    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Reservation> reservations = new HashSet<>();
    // room
    // total price
    @Column(name="total_price")
    private Double totalPrice;

    // user who made the reservation
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // hotel where the reservation was made
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "total_fee", nullable = false)
    private BigDecimal totalFee;

    // voucher
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    // paid?
    @Column(name = "is_paid")
    private boolean isPaid;

    // check out date
    @Column(name = "check_out")
    private LocalDate checkOut;

    public void calculateTotalFee() {
        // calculate total fee
        double totalFee = 0;
        for (Reservation reservation : reservations) {
            totalFee += reservation.getRoom().getPrice();
        }
        this.totalFee = BigDecimal.valueOf(totalFee);

    }
    // is paid
    // is canceled
    // is checked in
    // is checked out
    // is active
    // is deleted
    // created at
    // updated at
}
