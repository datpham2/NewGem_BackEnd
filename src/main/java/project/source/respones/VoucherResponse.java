package project.source.respones;
/**
 * @Author An Nguyen
 */
import lombok.*;
import project.source.models.entities.Voucher;

import java.util.List;

@Setter
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponse {
    private List<Voucher> voucherList;
    private int totalPage;
}
