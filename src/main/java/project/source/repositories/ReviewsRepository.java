package project.source.repositories;
/**
 * @autor An Nguyen
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews,Long> {
    Page<Reviews> findAllById(Long id, PageRequest pageRequest);
}
