package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Garantie;
import sn.axa.apiaxacnaas.util.GarantieEnum;

import java.util.Optional;

public interface GarantieRepository extends JpaRepository<Garantie, Long> {
    Optional<Garantie> findByName(GarantieEnum name);
}
