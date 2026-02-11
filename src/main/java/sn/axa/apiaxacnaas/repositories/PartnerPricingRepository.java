package sn.axa.apiaxacnaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.axa.apiaxacnaas.entities.PartnerPricing;
import sn.axa.apiaxacnaas.util.PartnerCategory;

import java.util.List;
import java.util.Optional;

public interface PartnerPricingRepository extends JpaRepository<PartnerPricing, Long> {
    List<PartnerPricing> findAllByPartnerId(Long partnerId);
    Optional<PartnerPricing> findByPartnerIdAndCategory(Long partnerId, PartnerCategory category);
    Optional<PartnerPricing> findFirstByPartnerId(Long partnerId);

}
