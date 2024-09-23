package project.source.services.room;

import project.source.dtos.RoomDTO;
import project.source.models.entities.Room;

import java.util.List;

public interface IRoomService {
    Room addRoom(Room room, Long hotelId);
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    Room updateRoom(Long id, RoomDTO roomDTO);
    void deleteRoom(Long id);

    Object getRoomByType(String type);

    Object getRoomByHotelId(Long hotelId);
}
