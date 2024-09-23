package project.source.requests;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BillRequest {
    long hotelId;
    long userId;
    LocalDate checkOut;
    Long voucherId;
    long reservationId;
}
