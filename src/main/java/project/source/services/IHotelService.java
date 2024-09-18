package project.source.services;
/**
 * @autor An Nguyen
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.source.dtos.HotelDTO;
import project.source.models.entities.Hotel;

import java.util.List;

public interface IHotelService {
    Page<Hotel> getAllHotel(PageRequest pageRequest);
    Hotel getHotelById(Long id);
    void saveHotel(HotelDTO hotelDTO);
    void updateHotel(HotelDTO hotelDTO, Long id);
    Page<Hotel> searchHotelByName(String name, PageRequest pageRequest);
}
