package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Group;

public interface GroupRepository extends JpaRepository<Group,Long> {
}
