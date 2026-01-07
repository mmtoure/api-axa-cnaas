package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Agence;

public interface AgenceRepository extends JpaRepository<Agence, Long> {
}
