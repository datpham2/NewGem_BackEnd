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

    // Get all reservations by user id
    @GetMapping("/all/{userId}")
    public ResponseEntity<ApiResponse> getAllReservationByUserId(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(400)
                    .message(null)
                    .data(bindingResult.getAllErrors())
                    .build());
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationService.getAllReservationByUserId(userId, pageRequest);

        List<ReservationDTO> items = reservations.getContent().stream().map(ReservationDTO::fromReservation).toList();

        PageResponse<List<ReservationDTO>> response = PageResponse.<List<ReservationDTO>>builder()
                .totalPage(reservations.getTotalPages())
                .pageNo(reservations.getNumber())
                .data(items)
                .build();

        return ResponseEntity.ok(ApiResponse.builder()
                .status(200)
                .message(null)
                .data(response)
                .build());
    }

    // Create a new reservation
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReservation(@Valid @RequestBody ReservationDTO reservationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(400)
                    .message(null)
                    .data(bindingResult.getAllErrors())
                    .build());
        }

        Long roomId = reservationDTO.getRoomId();
        Long userId = reservationDTO.getUserId();
        Long hotelId = reservationDTO.getHotelId();

        Reservation reservation = reservationService.saveReservation(reservationDTO.toReservation(), roomId, userId, hotelId);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(200)
                .message("Reservation created successfully")
                .data(ReservationDTO.fromReservation(reservation))
                .build());
    }


}
