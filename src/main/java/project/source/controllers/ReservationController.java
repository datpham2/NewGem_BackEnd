package project.source.controllers;

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
import project.source.dtos.ReservationDTO;
import project.source.exceptions.ConflictException;
import project.source.models.entities.Reservation;
import project.source.respones.ApiResponse;
import project.source.respones.PageResponse;
import project.source.services.implement.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Tag(name = "Reservation", description = "Operations related to reservations")
public class ReservationController {
    ReservationService reservationService;

    @GetMapping("/allRooms/{roomId}")
    public ResponseEntity<ApiResponse> getAllRoomByRoomId(
            @PathVariable(value = "roomId") Long roomId,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = "Page must be more than zero") int page,
            @RequestParam(value = "size", defaultValue = "8") @Min(value = 1, message = "Page must be more than one") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationService.getAllReservationByRoomId(roomId, pageRequest);

        List<ReservationDTO> items = reservations.getContent().stream().map(ReservationDTO::fromReservation).toList();

        PageResponse<List<ReservationDTO>> response = PageResponse.<List<ReservationDTO>>builder()
                .totalPage(reservations.getTotalPages())
                .pageNo(reservations.getNumber())
                .pageSize(reservations.getSize())
                .items(items)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all reservations successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/allUsers/{userId}")
    public ResponseEntity<ApiResponse> getAllRoomByUserId(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = "Page must be more than zero") int page,
            @RequestParam(value = "size", defaultValue = "8") @Min(value = 1, message = "Page must be more than one") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        List<Reservation> reservations = reservationService.getAllReservationByUserId(userId, pageRequest);
        List<ReservationDTO> response = reservations.stream().map(ReservationDTO::fromReservation).collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all reservations successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable(value = "id")Long id){
        Reservation reservation = reservationService.getReservationById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get reservation successfully by id = "+ id)
                .data(ReservationDTO.fromReservation(reservation))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/bookRoom")
    public ResponseEntity<ApiResponse> makeReservation(@Valid @RequestBody ReservationDTO reservationDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't create reservation")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            Reservation newReservation = reservationService.saveReservation(reservationDTO);
                ApiResponse apiResponse = ApiResponse.builder()
                        .message("Create successfully")
                        .data(ReservationDTO.fromReservation(newReservation))
                        .status(HttpStatus.CREATED.value())
                        .build();
                return ResponseEntity.ok(apiResponse);
            }
        }

    @PostMapping("/bookRooms")
    public ResponseEntity<ApiResponse> makeReservations(@Valid @RequestBody List<ReservationDTO> reservationDTOs, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't create reservation")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            List<Reservation> newReservations = reservationService.saveReservations(reservationDTOs);
            List<ReservationDTO> data = newReservations.stream().map(ReservationDTO::fromReservation).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Create successfully")
                    .data(data)
                    .status(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable(value = "id")Long id){
        reservationService.deleteReservation(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Delete reservation successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/cancels")
    public ResponseEntity<ApiResponse> cancelReservations(@RequestBody List<Long> ids) {
        reservationService.deleteReservations(ids);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Deleted reservations successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
