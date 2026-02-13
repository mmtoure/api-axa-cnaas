package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Contract;
import sn.axa.apiaxacnaas.entities.Insured;

public interface InsuredRepository extends JpaRepository<Insured, Long> {
    Page<Insured> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
