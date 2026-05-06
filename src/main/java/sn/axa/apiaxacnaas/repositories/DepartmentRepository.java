package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
