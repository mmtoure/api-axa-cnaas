package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.entities.Insured;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Page<Group> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Group> findByUserIdOrderByCreatedAtDesc(Pageable pageable, Long userId);
    @Query("SELECT COUNT(g) FROM Group g")
    Long countAllGroups();

    @Query("SELECT COUNT(g) FROM Group g WHERE g.user.id =:userId")
    Long countGroupsForCurrentUser(Long userId);
}
