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
import project.source.models.enums.City;
import project.source.respones.ApiResponse;
import project.source.respones.HotelResponse;
import project.source.respones.PageResponse;
import project.source.services.implement.HotelService;

import java.math.BigDecimal;
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
        List<HotelDTO> hotelList = hotelPage.getContent().stream().map(HotelDTO::fromHotel).toList();
        HotelResponse hotelResponse = HotelResponse.builder()
                .totalPage(hotelPage.getTotalPages())
                .hotel(hotelList)
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
                .data(HotelDTO.fromHotel(hotel))
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchHotel(@RequestParam(name = "city", required = false) City city,
                                                   @RequestParam(name = "min", required = false) BigDecimal minPrice,
                                                   @RequestParam(name = "max", required = false) BigDecimal maxPrice,
                                                   @RequestParam(name = "min", defaultValue = "0") int page,
                                                   @RequestParam(name = "min", defaultValue = "5") int size){
        Page<Hotel> hotels = hotelService.getHotelByCityAndPriceRange(city,minPrice,maxPrice,PageRequest.of(page,size));
        List<HotelDTO> hotelList = hotels.getContent().stream().map(HotelDTO::fromHotel).toList();

        HotelResponse hotelResponse = HotelResponse.builder()
                .totalPage(hotels.getTotalPages())
                .hotel(hotelList)
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Search successfully")
                .data(hotelResponse)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/totalHotel")
    public ResponseEntity<ApiResponse> getTotalHotels(){
        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Get total hotel successfully")
                        .status(HttpStatus.OK.value())
                        .data(hotelService.getTotalHotels())
                .build());
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
            Hotel hotel = hotelService.saveHotel(hotelDTO);
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(HotelDTO.fromHotel(hotel))
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
            hotel = hotelService.updateHotel(hotelDTO,id);
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(HotelDTO.fromHotel(hotel))
                    .message("Upadte successfully")
                    .status(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
    }

    @PatchMapping("/disable/{id}")
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
        List<HotelDTO> hotelList = pageHotel.getContent().stream().map(HotelDTO::fromHotel).toList();

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
