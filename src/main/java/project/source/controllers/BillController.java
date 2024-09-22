package project.source.controllers;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.source.models.entities.Bill;


import project.source.requests.BillRequest;
import project.source.respones.ApiResponse;
import project.source.services.implement.BillService;
import project.source.services.implement.HotelService;


import java.util.List;



@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BillController {
    BillService billService;
    HotelService hotelService;

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

    @PostMapping("/addBill")
    public ResponseEntity<ApiResponse> getBillByUserId(@Valid @RequestBody BillRequest billRequest){
        Bill bill = billService.addBill(billRequest);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Get all bills successfully").message("Get all bills by user id "
                        + bill.getUser().getId() + " hotel name " +
                        bill.getHotel().getName() + " successfully")
                .data(bill)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/addBill/{billId}")
    public ResponseEntity<ApiResponse> getBillId(@PathVariable(name = "billId") long billId){
        Bill bill = billService.getBillById(billId);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get bill successfully")
                .data(bill)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBillById(@PathVariable(value = "id")Long id){
        Bill bill = billService.getBillById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get bill successfully by id = "+ id)
                .data(bill)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
