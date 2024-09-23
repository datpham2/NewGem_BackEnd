package project.source.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.source.models.enums.RoomType;

import java.util.Set;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Room extends BaseEntity<Long>{
    @ManyToOne
    @JoinColumn(name = "hotel-id", nullable = false)
    private Hotel hotel;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    @Column(name="price")
    private Double price;

    @NotNull(message = "Type must not be null")
    @Enumerated(EnumType.STRING)
    private RoomType type; /* old column name: typeRoom */

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Set<Reservation> reservations; /* old column name: bookrooms */


    // numbers of guests that can stay in the room
    @Column(name="guests")
    private int guests; /* old column name: noGuests */

    public boolean bookRoom(Reservation reservation) {
        if (isConflict(reservation)) {
            return false;
        } else {
            this.reservations.add(reservation);
            return true;
        }
    }

    private boolean isConflict(Reservation reservation) {
        for (Reservation r : reservations) {
            if (r.getCheckIn().isBefore(reservation.getCheckOut()) && r.getCheckOut().isAfter(reservation.getCheckIn())) {
                return true;
            }
        }
        return false;
    }
}
