package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
