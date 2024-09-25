package project.source.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByHotelId(Long hotelId, PageRequest pageRequest);

    List<Room> findAllByHotelId(Long hotelId);

    Page<Room> findAllByType(RoomType roomType, PageRequest pageable);
}
