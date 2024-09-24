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


    Bill getBillById(Long id);

    List<Bill> getAllBillByUserIdAndHotelId(Long userId, Long hotelId);

    Bill payBill(PayRequest payRequest);
}
