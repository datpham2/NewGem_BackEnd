package project.source.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.source.dtos.RoomDTO;
import project.source.services.room.RoomService;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/allRoom")
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/roomById/{id}")
    public ResponseEntity<?> getRoomById(Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/roomByType/{type}")
    public ResponseEntity<?> getRoomByType(String type) {
        return ResponseEntity.ok(roomService.getRoomByType(type));
    }

    @GetMapping("/roomByHotelId/{hotelId}")
    public ResponseEntity<?> getRoomByHotelId(Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomByHotelId(hotelId));
    }

    @PostMapping("/addRoom")
    public ResponseEntity<?> addRoom(RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.addRoom(roomDTO));
    }

    @PostMapping("/updateRoom/{id}")
    public ResponseEntity<?> updateRoom(Long id, RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomDTO));
    }
}
