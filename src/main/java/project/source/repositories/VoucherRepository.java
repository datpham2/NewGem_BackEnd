package project.source.repositories;
/**
 * @Author An Nguyen
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Voucher;


import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findAllById(Long id, PageRequest pageRequest);

    Optional<Voucher> findAllById(Long id);

    List<Voucher> findAllByHotelId(Long hotelId);
}
