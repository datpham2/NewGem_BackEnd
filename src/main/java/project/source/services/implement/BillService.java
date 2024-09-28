package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.*;

import project.source.models.enums.Status;
import project.source.repositories.BillRepository;

import project.source.repositories.ReservationRepository;
import project.source.requests.BillRequest;
import project.source.requests.PayRequest;
import project.source.services.IBillService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillService implements IBillService {
    BillRepository billRepository;
    UserService userService;
    HotelService hotelService;
    VoucherService voucherService;
    ReservationService reservationService;
    ReservationRepository reservationRepository;


    @Override
    public Bill addBill(BillRequest billRequest) {
        Hotel hotel = hotelService.getHotelById(billRequest.getHotelId());
        String email = billRequest.getEmail();
        User user  = userService.getUserByEmail(email);

        Bill bill = Bill.builder()
                .user(user)
                .hotel(hotel)
                .isPaid(false)
                .descriptions(new ArrayList<>())
                .build();

        if (billRequest.getVoucherId() != null) {
            Voucher voucher = voucherService.getVoucherById(billRequest.getVoucherId());
            if (voucher.getHotel() == bill.getHotel()){
                bill.setVoucher(voucher);
            }
            else {
                bill.getDescriptions().add("This voucher do not apply for this hotel");
            }
        }

        Set<Reservation> reservations = reservationService.getAllReservationByUserIdAndHotelId(user.getId(),hotel.getId());
        Set<Reservation> activeReservations = reservations.stream()
                .filter(reservation -> reservation.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toSet());

        if (activeReservations.isEmpty()) {
            return null;
        }

        reservations.forEach(reservation -> {
            reservation.setStatus(Status.INACTIVE);
            reservation.setBill(bill);
        });

        bill.setReservations(activeReservations);
        bill.calculateTotalFee();
        Bill newBill = billRepository.save(bill);
        reservationRepository.saveAll(activeReservations);

        return newBill;
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElseThrow(()-> new NotFoundException("Can not find bill with id: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Bill payBill(PayRequest payRequest){
        Bill bill = getBillById(payRequest.getBillId());
        bill.setPaid(true);
        bill.setReceivedAmount(payRequest.getReceivedAmount());
        if (payRequest.getNewFee() != null){
            bill.setNewFee(payRequest.getNewFee());
        }
        bill.calculateAmountReturn();
        for (Reservation reservation : bill.getReservations()){
            reservation.setStatus(Status.INACTIVE);
        }
        for (String description : payRequest.getDescriptions()){
            bill.getDescriptions().add(description);
        }
        return billRepository.save(bill);
    }

    @Override
    public List<Bill> getAllBillByHotelId(Long hotelId) {
        return billRepository.findAllByHotelId(hotelId);
    }
}
