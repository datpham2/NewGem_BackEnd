package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.VoucherDTO;
import project.source.models.entities.Voucher;
import project.source.respones.ApiResponse;

import project.source.respones.VoucherResponse;
import project.source.services.implement.VoucherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/createVoucher/{hotelId}")
    public ResponseEntity<ApiResponse> createReview(@Valid @PathVariable(value = "hotelId")Long hotelId,
                                                    @RequestBody VoucherDTO voucherDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't Create Voucher")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            voucherService.saveVoucher(hotelId, voucherDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(voucherDTO)
                    .message("Saved voucher successfully")
                    .status(HttpStatus.CREATED.value())
                    .build());
        }
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
    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateVoucher(@Valid @PathVariable Long id,
                                                     @RequestBody VoucherDTO voucherDTO,
                                                     BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't Update Voucher")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            voucherService.updateVoucher(id, voucherDTO);
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(voucherDTO)
                    .message("Update voucher successfully")
                    .status(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> disableVoucher(@PathVariable Long id){
        voucherService.disableVoucher(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Disable Voucher Successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
