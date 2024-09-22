package project.source.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDTO {
    @NonNull
    Long userId;

    @NonNull
    Long hotelId;

    Set<ReservationDTO> reservations;

    BigDecimal totalFee;

    Long voucherId;

    @NonNull
    LocalDate checkOut;

    boolean isPaid;

    BigDecimal receivedAmount;

    BigDecimal newFee;

    BigDecimal changedAmount;

    List<String> descriptions;

    public static BillDTO fromBill(Bill bill){
        Set<ReservationDTO> reservations = bill.getReservations().stream().map(ReservationDTO::fromReservation).collect(Collectors.toSet());

        BillDTO billDTO = BillDTO.builder()
                .hotelId(bill.getHotel().getId())
                .userId(bill.getUser().getId())
                .totalFee(bill.getTotalFee())
                .checkOut(bill.getCheckOut())
                .reservations(reservations)
                .isPaid(bill.isPaid())
                .newFee(bill.getNewFee())
                .changedAmount(bill.getChangedAmount())
                .receivedAmount(bill.getReceivedAmount())
                .descriptions(bill.getDescriptions())
                .build();
        if (bill.getVoucher() != null){
            billDTO.setVoucherId(bill.getVoucher().getId());
        }
        return billDTO;
    }
}
