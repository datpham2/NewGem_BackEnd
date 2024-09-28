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
import project.source.models.enums.RoomType;
import project.source.models.enums.Status;
import project.source.repositories.RoomRepository;
import project.source.services.IRoomService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    @PreAuthorize("hasRole('ADMIN')")
    public Room saveRoom(RoomDTO roomDTO) {
        Hotel hotel = hotelService.getHotelById(roomDTO.getHotelId());
        existed(hotel,roomDTO.getRoomNumber());
        Room room = roomRepository.save(Room.builder()
                .price(roomDTO.getPrice())
                .guests(roomDTO.getGuests())
                .type(roomDTO.getType())
                .roomNumber(roomDTO.getRoomNumber())
                .hotel(hotel)
                .status(Status.ACTIVE)
                .build());
        hotel.setNoRooms(hotel.getRooms().size());
        hotel.setPrices();
        hotelService.saveHotel(hotel);
        return room;
    }

    public void existed(Hotel hotel, int roomNumber){
        List<Room> rooms = roomRepository.findAllByHotelId(hotel.getId());
        for (Room room : rooms){
            if (roomNumber == room.getRoomNumber()){
                throw new ExistedException("Room " + roomNumber + " of hotel " + hotel.getName());
            }
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Room updateRoom(RoomDTO roomDTO, Long id) {
        Room updatedRoom = getRoomById(id);
        Hotel hotel = hotelService.getHotelById(updatedRoom.getHotel().getId());
        existed(hotel,roomDTO.getRoomNumber());
        updatedRoom.setRoomNumber(roomDTO.getRoomNumber());
        updatedRoom.setGuests(roomDTO.getGuests());
        updatedRoom.setType(roomDTO.getType());
        updatedRoom.setPrice(roomDTO.getPrice());
        hotel.setPrices();
        hotelService.saveHotel(hotel);
        return roomRepository.save(updatedRoom);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Status changeStatus(Long hotelId) {
        Room room = getRoomById(hotelId);
        room.setStatus(room.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
        return roomRepository.save(room).getStatus();
    }

    @Override
    public Page<Room> getAllRoomByType(RoomType roomType, PageRequest pageRequest) {
        return roomRepository.findAllByType(roomType, pageRequest);
    }

    @Override
    public Page<Room> getRoomByHotelAndTypeAndPriceAndStatus(Long hotelId, RoomType type, BigDecimal maxPrice, Status status, PageRequest pageRequest) {
        if (hotelId != null) {
            if (!hotelService.existedById(hotelId)) {
                throw new NotFoundException("Hotel with ID " + hotelId + " does not exist.");
            }
            return roomRepository.findByHotelAndTypeAndPriceAndStatus(hotelId, type, maxPrice, status, pageRequest);
        }
        return roomRepository.findByHotelAndTypeAndPriceAndStatus(null, type, maxPrice, status, pageRequest);
    }
}
