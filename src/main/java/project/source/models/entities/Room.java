package project.source.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.source.models.enums.RoomType;
import project.source.models.enums.Status;

import java.math.BigDecimal;
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
    @JoinColumn(name = "hotel-id", nullable = false, updatable = false)
    Hotel hotel;

    @Column(name = "room_number", nullable = false)
    int roomNumber;

    @Column(name="price", nullable = false)
    BigDecimal price;

    @Column(name = "room_type",nullable = false)
    @Enumerated(EnumType.STRING)
    RoomType type;


    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    Set<Reservation> reservations = new HashSet<>();

    @Column(name="guests")
    int guests;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    public boolean bookRoom(Reservation reservation) {
        if (isConflict(reservation)) {
            return false;
        } else {
            this.reservations.add(reservation);
            return true;
        }
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
