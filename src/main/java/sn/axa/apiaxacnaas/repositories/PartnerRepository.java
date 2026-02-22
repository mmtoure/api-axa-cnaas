package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Partner;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner,Long> {
    Optional<Partner> findByCode(String code);
}
