package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Zone;

public interface ZoneRepository  extends JpaRepository<Zone, Long> {
}
