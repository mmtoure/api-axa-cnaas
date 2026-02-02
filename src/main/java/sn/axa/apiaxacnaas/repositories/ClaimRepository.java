package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.axa.apiaxacnaas.entities.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    @Query(value = "SELECT  nextval ('claim_seq')",nativeQuery = true)
    Long getNextSequence();
}
