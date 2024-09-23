package project.source.services.bill;

import project.source.models.entities.Bill;
import project.source.requests.BillRequest;
import project.source.requests.PayRequest;

import java.util.List;

public interface IBillService {

    Bill addBill(BillRequest billRequest, Long userId, Long hotelId, Long reservationId, Long voucherId);

    Bill getBillById(Long id);

    List<Bill> getAllBillByUserIdAndHotelId(Long userId, Long hotelId);
    Bill payBill(PayRequest payRequest);
}
