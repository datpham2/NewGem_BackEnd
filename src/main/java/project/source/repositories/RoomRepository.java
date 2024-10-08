package project.source.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByHotelId(Long hotelId, PageRequest pageRequest);

    List<Room> findAllByHotelId(Long hotelId);

    @Query("SELECT r FROM Room r WHERE (:type IS NULL OR r.type = :type)")
    Page<Room> findAllByType(RoomType type, PageRequest pageRequest);

    @Query("SELECT r FROM Room r WHERE (:hotel IS NULL OR r.hotel.id = :hotel) " +
            "AND (:type IS NULL OR r.type = :type) " +
            "AND (:maxPrice IS NULL OR r.price <= :maxPrice)")
    Page<Room> findByHotelAndTypeAndPrice(Long hotel, RoomType type, BigDecimal maxPrice, PageRequest pageRequest);

    @Query("SELECT r FROM Room r WHERE (:hotel IS NULL OR r.hotel.id = :hotel) " +
            "AND (:type IS NULL OR r.type = :type) " +
            "AND (:maxPrice IS NULL OR r.price <= :maxPrice) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<Room> findByHotelAndTypeAndPriceAndStatus(Long hotel, RoomType type, BigDecimal maxPrice, Status status, PageRequest pageRequest);
}
