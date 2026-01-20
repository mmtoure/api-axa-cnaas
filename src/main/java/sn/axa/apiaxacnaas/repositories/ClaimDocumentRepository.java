package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.ClaimDocument;

import java.util.List;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, Long> {
    List<ClaimDocument> findByClaimId(Long claimId);
}
