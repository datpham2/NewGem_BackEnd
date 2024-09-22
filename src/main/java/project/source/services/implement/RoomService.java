package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Room;
import project.source.repositories.RoomRepository;
import project.source.services.IRoomService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoomService implements IRoomService {
    RoomRepository roomRepository;
    HotelService hotelService;

    @Override
    public Page<Room> getAllRoom(PageRequest pageRequest) {
        return roomRepository.findAll(pageRequest);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(()-> new NotFoundException("Can not find room with id: " + id));
    }

    @Override
    public Page<Room> getAllRoomByHotelId(Long hotelId, PageRequest pageRequest) {
        hotelService.getHotelById(hotelId);
        return roomRepository.findAllByHotelId(hotelId,pageRequest);
    }

    @Override
    public Room saveRoom(Room room) {
        Hotel hotel = hotelService.getHotelById(room.getHotel().getId());
        hotel.setNoRooms(hotel.getRooms().size());
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Room room, Long id) {
        Room updatedRoom = getRoomById(id);
        updatedRoom.setRoomNumber(room.getRoomNumber());
        updatedRoom.setGuests(room.getGuests());
        updatedRoom.setType(room.getType());
        updatedRoom.setPrice(room.getPrice());

        return roomRepository.save(updatedRoom);
    }
}
