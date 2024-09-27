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
import project.source.models.enums.City;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("select s from Hotel s where s.name like ?1%")
    Page<Hotel> searchByName(String name, PageRequest pageRequest);

    Optional<Hotel> findByName(String name);

    List<Hotel> findByCity(City city);

    boolean existsByNameAndCity(String name, City city);

    boolean existsByLocationAndCity(String location, City city);

    @Query("SELECT h FROM Hotel h WHERE (:city IS NULL OR h.city = :city) " +
            "AND (:maxPrice IS NULL OR h.maxPrice <= :maxPrice) " +
            "AND (:minPrice IS NULL OR h.minPrice >= :minPrice)" +
            "AND (:status IS NULL OR h.status = :status)")
    Page<Hotel> findByCityAndPriceRange(City city, BigDecimal minPrice, BigDecimal maxPrice, Status status, PageRequest pageRequest);

    @Query("SELECT COUNT(h) FROM Hotel h")
    long countAllHotels();
}
