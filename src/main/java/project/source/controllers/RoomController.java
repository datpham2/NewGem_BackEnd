package project.source.controllers;

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
import project.source.respones.ApiResponse;

import project.source.respones.PageResponse;
import project.source.services.implement.RoomService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoomController {
    RoomService roomService;

    @GetMapping("/allRoom/{hotelId}")
    public ResponseEntity<ApiResponse> getAllRoomByHotelId(
            @PathVariable(value = "hotelId") Long hotelId,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = "Page must be more than zero") int page,
            @RequestParam(value = "size", defaultValue = "8") @Min(value = 1, message = "Page must be more than one") int size,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(null)
                    .data(errors)
                    .build());
        }

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

    @PostMapping("/createRoom")
    public ResponseEntity<ApiResponse> addRoom(@Valid @RequestBody Room room, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't create room")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            Room newRoom = roomService.saveRoom(room);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Create successfully")
                    .data(RoomDTO.fromRoom(newRoom))
                    .status(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @PutMapping("/updateRoom/{id}")
    public ResponseEntity<ApiResponse> updateRoom(@Valid @RequestBody Room room, @PathVariable(value = "id")Long id,
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
            Room updatedRoom = roomService.updateRoom(room,id);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Update successfully")
                    .data(RoomDTO.fromRoom(updatedRoom))
                    .status(HttpStatus.ACCEPTED.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

//    @PatchMapping("/delete/{id}")
//    public ResponseEntity<ApiResponse> disableHotel(@PathVariable(value = "id")Long id){
//        hotelService.disableHotel(id);
//        ApiResponse apiResponse = ApiResponse.builder()
//                .status(HttpStatus.OK.value())
//                .message("Disable successfully")
//                .data(null)
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }
}
