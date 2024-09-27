package project.source.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.RoomDTO;
import project.source.models.entities.Room;

import project.source.models.enums.RoomType;
import project.source.models.enums.Status;
import project.source.respones.ApiResponse;

import project.source.respones.PageResponse;
import project.source.services.implement.RoomService;


import java.math.BigDecimal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Room", description = "Operations related to rooms")
public class RoomController {
    RoomService roomService;


    @GetMapping("/allRoom/{hotelId}")
    @Operation(
            method = "GET",
            summary = "Get all room by hotel id",
            description = "Send a request to get all the rooms of the hotel with the path variable hotelId")
    public ResponseEntity<ApiResponse> getAllRoomByHotelId(
            @PathVariable(value = "hotelId") Long hotelId,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = "Page must be more than zero") int page,
            @RequestParam(value = "size", defaultValue = "8") @Min(value = 1, message = "Page must be more than one") int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Room> rooms = roomService.getAllRoomByHotelId(hotelId, pageRequest);
        List<RoomDTO> items = rooms.stream().map(RoomDTO::fromRoom).toList();

        PageResponse<List<RoomDTO>> response = PageResponse.<List<RoomDTO>>builder()
                .totalPage(rooms.getTotalPages())
                .pageNo(rooms.getNumber())
                .pageSize(rooms.getSize())
                .items(items)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all rooms successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "GET",
            summary = "Add room by id",
            description = "Send a request to get the room with the path variable id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable(value = "id")Long id){
        Room room = roomService.getRoomById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get room successfully by id = "+ id)
                .data(RoomDTO.fromRoom(room))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "GET",
            summary = "Search room matching the criteria",
            description = "Send a request to get all the rooms matching the criterias(hotel id, room type, max price")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchRoom(@RequestParam(name = "hotel", required = false) Long hotelId,
                                                   @RequestParam(name = "type", required = false) RoomType type,
                                                   @RequestParam(name = "max", required = false) BigDecimal maxPrice,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "5") int size){
        Page<Room> rooms = roomService.getRoomByHotelAndTypeAndPrice(hotelId,type,maxPrice,PageRequest.of(page,size));
        List<RoomDTO> roomDTOList = rooms.getContent().stream().map(RoomDTO::fromRoom).toList();

        PageResponse<List<RoomDTO>> response = PageResponse.<List<RoomDTO>>builder()
                .totalPage(rooms.getTotalPages())
                .pageNo(rooms.getNumber())
                .pageSize(rooms.getSize())
                .items(roomDTOList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Search successfully")
                .data(response)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(
            method = "POST",
            summary = "Add a room",
            description = "Post a request with hotelId, roomNumber, price, type, guests")
    @PostMapping("/createRoom")
    public ResponseEntity<ApiResponse> addRoom(@Valid @RequestBody RoomDTO roomDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't create room")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            Room newRoom = roomService.saveRoom(roomDTO);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Create successfully")
                    .data(RoomDTO.fromRoom(newRoom))
                    .status(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @Operation(
            method = "PUT",
            summary = "Update the room with the pariable id",
            description = "Send a request update the roomNumber, price, type and guests")
    @PutMapping("/updateRoom/{id}")
    public ResponseEntity<ApiResponse> updateRoom(@Valid @RequestBody RoomDTO roomDTO, @PathVariable(value = "id")Long id,
                                                   BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't update room")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            Room updatedRoom = roomService.updateRoom(roomDTO,id);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Update successfully")
                    .data(RoomDTO.fromRoom(updatedRoom))
                    .status(HttpStatus.ACCEPTED.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @Operation(
            method = "PATCH",
            summary = "Change the status of the room",
            description = "Send a request change the status of the room")
    @PatchMapping("/changeStatus/{id}")
    public ResponseEntity<ApiResponse> disableHotel(@PathVariable(value = "id")Long id){
        Status status = roomService.changeStatus(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(status == Status.ACTIVE ? "Activate room successfully" : "Disable room successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get all rooms by type",
            description = "Send a request to get all the rooms by the type")
    @GetMapping("/type")
    public ResponseEntity<ApiResponse> getAllRoomByType(
            @RequestParam(value = "type", required = false) RoomType roomType,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = "Page must be more than zero") int page,
            @RequestParam(value = "size", defaultValue = "8") @Min(value = 1, message = "Page must be more than one") int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Room> rooms = roomService.getAllRoomByType(roomType,pageRequest);
        List<RoomDTO> items = rooms.stream().map(RoomDTO::fromRoom).toList();

        PageResponse<List<RoomDTO>> response = PageResponse.<List<RoomDTO>>builder()
                .totalPage(rooms.getTotalPages())
                .pageNo(rooms.getNumber())
                .pageSize(rooms.getSize())
                .items(items)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all rooms successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
