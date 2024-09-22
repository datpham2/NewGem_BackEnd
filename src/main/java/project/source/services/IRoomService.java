package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.models.entities.Room;


@Service
public interface IRoomService {
    Page<Room> getAllRoom(PageRequest pageRequest);

    Room getRoomById(Long id);

    Page<Room> getAllRoomByHotelId(Long hotelId, PageRequest pageRequest);

    Room saveRoom(Room room);

    Room updateRoom(Room room, Long id);
}
