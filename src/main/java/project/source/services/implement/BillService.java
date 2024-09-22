package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Bill;
import project.source.models.entities.Hotel;
import project.source.models.entities.Reservation;

import project.source.repositories.BillRepository;

import project.source.requests.BillRequest;
import project.source.requests.PayRequest;
import project.source.services.IBillService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillService implements IBillService {
    BillRepository billRepository;
    UserService userService;
    HotelService hotelService;
    VoucherService voucherService;
    ReservationService reservationService;


    @Override
    public Bill addBill(BillRequest billRequest) {
        Hotel hotel = hotelService.getHotelById(billRequest.getHotelId());
        long userId = billRequest.getUserId();

        Bill bill = Bill.builder()
                .user(userService.getUserById(userId))
                .hotel(hotel)
                .voucher(voucherService.getVoucherById(billRequest.getVoucherId()))
                .isPaid(false)
                .checkOut(billRequest.getCheckOut())
                .payAmount(null)
                .build();

        Set<Reservation> reservations = reservationService.getAllReservationByUserId(userId);

        reservations = reservations.stream()
                .filter(reservation -> !reservation.getRoom().getHotel().equals(hotel))
                        .collect(Collectors.toSet());

        bill.setReservations(reservations);
        bill.calculateTotalFee();

        return billRepository.save(bill);
    }


    @Override
    public Page<Bill> getAllBill(PageRequest pageRequest) {
        Page<Bill> bills = billRepository.findAll(pageRequest);
        for (Bill bill : bills){
            bill.calculateTotalFee();
        }
        return bills;
    }

    @Override
    public Bill getBillById(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(()-> new NotFoundException("Can not find bill with id: " + id));
        bill.calculateTotalFee();
        return bill;
    }

    @Override
    public List<Bill> getAllBillByUserIdAndHotelId(Long userId, Long hotelId) {
        userService.getUserById(userId);
        hotelService.getHotelById(hotelId);
        List<Bill> bills = billRepository.findAllByUserIdAndHotelId(userId, hotelId);
        for (Bill bill : bills){
            bill.calculateTotalFee();
        }
        return bills;
    }

    @Override
    public void updateBill(Bill bill, Long id) {
        Bill updateBill = getBillById(id);
        updateBill.setVoucher(bill.getVoucher());
        for (String description : bill.getDescriptions()){
            updateBill.getDescriptions().add(description);
        }
        billRepository.save(updateBill);
    }

    @Override
    public void payBill(PayRequest payRequest){
        Bill bill = getBillById(payRequest.getBillId());
        bill.setPaid(true);
        bill.setPayAmount(payRequest.getPayAmount());
        for (String description : payRequest.getDescriptions()){
            bill.getDescriptions().add(description);
        }
        billRepository.save(bill);
    }
}
