package project.source.dtos;


import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDTO {
    Long billId;

    @NonNull
    Long userId;

    UserDTO user;

    @NonNull
    Long hotelId;

    Set<ReservationDTO> reservations;

    BigDecimal totalFee;

    Long voucherId;


    boolean isPaid;

    BigDecimal receivedAmount;

    BigDecimal newFee;

    BigDecimal changedAmount;

    List<String> descriptions;

    LocalDateTime checkOut;

    public static BillDTO fromBill(Bill bill){
        Set<ReservationDTO> reservations = bill.getReservations().stream().map(ReservationDTO::fromReservation).collect(Collectors.toSet());

        bill.getTotalFee();

        BillDTO billDTO = BillDTO.builder()
                .billId(bill.getId())
                .hotelId(bill.getHotel().getId())
                .userId(bill.getUser().getId())
                .user(UserDTO.fromUser(bill.getUser()))
                .totalFee(bill.getTotalFee())
                .reservations(reservations)
                .isPaid(bill.isPaid())
                .newFee(bill.getNewFee())
                .changedAmount(bill.getChangedAmount())
                .receivedAmount(bill.getReceivedAmount())
                .descriptions(bill.getDescriptions())
                .checkOut(bill.getUpdatedAt())
                .build();
        if (bill.getVoucher() != null){
            billDTO.setVoucherId(bill.getVoucher().getId());
        }
        return billDTO;
    }
}
