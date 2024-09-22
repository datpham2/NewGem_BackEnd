package project.source.models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bills")
public class Bill extends BaseEntity<Long>{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    Set<Reservation> reservations;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "total_fee", nullable = false)
    BigDecimal totalFee;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    Voucher voucher;

    @Column(name = "check_out")
    LocalDate checkOut;

    @Column(name = "is_paid")
    boolean isPaid;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "pay_amount", updatable = false)
    BigDecimal payAmount;

    @Column(name = "descriptions")
    List<String> descriptions;

    public void calculateTotalFee() {
        double total = reservations.stream().mapToDouble(Reservation::getTotalPrice).sum();
        if (voucher != null) {
            total = total * voucher.getDiscount();
        }
        this.totalFee = BigDecimal.valueOf(total);
    }
}
