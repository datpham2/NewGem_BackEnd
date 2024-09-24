package project.source.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.source.models.entities.User;
import project.source.models.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.role.name != :adminRole")
    Page<User> findActiveUsersExcludingAdmins(@Param("status") Status status, @Param("adminRole") String adminRole, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.status != :status AND u.role.name != :adminRole")
    Page<User> findInactiveUsersExcludingAdmins(@Param("status") Status status, @Param("adminRole") String adminRole, Pageable pageable);

}
