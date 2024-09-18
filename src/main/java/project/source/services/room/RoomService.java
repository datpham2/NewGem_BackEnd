package project.source.services.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.source.dtos.RoomDTO;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.repositories.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    @Override
    public Room addRoom(RoomDTO roomDTO) {
        Room room = Room.builder()
                .price(roomDTO.getPrice())
                .type(RoomType.valueOf(roomDTO.getType()))
                .guests(roomDTO.getGuests())
                .build();
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
}
