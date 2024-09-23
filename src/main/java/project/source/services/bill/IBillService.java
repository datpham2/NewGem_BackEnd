package project.source.services.bill;

import project.source.models.entities.Bill;
import project.source.requests.BillRequest;
import project.source.requests.PayRequest;

import java.util.List;

public interface IBillService {
    Bill addBill(BillRequest billRequest);
    Bill getBillById(Long id);

    List<Bill> getAllBillByUserIdAndHotelId(Long userId, Long hotelId);
    Bill payBill(PayRequest payRequest);
}
