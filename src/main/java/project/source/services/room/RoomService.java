package project.source.services.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.source.dtos.RoomDTO;
import project.source.models.entities.Hotel;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.repositories.RoomRepository;
import project.source.services.implement.HotelService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    @Override
    public Room addRoom(Room room, Long hotelId) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        hotel.setNoRooms(hotel.getNoRooms());
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room updateRoom(Long id, RoomDTO roomDTO) {
        Room room = getRoomById(id);
        room.setPrice(roomDTO.getPrice());
        room.setType(RoomType.valueOf(roomDTO.getType()));
        room.setGuests(roomDTO.getGuests());
        roomRepository.save(room);
        return room;
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Object getRoomByType(String type) {
        return roomRepository.findByType(RoomType.valueOf(type));
    }

    @Override
    public Object getRoomByHotelId(Long hotelId) {
        return null;
    }
}
