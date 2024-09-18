package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Bill;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByRoomId(Long roomId);
}
