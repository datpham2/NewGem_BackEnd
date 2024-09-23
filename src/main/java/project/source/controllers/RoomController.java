package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.RoomDTO;
import project.source.models.entities.Room;
import project.source.respones.ApiResponse;
import project.source.services.room.RoomService;

import java.util.List;

@Slf4j
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

    @PostMapping("/createRoom")
    public ResponseEntity<ApiResponse> addRoom(@Valid @RequestBody Room room, BindingResult result){
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .status(400)
                    .message("Validation failed")
                    .data(errors)
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        } else {
            Room newRoom = roomService.addRoom(room, room.getHotel().getId());
            ApiResponse apiResponse = ApiResponse.builder()
                    .status(201)
                    .message("Room created successfully")
                    .data(newRoom)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @PostMapping("/updateRoom/{id}")
    public ResponseEntity<?> updateRoom(Long id, RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomDTO));
    }
}
