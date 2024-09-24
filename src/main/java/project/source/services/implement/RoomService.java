package project.source.services.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import project.source.dtos.RoomDTO;
import project.source.exceptions.ConflictException;
import project.source.exceptions.ExistedException;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Room;
import project.source.repositories.RoomRepository;
import project.source.services.IRoomService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoomService implements IRoomService {
    RoomRepository roomRepository;
    HotelService hotelService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<Room> getAllRoom(PageRequest pageRequest) {
        return roomRepository.findAll(pageRequest);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(()-> new NotFoundException("Can not find room with id: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<Room> getAllRoomByHotelId(Long hotelId, PageRequest pageRequest) {
        hotelService.getHotelById(hotelId);
        return roomRepository.findAllByHotelId(hotelId,pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Room saveRoom(RoomDTO roomDTO) {
        Hotel hotel = hotelService.getHotelById(roomDTO.getHotelId());
        existed(hotel,roomDTO.getRoomNumber());
        hotel.setNoRooms(hotel.getRooms().size());
        Room room = Room.builder()
                .price(roomDTO.getPrice())
                .guests(roomDTO.getGuests())
                .type(roomDTO.getType())
                .roomNumber(roomDTO.getRoomNumber())
                .hotel(hotel)
                .build();
        return roomRepository.save(room);
    }

    public void existed(Hotel hotel, int roomNumber){
        List<Room> rooms = roomRepository.findAllByHotelId(hotel.getId());
        for (Room room : rooms){
            if (roomNumber == room.getRoomNumber()){
                throw new ExistedException("Room " + roomNumber + " of hotel" + hotel.getName() + " existed");
            }
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Room updateRoom(RoomDTO roomDTO, Long id) {
        Room updatedRoom = getRoomById(id);
        Hotel hotel = hotelService.getHotelById(roomDTO.getHotelId());
        existed(hotel,roomDTO.getRoomNumber());
        if (!Objects.equals(updatedRoom.getHotel().getId(), roomDTO.getHotelId())){
            throw new ConflictException("Can not change room to another hotel");
        }
        updatedRoom.setRoomNumber(roomDTO.getRoomNumber());
        updatedRoom.setGuests(roomDTO.getGuests());
        updatedRoom.setType(roomDTO.getType());
        updatedRoom.setPrice(roomDTO.getPrice());

        return roomRepository.save(updatedRoom);
    }
}
