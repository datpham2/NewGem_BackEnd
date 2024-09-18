package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
