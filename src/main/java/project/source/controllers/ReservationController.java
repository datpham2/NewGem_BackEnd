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
import project.source.models.entities.Reservation;
import project.source.models.entities.Room;
import project.source.respones.ApiResponse;
import project.source.respones.PageResponse;
import project.source.services.implement.ReservationService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ReservationController {
    ReservationService reservationService;

    @GetMapping("/allRooms/{roomId}")
    public ResponseEntity<ApiResponse> getAllRoomByRoomId(
            @PathVariable(value = "roomId") Long roomId,
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
        Page<Reservation> reservations = reservationService.getAllReservationByRoomId(roomId, pageRequest);

        PageResponse<List<Reservation>> response = PageResponse.<List<Reservation>>builder()
                .totalPage(reservations.getTotalPages())
                .pageNo(reservations.getNumber())
                .pageSize(reservations.getSize())
                .items(reservations.getContent())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all reservations successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/allUsers/{roomId}")
    public ResponseEntity<ApiResponse> getAllRoomByUserId(
            @PathVariable(value = "userId") Long userId,
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
        Set<Reservation> reservations = reservationService.getAllReservationByUserId(userId);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all reservations successfully")
                .data(reservations)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable(value = "id")Long id){
        Reservation reservation = reservationService.getReservationById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get reservation successfully by id = "+ id)
                .data(reservation)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/bookRoom")
    public ResponseEntity<ApiResponse> makeReservation(@Valid @RequestBody Reservation reservation, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't create reservation")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            reservationService.saveReservation(reservation);
                ApiResponse apiResponse = ApiResponse.builder()
                        .message("Create successfully")
                        .status(HttpStatus.CREATED.value())
                        .build();
                return ResponseEntity.ok(apiResponse);
            }
        }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable(value = "id")Long id){
        reservationService.deleteReservation(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Delete reservation successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
