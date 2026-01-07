package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Sinistre;

public interface SinistreRepository extends JpaRepository<Sinistre, Long> {
}
