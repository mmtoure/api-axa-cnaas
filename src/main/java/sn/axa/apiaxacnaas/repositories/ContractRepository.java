package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.axa.apiaxacnaas.entities.Contract;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("""
        select c
        from Contract c
        left join fetch c.garanties cg
        left join fetch cg.garantie g
        where c.id = :id
    """)
    Optional<Contract> findByIdWithGaranties(@Param("id") Long id);

    @Query("SELECT COUNT(c) FROM Contract c WHERE c.status = 'ACTIVE'")
    Long countActiveContracts();

    Page<Contract> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Contract> findByUserIdOrderByCreatedAtDesc(Pageable pageable, Long userId);




}
