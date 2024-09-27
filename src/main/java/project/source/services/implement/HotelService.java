package project.source.services.implement;
/**
 * @autor An Nguyen
 */
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import project.source.dtos.HotelDTO;
import project.source.exceptions.ConflictException;
import project.source.exceptions.ExistedException;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Hotel;
import project.source.models.entities.Voucher;
import project.source.models.enums.City;
import project.source.models.enums.Status;
import project.source.repositories.HotelRepository;
import project.source.repositories.VoucherRepository;
import project.source.services.IHotelService;

import java.math.BigDecimal;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {
    private final HotelRepository hotelRepository;


    @Override
    public Page<Hotel> getAllHotel(PageRequest pageRequest) {
        return hotelRepository.findAll(pageRequest);
    }

    @Override
    public Page<Hotel> getHotelByCityAndPriceRange(City city, BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest) {
        return hotelRepository.findByCityAndPriceRange(city,minPrice,maxPrice,pageRequest);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new NotFoundException("Hotel not found by id = "+id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Hotel saveHotel(HotelDTO hotelDTO) {
        existed(hotelDTO);
        Hotel hotel = Hotel.builder()
                .location(hotelDTO.getLocation())
                .city(hotelDTO.getCity())
                .noRooms(0)
                .status(Status.ACTIVE)
                .name(hotelDTO.getName())
                .build();
        hotel.setPrices();
        return hotelRepository.save(hotel);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Hotel updateHotel(HotelDTO hotelDTO, Long id) {
        Hotel hotel1 = getHotelById(id);
        if(hotel1 == null){
            throw new NotFoundException("Can't find hotel by id = "+ id);
        }else {
            if (hotel1.getCity() != hotelDTO.getCity()){
                throw new ConflictException("Can move hotel to another city");
            }
            hotel1.setLocation(hotelDTO.getLocation());
            hotel1.setCity(hotelDTO.getCity());
            return hotelRepository.save(hotel1);
        }
    }

    @Override
    public Page<Hotel> searchHotelByName(String name, PageRequest pageRequest) {
        return hotelRepository.searchByName(name, pageRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void disableHotel(Long id) {
        Hotel hotel = getHotelById(id);
        if(hotel.getStatus() == Status.ACTIVE){
            hotel.setStatus(Status.INACTIVE);
        }else {
            hotel.setStatus(Status.ACTIVE);
        }
        hotelRepository.save(hotel);
    }

    @Override
    public long getTotalHotels() {
        return hotelRepository.countAllHotels();
    }

    public void existed(HotelDTO hotelDTO) {
        boolean exists = hotelRepository.existsByNameAndCity(hotelDTO.getName(), hotelDTO.getCity()) &&
                hotelRepository.existsByLocationAndCity(hotelDTO.getLocation(), hotelDTO.getCity());

        if (exists) {
            throw new ExistedException("Hotel already exists");
        }
    }

    public boolean existedById(Long hotelId){
        if (hotelRepository.existsById(hotelId)){
            return true;
        };
        return false;
    }


    public void saveHotel(Hotel hotel){
        hotelRepository.save(hotel);
    }
}
