package project.source.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.BillDTO;
import project.source.models.entities.Bill;


import project.source.requests.BillRequest;
import project.source.requests.PayRequest;
import project.source.respones.ApiResponse;
import project.source.services.implement.BillService;
import project.source.services.implement.HotelService;


import java.util.List;



@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
@Tag(name = "Bill", description = "Operations related to bills")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BillController {
    BillService billService;
    HotelService hotelService;

    @Operation(
            method = "GET",
            summary = "Get all bills in the database",
            description = "Send a request to get all the bills data existed in the database")
    @GetMapping("/getAllBill")
    public ResponseEntity<ApiResponse> getAllBillByUserIdAndHotelId(
            @RequestParam(value = "user") Long userId,
            @RequestParam(value = "hotel") Long hotelId){
        List<Bill> bills = billService.getAllBillByUserIdAndHotelId(userId,hotelId);
        List<BillDTO> response = bills.stream().map(BillDTO::fromBill).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all bills by user id " + userId + " hotel name " +
                        hotelService.getHotelById(hotelId).getName() + " successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get all bills in the same hotel",
            description = "Send a request to get all the bills corresponding to a 'hotelId'")
    @GetMapping("/getBillHotel/{hotelId}")
    public ResponseEntity<ApiResponse> getAllBillByHotelId(
            @PathVariable(value = "hotelId") Long hotelId){
        List<Bill> bills = billService.getAllBillByHotelId(hotelId);
        List<BillDTO> response = bills.stream().map(BillDTO::fromBill).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all bills of hotel " +
                        hotelService.getHotelById(hotelId).getName() + " successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "POST",
            summary = "Post the body to create a new bill",
            description = "Send a request with bill body ('hotelID', 'email', 'checkOut', 'voucherId' or else null) to create a bill" )
    @PostMapping("/createBill")
    public ResponseEntity<ApiResponse> getBillByUserId(@Valid @RequestBody BillRequest billRequest){
        Bill bill = billService.addBill(billRequest);
        if (bill != null){
            ApiResponse apiResponse = ApiResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Create bill for user id "
                            + bill.getUser().getId() + " hotel name " +
                            bill.getHotel().getName() + " successfully")
                    .data(BillDTO.fromBill(bill))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED.value())
                .body(ApiResponse.builder()
                        .status(HttpStatus.ALREADY_REPORTED.value())
                        .message("Bill already printed or there are no reservations")
                        .build());
    }

    @Operation(
            method = "GET",
            summary = "Get a bill with id",
            description = "Send a request to get the bill data ('hotelID', 'userId', 'checkOut', 'voucherId') with corresponding 'billId' ")
    @GetMapping("/getBill/{billId}")
    public ResponseEntity<ApiResponse> getBillId(@PathVariable(name = "billId") long billId){
        Bill bill = billService.getBillById(billId);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get bill successfully")
                .data(BillDTO.fromBill(bill))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "POST",
            summary = "Pay the bill",
            description = "Send a request(billId, newFee if necessary or else null, descriptions, receivedAmount) to pay the bill " )
    @PostMapping("/payBill")
    public ResponseEntity<ApiResponse> payBill(@RequestBody @Valid PayRequest payRequest){
        Bill bill = billService.payBill(payRequest);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Pay bill successfully by id = "+ payRequest.getBillId())
                .data(BillDTO.fromBill(bill))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
