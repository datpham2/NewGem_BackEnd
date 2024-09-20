package project.source.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.ReviewsDTO;
import project.source.dtos.VoucherDTO;
import project.source.models.entities.Voucher;
import project.source.respones.ApiResponse;

import project.source.respones.VoucherResponse;
import project.source.services.VoucherService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/createVoucher/{hotelId}")
    public ResponseEntity<ApiResponse> createReview(@PathVariable(value = "hotelId")Long hotelId,
                                                    @RequestBody VoucherDTO voucherDTO){
        voucherService.saveVoucher(hotelId,voucherDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(voucherDTO)
                .message("Saved voucher successfully")
                .status(HttpStatus.CREATED.value())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAllVoucher(@PathVariable(value = "id")Long id,
                                                     @RequestParam(value = "page", defaultValue = "0")int page,
                                                     @RequestParam(value = "size", defaultValue = "5")int size){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Voucher> vouchers = voucherService.getAllVoucher(id, pageRequest);
        VoucherResponse voucherResponse = VoucherResponse.builder()
                .voucherList(vouchers.getContent())
                .totalPage(vouchers.getTotalPages())
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get all vouchers successfully")
                .data(voucherResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
