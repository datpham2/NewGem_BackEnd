package project.source.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.VoucherDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Voucher;
import project.source.repositories.VoucherRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService{

    private final VoucherRepository voucherRepository;
    private final HotelService hotelService;

    @Override
    public void saveVoucher(Long hotelId, VoucherDTO voucherDTO) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if(hotel == null){
            throw new NotFoundException("Hotel is not Exist with id = " + hotelId);
        }else {
            saveVoucher(hotelId, voucherDTO);
        }
    }

    @Override
    public Page<Voucher> getAllVoucher(Long id, PageRequest pageRequest) {
        return voucherRepository.findAllById(id, pageRequest);
    }

    @Override
    public void updateVoucher(Long id, VoucherDTO voucherDTO) {
        Voucher voucher = (Voucher) voucherRepository.findAllById(Collections.singleton(id));
        voucher = Voucher.builder()
                .active(voucherDTO.isActive())
                .discount(voucherDTO.getDiscount())
                .endDate(voucherDTO.getEndDate())
                .startDate(voucherDTO.getStartDate())
                .hotel(hotelService.getHotelById(id))
                .build();
        voucherRepository.save(voucher);
    }
}
