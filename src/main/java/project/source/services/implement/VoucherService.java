package project.source.services.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import project.source.dtos.VoucherDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Voucher;
import project.source.models.enums.Status;
import project.source.repositories.VoucherRepository;
import project.source.services.IVoucherService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService {
    private final VoucherRepository voucherRepository;
    private final HotelService hotelService;


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Voucher getVoucherById(Long Id){
        return voucherRepository.findById(Id).orElseThrow(()-> new NotFoundException("Voucher"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveVoucher(Long hotelId, VoucherDTO voucherDTO) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if(hotel == null){
            throw new NotFoundException("Hotel is not Exist with id = " + hotelId);
        }else {
            Voucher voucher = Voucher.builder()
                    .hotel(hotel)
                    .startDate(voucherDTO.getStartDate())
                    .endDate(voucherDTO.getEndDate())
                    .status(Status.ACTIVE)
                    .discount(voucherDTO.getDiscount())
                    .build();
            voucherRepository.save(voucher);
        }
    }

//    public void checkVoucher(Voucher voucher){
//        if (voucher.getEndDate().isBefore(LocalDate.now())){
//            voucher.setStatus(Status.INACTIVE);
//            voucherRepository.save(voucher);
//        }
//    }



    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<Voucher> getAllVoucher(Long id, PageRequest pageRequest) {
        return voucherRepository.findAllById(id, pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateVoucher(Long id, VoucherDTO voucherDTO) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(()-> new NotFoundException("Voucher not found by Id = "+ id));
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setDiscount(voucherDTO.getDiscount());
        voucher.setEndDate(voucherDTO.getEndDate());
        voucher.setStartDate(voucherDTO.getStartDate());
        voucher.setHotel(hotelService.getHotelById(id));
        voucherRepository.save(voucher);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void disableVoucher(Long id) {
        Voucher voucher =  voucherRepository.findById(id).orElseThrow(()-> new NotFoundException("Voucher not found by Id = "+ id));
        if(voucher.getStatus() == Status.ACTIVE){
            voucher.setStatus(Status.INACTIVE);
        }else {
            voucher.setStatus(Status.ACTIVE);
        }
        voucherRepository.save(voucher);
    }


}
