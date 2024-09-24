package project.source.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.source.models.enums.RoomType;

import java.util.HashSet;
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
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    private int roomNumber;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    @Column(name="price")
    private Double price;

    @NotNull(message = "Type must not be null")
    @Enumerated(EnumType.STRING)
    private RoomType type; /* old column name: typeRoom */

    @JsonManagedReference
    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();


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
}
