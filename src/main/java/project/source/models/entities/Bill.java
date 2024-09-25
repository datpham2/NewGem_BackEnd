package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bills")
public class Bill extends BaseEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false, updatable = false)
    Hotel hotel;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    Set<Reservation> reservations = new HashSet<>();

    @Digits(integer = 10, fraction = 2)
    @Column(name = "total_fee", nullable = false)
    BigDecimal totalFee;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    Voucher voucher;

    @Column(name = "is_paid")
    boolean isPaid;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "received_amount")
    BigDecimal receivedAmount;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "new_fee")
    BigDecimal newFee;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "changed_amount")
    BigDecimal changedAmount;

    @Column(name = "descriptions")
    List<String> descriptions;

    public void calculateTotalFee() {
        BigDecimal total = reservations.stream()
                .map(Reservation::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (voucher != null && voucher.getStatus().equals(Status.ACTIVE)) {
            total = total.multiply(voucher.getDiscount());
        }
        total = total.setScale(2, RoundingMode.HALF_EVEN);

        this.totalFee = total;
    }

    public void calculateAmountReturn() {
        if (newFee == null) {
            this.changedAmount = receivedAmount.subtract(totalFee);
        } else {
            this.changedAmount = receivedAmount.subtract(newFee);
        }
        this.newFee = receivedAmount.subtract(changedAmount);
    }
}
