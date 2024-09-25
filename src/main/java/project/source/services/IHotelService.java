package project.source.services;
/**
 * @autor An Nguyen
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.source.dtos.HotelDTO;
import project.source.models.entities.Hotel;
import project.source.models.enums.City;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.util.List;

public interface IHotelService {
    Page<Hotel> getAllHotel(PageRequest pageRequest);

    Page<Hotel> getHotelByCityAndPriceRange(City city, BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest);

    Hotel getHotelById(Long id);

    Hotel saveHotel(HotelDTO hotelDTO);

    Hotel updateHotel(HotelDTO hotelDTO, Long id);

    Page<Hotel> searchHotelByName(String name, PageRequest pageRequest);

    void disableHotel(Long id);
}
