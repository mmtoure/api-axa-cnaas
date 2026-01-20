package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}
