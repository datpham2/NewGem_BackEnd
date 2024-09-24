package project.source.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;



import java.time.LocalDate;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillRequest {
    @NotNull
    long hotelId;

    @NotNull
    long userId;

    @NotNull
    LocalDate checkOut;

    Long voucherId;
    long reservationId;
}
