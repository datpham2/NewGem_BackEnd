package project.source.repositories;
/**
 * @autor An Nguyen
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.source.models.entities.Hotel;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("select s from Hotel s where s.name like ?1%")
    Page<Hotel> searchByName(String name, PageRequest pageRequest);

    Optional<Hotel> findByName(String name);

    boolean existsByName(String name);

    boolean existsByLocation(String location);
}
