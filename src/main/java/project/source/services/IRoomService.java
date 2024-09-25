package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.RoomDTO;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;


@Service
public interface IRoomService {
    Page<Room> getAllRoom(PageRequest pageRequest);

    Room getRoomById(Long id);

    Page<Room> getAllRoomByHotelId(Long hotelId, PageRequest pageRequest);

    Room saveRoom(RoomDTO roomDTO);

    Room updateRoom(RoomDTO roomDTO, Long id);

    Page<Room> getAllRoomByType(RoomType roomType,PageRequest pageRequest);
}
