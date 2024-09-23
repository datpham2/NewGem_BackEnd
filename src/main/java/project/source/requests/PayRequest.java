package project.source.requests;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PayRequest {
    Long billId;

    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    BigDecimal receivedAmount;

    BigDecimal newFee;

    List<String> descriptions;
}
