package project.source.services;
/**
 * @autor An Nguyen
 */
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.source.dtos.HotelDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.repositories.HotelRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService{

    private final HotelRepository hotelRepository;

    @Override
    public Page<Hotel> getAllHotel(PageRequest pageRequest) {
        return hotelRepository.findAll(pageRequest);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new NotFoundException("Hotel not found by id = "+id));
    }

    @Override
    public void saveHotel(HotelDTO hotelDTO) {
        Hotel hotel = Hotel.builder()
                .location(hotelDTO.getLocation())
                .noRooms(hotelDTO.getNoRooms())
                .status(hotelDTO.isStatus())
                .maxPrice(hotelDTO.getMaxPrice())
                .minPrice(hotelDTO.getMinPrice())
                .name(hotelDTO.getName())
                .build();
        hotelRepository.save(hotel);
    }

    @Override
    public void updateHotel(HotelDTO hotelDTO, Long id) {
        Hotel hotel1 = getHotelById(id);
        if(hotel1 == null){
            throw new NotFoundException("Can't find hotel by id = "+ id);
        }else {
            hotel1.setLocation(hotelDTO.getLocation());
            hotel1.setStatus(hotelDTO.isStatus());
            hotel1.setNoRooms(hotelDTO.getNoRooms());
            hotel1.setMaxPrice(hotelDTO.getMaxPrice());
            hotel1.setMinPrice(hotelDTO.getMinPrice());
            hotel1.setName(hotelDTO.getName());
            hotelRepository.save(hotel1);
        }
    }

    @Override
    public Page<Hotel> searchHotelByName(String name, PageRequest pageRequest) {
        return hotelRepository.searchByName(name, pageRequest);
    }
}