package project.source.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.enums.RoomType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rooms")
public class Room extends BaseEntity<Long>{
    @ManyToOne
    @JoinColumn(name = "hotel-id", nullable = false)
    Hotel hotel;

    @Column(name = "room_number", nullable = false, unique = true)
    int roomNumber;

    @Column(name="price", nullable = false)
    double price;

    @Column(name = "room_type",nullable = false)
    @Enumerated(EnumType.STRING)
    RoomType type;


    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Reservation> reservations = new HashSet<>();

    @Column(name="guests")
    int guests;

    public boolean bookRoom(Reservation reservation) {
        if (isConflict(reservation)) {
            return false;
        } else {
            this.reservations.add(reservation);
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room ID: ").append(id).append("\n");
        sb.append("Reservations:\n");
        for (Reservation reservation : reservations) {
            sb.append("Reservation ID: ").append(reservation.getId()).append(", ");
            sb.append("Check in: ").append(reservation.getCheckIn()).append(", ");
            sb.append("Check out: ").append(reservation.getCheckOut()).append(", ");
            sb.append("Customer ID: ").append(reservation.getUser()).append("\n");
        }
        return sb.toString();
    }

    public boolean isBooked(LocalDate date) {
        for (Reservation booking : reservations) {
            if (!date.isBefore(booking.getCheckOut()) && !date.isAfter(booking.getCheckOut())) {
                return true;
            }
        }
        return false;
    }

    private boolean isConflict(Reservation reservation) {
        for (Reservation room : reservations) {
            if (!(reservation.getCheckOut().isBefore(room.getCheckIn()) ||
                    reservation.getCheckIn().isAfter(room.getCheckOut()))) {
                return true;
            }
        }
        return false;
    }

}
