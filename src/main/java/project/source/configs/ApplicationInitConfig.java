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
import project.source.models.enums.RoomType;
import project.source.repositories.*;
import project.source.models.enums.Status;
import project.source.services.implement.ReservationService;

import java.time.LocalDate;
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
    ReservationService reservationService;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            PageRequest pageRequest = PageRequest.of(0, 1);

            // Add roles in db
            List<String> roles = List.of(Role.ADMIN, Role.USER);
            for (String roleType : roles) {
                if (!roleRepository.existsByName(roleType)) {
                    Role role = Role.builder()
                            .name(roleType)
                            .build();
                    roleRepository.save(role);
                }
            }

            // Add admin in db
            final String ADMIN_USERNAME = "admin";
            final String ADMIN_PASSWORD = "admin";

            User adminUser = User.builder()
                    .username(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .email("admin@newgem.com")
                    .phone("3725985432")
                    .status(Status.ACTIVE)
                    .build();

            Role role = roleRepository.findByName(Role.ADMIN).orElseThrow(() -> new NotFoundException("Role not found"));
            adminUser.setRole(role);

            if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
                adminUser = userRepository.save(adminUser);
                log.info("Admin user has been created with default password: admin");
            } else {
                adminUser = userRepository.findByUsername(ADMIN_USERNAME).get();
            }

            // Add hotels in db
            Hotel hotel = Hotel.builder()
                    .name("New Gem")
                    .location("HCM city")
                    .noRooms(2)
                    .status(Status.ACTIVE)
                    .build();

            Optional<Hotel> existingHotel = hotelRepository.findByName(hotel.getName());
            if (existingHotel.isEmpty()) {
                hotel = hotelRepository.save(hotel);
            } else {
                hotel = existingHotel.get();
            }

            // Add room to db
            Room room = Room.builder()
                    .guests(4)
                    .hotel(hotel)
                    .price(10d)
                    .type(RoomType.VIP)
                    .roomNumber(101)
                    .build();

            // Save the room if it does not exist
            Page<Room> rooms = roomRepository.findAllByHotelId(hotel.getId(), pageRequest);
            if (rooms.getContent().isEmpty()) {
                room = roomRepository.save(room);
            } else {
                room = rooms.getContent().getFirst();
            }


            // Add reservation to db
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

            // Save the reservation if it does not exist
            Page<Reservation> reservations = reservationRepository.findAllReservationByRoomId(room.getId(), pageRequest);
            if (reservations.getContent().isEmpty()) {
                reservationRepository.save(reservation1);
                reservationRepository.save(reservation2);
            }
        };
    }
}
