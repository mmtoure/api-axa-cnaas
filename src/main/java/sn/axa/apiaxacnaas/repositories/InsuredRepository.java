package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.axa.apiaxacnaas.dto.InsuredMonthlyStatDTO;

import sn.axa.apiaxacnaas.entities.Insured;

import java.util.List;

public interface InsuredRepository extends JpaRepository<Insured, Long> {
    Page<Insured> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Insured> findByUserIdOrderByCreatedAtDesc(Pageable pageable, Long userId);
    List<Insured> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
    SELECT
      EXTRACT(YEAR FROM i.createdAt),
      EXTRACT(MONTH FROM i.createdAt),
      COUNT(i)
    FROM Insured i
    WHERE i.user.id = :userId
    GROUP BY
      EXTRACT(YEAR FROM i.createdAt),
      EXTRACT(MONTH FROM i.createdAt)
    ORDER BY
      EXTRACT(YEAR FROM i.createdAt),
      EXTRACT(MONTH FROM i.createdAt)
    """)
    List<InsuredMonthlyStatDTO> countInsuredByMonthForCurrentuser(Long userId);

    @Query(value = """
    SELECT
      EXTRACT(YEAR FROM i.createdAt),
      EXTRACT(MONTH FROM i.createdAt),
      COUNT(i)
    FROM Insured i
    GROUP BY
      EXTRACT(YEAR FROM i.createdAt),
      EXTRACT(MONTH FROM i.createdAt)
    ORDER BY
      EXTRACT(YEAR FROM i.createdAt),
      EXTRACT(MONTH FROM i.createdAt)
    """)
    List<InsuredMonthlyStatDTO> countAllInsuredByMonth();


    @Query("SELECT COUNT(i) FROM Insured i")
    Long countAllInsureds();

    @Query("SELECT COUNT(i) FROM Insured i WHERE i.user.id =:userId")
    Long countInsuredsForCurrentUser(Long userId);

}
