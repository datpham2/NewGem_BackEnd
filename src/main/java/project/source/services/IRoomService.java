package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.RoomDTO;
import project.source.models.entities.Hotel;
import project.source.models.entities.Room;
import project.source.models.enums.RoomType;
import project.source.models.enums.Status;

import java.math.BigDecimal;


@Service
public interface IRoomService {
    Page<Room> getAllRoom(PageRequest pageRequest);

    Room getRoomById(Long id);

    Page<Room> getAllRoomByHotelId(Long hotelId, PageRequest pageRequest);

    Room saveRoom(RoomDTO roomDTO);

    Status changeStatus(Long id);

    Room updateRoom(RoomDTO roomDTO, Long id);

    Page<Room> getAllRoomByType(RoomType roomType,PageRequest pageRequest);

    Page<Room> getRoomByHotelAndTypeAndPriceAndStatus(Long hotelId, RoomType type, BigDecimal maxPrice, Status status, PageRequest pageRequest);
}
