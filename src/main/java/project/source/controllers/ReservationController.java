package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.ReservationDTO;
import project.source.models.entities.Reservation;
import project.source.respones.ApiResponse;
import project.source.respones.PageResponse;
import project.source.services.reservation.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/allRooms/{roomId}")
    public ResponseEntity<?> getAllRoomByRoomId(
        @PathVariable(value = "roomId") Long roomId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "8") int size,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Error");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationService.getAllReservationByRoomId(roomId, pageRequest);

        List<ReservationDTO> items = reservations.getContent().stream().map(ReservationDTO::fromReservation).toList();

        PageResponse<List<ReservationDTO>> response = PageResponse.<List<ReservationDTO>>builder()
            .totalPage(reservations.getTotalPages())
            .pageNo(reservations.getNumber()).pageSize(reservations.getSize())
            .items(items)
            .build();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(200);
        apiResponse.setMessage("Success");
        apiResponse.setData(response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/allUsers/{roomId}")
    public ResponseEntity<?> getAllUserByRoomId(
        @PathVariable(value = "roomId") Long roomId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "8") int size,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Error");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationService.getAllReservationByRoomId(roomId, pageRequest);

        List<ReservationDTO> items = reservations.getContent().stream().map(ReservationDTO::fromReservation).toList();

        PageResponse<List<ReservationDTO>> response = PageResponse.<List<ReservationDTO>>builder()
            .totalPage(reservations.getTotalPages())
            .pageNo(reservations.getNumber()).pageSize(reservations.getSize())
            .items(items)
            .build();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(200);
        apiResponse.setMessage("Success");
        apiResponse.setData(response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable(value = "id")Long id){
        Reservation reservation = reservationService.getReservationById(id);
        ReservationDTO reservationDTO = ReservationDTO.fromReservation(reservation);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(200);
        apiResponse.setMessage("Get reservation successfully");
        apiResponse.setData(reservationDTO);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/bookRoom")
    public ResponseEntity<ApiResponse> makeReservation(@Valid @RequestBody Reservation reservation, BindingResult result){
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(400);
            apiResponse.setMessage("Error");
            apiResponse.setData(errors);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        Reservation newReservation = reservationService.saveReservation(reservation);
        ReservationDTO reservationDTO = ReservationDTO.fromReservation(newReservation);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(200);
        apiResponse.setMessage("Book room successfully");
        apiResponse.setData(reservationDTO);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable(value = "id")Long id){
        reservationService.deleteReservation(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(200);
        apiResponse.setMessage("Cancel reservation successfully");
        return ResponseEntity.ok(apiResponse);
    }
}
