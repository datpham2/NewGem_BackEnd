package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Bill;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {
    List<Bill> findAllByUserIdAndHotelId(Long userId, Long hotelId);

    List<Bill> findAllByUserIdAndHotelIdAndIsPaid(Long userId, Long hotelId, boolean isPaid);

    List<Bill> findAllByHotelId(Long hotelId);

    List<Bill> findAllByHotelIdAndIsPaid(Long hotelId, boolean isPaid);
}
