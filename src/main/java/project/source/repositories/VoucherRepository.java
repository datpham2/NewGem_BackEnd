package project.source.repositories;
/**
 * @Author An Nguyen
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findAllById(Long id, PageRequest pageRequest);
}
