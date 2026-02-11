package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Partner;

public interface PartnerRepository extends JpaRepository<Partner,Long> {
}
