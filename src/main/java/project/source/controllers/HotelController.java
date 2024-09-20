package project.source.controllers;
/**
 * @author An Nguyen
 */

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.source.dtos.HotelDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.respones.ApiResponse;
import project.source.respones.HotelResponse;
import project.source.services.implement.HotelService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/allHotel")
    public ResponseEntity<ApiResponse> getAllHotel(@RequestParam(value = "page", defaultValue = "0")int page
                                        ,@RequestParam(value = "size", defaultValue = "8")int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Hotel> hotelPage = hotelService.getAllHotel(pageRequest);
        HotelResponse hotelResponse = HotelResponse.builder()
                .totalPage(hotelPage.getTotalPages())
                .hotel(hotelPage.getContent())
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get All Hotel Successfully")
                .data(hotelResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getHotelDetail(@PathVariable(value = "id")Long id){
        Hotel hotel = hotelService.getHotelById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get Hotel Successfully by id = "+ id)
                .data(hotel)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/createHotel")
    public ResponseEntity<ApiResponse> createHotel(@Valid @RequestBody HotelDTO hotelDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't Create Hotel")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            hotelService.saveHotel(hotelDTO);
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(hotelDTO)
                    .message("Create successfully")
                    .status(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }
    @PutMapping("/updateHotel/{id}")
    public ResponseEntity<ApiResponse> updateHotel(@Valid @RequestBody HotelDTO hotelDTO, @PathVariable(value = "id")Long id,
                                                   BindingResult result){
        if(result.hasErrors()){
            List<String> err = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Can't update Hotel")
                    .data(err)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }else {
            Hotel hotel = hotelService.getHotelById(id);
            if(hotel == null){
                throw new NotFoundException("Not Found Hotel with ID = "+ id);
            }
                hotelService.updateHotel(hotelDTO,id);
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(hotelDTO)
                    .message("Upadte successfully")
                    .status(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> disableHotel(@PathVariable(value = "id")Long id){
        hotelService.disableHotel(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Disable successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/searchHotelByName")
    public ResponseEntity<ApiResponse> searchByNameHotel(@RequestParam(value = "name", defaultValue = "%")String name,
                                                         @RequestParam(value = "page",defaultValue = "0")int page,
                                                         @RequestParam(value = "size",defaultValue = "8")int size){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Hotel> pageHotel = hotelService.searchHotelByName(name, pageRequest);
        List<Hotel> hotelList = pageHotel.getContent();
        HotelResponse hotelResponse = HotelResponse.builder()
                .totalPage(pageHotel.getTotalPages())
                .hotel(hotelList)
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Search successfully by name = "+name)
                .data(hotelResponse)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
