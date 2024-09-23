package project.source.services.bill;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.source.models.entities.Bill;
import project.source.models.entities.Hotel;
import project.source.models.entities.Reservation;
import project.source.repositories.BillRepository;
import project.source.repositories.UserRepository;
import project.source.requests.BillRequest;
import project.source.requests.PayRequest;
import project.source.services.implement.HotelService;
import project.source.services.implement.UserService;
import project.source.services.implement.VoucherService;
import project.source.services.reservation.ReservationService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BillService implements IBillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final HotelService hotelService;
    private final VoucherService voucherService;
    private final ReservationService reservationService;
    private final UserService userService;


    @Override
    public Bill addBill(BillRequest billRequest) {
        Hotel hotel = new Hotel();
        long userId = billRequest.getUserId();
        Bill bill = Bill.builder()
                .isPaid(false)
                .checkOut(billRequest.getCheckOut())
                .hotel(hotel)
                .user(userRepository.findById(userId).orElse(null))
                .build();
        Long voucherId = billRequest.getVoucherId();
        if (voucherId != null) {
            bill.setVoucher(voucherService.getVoucherById(voucherId));
        }

        Set<Reservation> reservations = reservationService.getAllReservationByUserId(userId);
        reservations = reservations.stream()
                .filter(reservation -> reservation.getRoom().getHotel().equals(hotel))
                .collect(Collectors.toSet());

        bill.setReservations(reservations);

        bill.calculateTotalFee();
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    @Override
    public List<Bill> getAllBillByUserIdAndHotelId(Long userId, Long hotelId) {
        userService.getUserById(userId);
        hotelService.getHotelById(hotelId);
        List<Bill> bills = billRepository.findAllBillByUserIdAndHotelId(userId, hotelId);
        bills.forEach(Bill::calculateTotalFee);
        return bills;
    }

    @Override
    public Bill payBill(PayRequest payRequest){
        Bill bill = billRepository.findById(payRequest.getBillId()).orElse(null);
        if (bill == null){
            return null;
        }
        bill.setPaid(true);
        return billRepository.save(bill);
    }
}
