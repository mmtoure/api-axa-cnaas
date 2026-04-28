package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsActiveTrue(String email);
    List<User> findByIsActiveTrue();
    List<User> findByZoneIdAndIsActiveTrue(Long zoneId);
    //List<User> findByAgenceIdAndIsActiveTrue(Long agenceId);

}
