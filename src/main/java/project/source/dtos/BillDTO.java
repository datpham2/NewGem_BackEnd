package project.source.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.source.models.entities.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class BillDTO {
    Long userId;
    Long hotelId;
    Set<ReservationDTO> reservations;
    Long voucherId;
    boolean isPaid;
    BigDecimal totalFee;
    Double totalPrice;

    public static BillDTO fromBill(Bill bill){
        Set<ReservationDTO> reservations = bill.getReservations().stream().map(ReservationDTO::fromReservation).collect(Collectors.toSet());

        return BillDTO.builder()
                .hotelId(bill.getHotel().getId())
                .userId(bill.getUser().getId())
                .totalFee(bill.getTotalFee())
                .reservations(reservations)
                .isPaid(bill.isPaid())
                .build();
    }
}
