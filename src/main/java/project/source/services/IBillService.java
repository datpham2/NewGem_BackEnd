package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.HotelDTO;
import project.source.models.entities.Bill;
import project.source.requests.BillRequest;
import project.source.requests.PayRequest;

import java.util.List;


@Service
public interface IBillService {
    Bill addBill(BillRequest billRequest);

    Page<Bill> getAllBill(PageRequest pageRequest);

    Bill getBillById(Long id);

    List<Bill> getAllBillByUserIdAndHotelId(Long userId, Long hotelId);

    void updateBill(Bill bill, Long id);

    void payBill(PayRequest payRequest);
}
