package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Region;
import sn.axa.apiaxacnaas.util.RegionEnum;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
     Optional<Region> findByName(RegionEnum name);
     List<Region> findByNetworkIsNull();
     List<Region> findByNetworkIdAndUserIsNull(Long networkId);

}
