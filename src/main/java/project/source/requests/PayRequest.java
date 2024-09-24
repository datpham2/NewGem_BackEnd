package project.source.requests;

import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayRequest {
    @NonNull
    Long billId;

    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    BigDecimal receivedAmount;

    BigDecimal newFee;

    List<String> descriptions;
}
