package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.source.dtos.VoucherDTO;
import project.source.models.entities.Voucher;

/**
 * @Author An Nguyen
 */

public interface IVoucherService {
    Voucher getVoucherById(Long voucherId);
    void saveVoucher(Long hotelId, VoucherDTO voucherDTO);
    Voucher getVoucherById(Long id);
    Page<Voucher> getAllVoucher(Long id, PageRequest pageRequest);
    void updateVoucher(Long id, VoucherDTO voucherDTO);
    void disableVoucher(Long id);
}
