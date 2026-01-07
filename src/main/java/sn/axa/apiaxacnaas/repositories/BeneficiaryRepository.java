package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}
