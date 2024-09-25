package project.source.controllers;

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
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BillController {
    BillService billService;
    HotelService hotelService;


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

    @GetMapping("/getBillHotel/{hotelId}")
    public ResponseEntity<ApiResponse> getAllBillByHotelId(
            @RequestParam(value = "hotel") Long hotelId){
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


    @PostMapping("/createBill")
    public ResponseEntity<ApiResponse> getBillByUserId(@Valid @RequestBody BillRequest billRequest){
        Bill bill = billService.addBill(billRequest);
        if (bill != null){
            ApiResponse apiResponse = ApiResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Get all bills successfully").message("Get all bills by user id "
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


    @PostMapping("/payBill")
    public ResponseEntity<ApiResponse> getBillById(@RequestBody @Valid PayRequest payRequest){
        Bill bill = billService.payBill(payRequest);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Pay bill successfully by id = "+ payRequest.getBillId())
                .data(BillDTO.fromBill(bill))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
