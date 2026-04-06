package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.axa.apiaxacnaas.dto.ClaimMonthlyStatDTO;
import sn.axa.apiaxacnaas.entities.Claim;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {


    List<Claim> findByUserIdOrderByCreatedAtDesc(Long userId, Sort createdAt);
    List<Claim> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = 'EN_COURS'")
    Long countOpenClaims();

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = 'ACCEPTE'")
    Long countAcceptedClaims();

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = 'EN_COURS' AND c.user.id=:userId")
    Long countOpenClaimsForCurrentUser(Long userId);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = 'ACCEPTE' AND c.user.id=:userId")
    Long countAcceptedClaimsForCurrentUser(Long userId);

    @Query(value = """
    SELECT
      EXTRACT(YEAR FROM c.createdAt),
      EXTRACT(MONTH FROM c.createdAt),
      COUNT(c)
    FROM Claim c
    GROUP BY
      EXTRACT(YEAR FROM c.createdAt),
      EXTRACT(MONTH FROM c.createdAt)
    ORDER BY
      EXTRACT(YEAR FROM c.createdAt),
      EXTRACT(MONTH FROM c.createdAt)
    """)
    List<ClaimMonthlyStatDTO> countAllClaimsByMonth();

    @Query(value = """
    SELECT
      EXTRACT(YEAR FROM c.createdAt),
      EXTRACT(MONTH FROM c.createdAt),
      COUNT(c)
    FROM Claim c
    WHERE c.user.id = :userId
    GROUP BY
      EXTRACT(YEAR FROM c.createdAt),
      EXTRACT(MONTH FROM c.createdAt)
    ORDER BY
      EXTRACT(YEAR FROM c.createdAt),
      EXTRACT(MONTH FROM c.createdAt)                                           
    """)
    List<ClaimMonthlyStatDTO> countAllClaimsByMonthForCurrentUser(Long userId);

    Optional<Claim> findTopByOrderByIdDesc();

}
