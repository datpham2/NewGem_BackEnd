package project.source.dtos;
/**
 * @Author An Nguyen
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import project.source.models.entities.Voucher;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class VoucherDTO {
    private Long voucherId;

    @DecimalMin(value = "0.5", message = "Discount must be at least 0.5")
    @DecimalMax(value = "1.0", message = "Discount must not exceed 1.0")
    private BigDecimal discount;

    @FutureOrPresent(message = "Start Date must be one day in future")
    private LocalDate startDate;

    @FutureOrPresent(message = "End Date must be one day in future")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonProperty(value = "hotel_id")
    private Long hotelId;


    public static VoucherDTO fromVoucher(Voucher voucher){
        return VoucherDTO.builder()
                .voucherId(voucher.getId())
                .discount(voucher.getDiscount())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .status(voucher.getStatus())
                .hotelId(voucher.getHotel().getId())
                .build();
    }
}
