package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Agence;

import java.util.List;
import java.util.Optional;

public interface AgenceRepository extends JpaRepository<Agence, Long> {
    Optional<Agence> findByName(String name);
   // List<Agence> findByChefZoneId(Long id);
    List<Agence> findByZoneId(Long zoneId);
}
