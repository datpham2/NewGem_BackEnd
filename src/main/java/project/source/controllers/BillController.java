package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.BillDTO;
import project.source.models.entities.Bill;
import project.source.requests.BillRequest;
import project.source.respones.ApiResponse;
import project.source.services.bill.BillService;
import project.source.services.implement.HotelService;

import java.util.List;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;
    private final HotelService hotelService;

    @GetMapping("/getAllBill")
    public ResponseEntity<ApiResponse> getAllBillByUserId(
            @RequestParam(value = "user") Long userId,
            @RequestParam(value = "hotel") Long hotelId){
        List<Bill> bills = billService.getAllBillByUserIdAndHotelId(userId,hotelId);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all bills by user id " + userId + " hotel name " +
                        hotelService.getHotelById(hotelId) + " successfully")
                .data(bills)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/createBill")
    public ResponseEntity<ApiResponse> getBillByUserId(@Valid @RequestBody BillRequest billRequest){
        Bill bill = billService.addBill(billRequest, billRequest.getUserId(), billRequest.getHotelId(), billRequest.getReservationId(), billRequest.getVoucherId());
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Create bill successfully")
                .data(bill)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
