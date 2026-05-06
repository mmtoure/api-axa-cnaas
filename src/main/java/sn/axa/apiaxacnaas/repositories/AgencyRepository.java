package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Agency;

import java.util.List;
import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByName(String name);
    List<Agency> findByNetworkId(Long networkId);
}
