package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.dto.NetworkDTO;
import sn.axa.apiaxacnaas.entities.Network;

import java.util.List;

public interface NetworkRepository extends JpaRepository<Network, Long> {
    List<Network> findByManagerIsNull();
}
