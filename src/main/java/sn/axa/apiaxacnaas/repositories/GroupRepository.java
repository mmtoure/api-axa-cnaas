package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.entities.Insured;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Long> {

    List<Group> findByUserIdOrderByCreatedAtDesc(Long userId);
    @Query("SELECT COUNT(g) FROM Group g")
    Long countAllGroups();

    @Query("SELECT COUNT(g) FROM Group g WHERE g.user.id =:userId")
    Long countGroupsForCurrentUser(Long userId);

    List<Group> findByAgenceId(Long agenceId);
    List<Group> findByZoneId(Long zoneId);
}
