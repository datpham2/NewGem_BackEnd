package project.source.configs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.*;
import project.source.models.enums.City;
import project.source.models.enums.RoomType;
import project.source.repositories.*;
import project.source.models.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    HotelRepository hotelRepository;
    RoomRepository roomRepository;
    ReservationRepository reservationRepository;
    VoucherRepository voucherRepository;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_EMAIL = "admin@newgem.com";
    private static final String ADMIN_PHONE = "3725985432";
    private static final String HOTEL_NAME = "New Gem";
    private static final String HOTEL_LOCATION = "HCM city";

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            PageRequest pageRequest = PageRequest.of(0, 1);
            initializeRoles();
            User adminUser = initializeAdminUser();
            Hotel hotel = initializeHotel();
            Room room = initializeRoom(hotel, pageRequest);
            Voucher voucher = initializeVoucher(hotel);
            initializeReservations(adminUser, hotel, room, pageRequest);
        };
    }

    private void initializeRoles() {
        List<String> roles = List.of(Role.ADMIN, Role.USER);
        for (String roleType : roles) {
            if (!roleRepository.existsByName(roleType)) {
                Role role = Role.builder()
                        .name(roleType)
                        .build();
                roleRepository.save(role);
            }
        }
    }

    private User initializeAdminUser() {
        User adminUser = User.builder()
                .username(ADMIN_USERNAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .email(ADMIN_EMAIL)
                .phone(ADMIN_PHONE)
                .status(Status.ACTIVE)
                .build();

        Role role = roleRepository.findByName(Role.ADMIN)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        adminUser.setRole(role);

        if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
            adminUser = userRepository.save(adminUser);
            log.info("Admin user has been created with default password: {}", ADMIN_PASSWORD);
        } else {
            adminUser = userRepository.findByUsername(ADMIN_USERNAME).get();
        }
        return adminUser;
    }

    private Hotel initializeHotel() {
        Hotel hotel = Hotel.builder()
                .name(HOTEL_NAME)
                .location(HOTEL_LOCATION)
                .city(City.HCM)
                .maxPrice(BigDecimal.valueOf(0))
                .minPrice(BigDecimal.valueOf(0))
                .rooms(new HashSet<>())
                .status(Status.ACTIVE)
                .build();

        Optional<Hotel> existingHotel = hotelRepository.findByName(hotel.getName());
        if (existingHotel.isEmpty()) {
            hotel = hotelRepository.save(hotel);
        } else {
            hotel = existingHotel.get();
        }
        return hotel;
    }

    private Room initializeRoom(Hotel hotel, PageRequest pageRequest) {
        Room room = Room.builder()
                .guests(4)
                .hotel(hotel)
                .price(BigDecimal.valueOf(10))
                .type(RoomType.VIP)
                .roomNumber(101)
                .build();

        Page<Room> rooms = roomRepository.findAllByHotelId(hotel.getId(), pageRequest);
        if (rooms.getContent().isEmpty()) {
            room = roomRepository.save(room);
        } else {
            room = rooms.getContent().getFirst();
        }
        return room;
    }

    private Voucher initializeVoucher(Hotel hotel) {
        Voucher voucher = Voucher.builder()
                .hotel(hotel)
                .discount(BigDecimal.valueOf(0.8))
                .startDate(LocalDate.of(2024,10,30))
                .endDate(LocalDate.now())
                .status(Status.ACTIVE)
                .build();
        Voucher existingVoucher = voucherRepository.findAllById(1L).orElse(null);
        if (existingVoucher == null){
            existingVoucher = voucherRepository.save(voucher);
        }
        return existingVoucher;
    }

    private void initializeReservations(User adminUser, Hotel hotel, Room room, PageRequest pageRequest) {
        Reservation reservation1 = Reservation.builder()
                .user(adminUser)
                .adults(2)
                .children(2)
                .hotel(hotel)
                .room(room)
                .checkIn(LocalDate.of(2024, 10, 1))
                .checkOut(LocalDate.of(2024, 10, 5))
                .status(Status.ACTIVE)
                .build();

        Reservation reservation2 = Reservation.builder()
                .user(adminUser)
                .adults(2)
                .children(2)
                .hotel(hotel)
                .room(room)
                .checkIn(LocalDate.of(2024, 9, 25))
                .checkOut(LocalDate.of(2024, 9, 30))
                .status(Status.ACTIVE)
                .build();

        Page<Reservation> reservations = reservationRepository.findAllReservationByRoomId(room.getId(), pageRequest);
        if (reservations.getContent().isEmpty()) {
            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
        }
    }
}
