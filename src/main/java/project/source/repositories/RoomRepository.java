package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Object findByType(RoomType roomType);

    Room findByHotelIdAndType(Long hotelId, RoomType roomType);
}
