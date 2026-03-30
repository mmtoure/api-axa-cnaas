package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ClaimHistoryDTO;
import sn.axa.apiaxacnaas.entities.Claim;
import sn.axa.apiaxacnaas.entities.ClaimHistory;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.mappers.ClaimHistoryMapper;
import sn.axa.apiaxacnaas.repositories.ClaimHistoryRepository;
import sn.axa.apiaxacnaas.util.ClaimStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimHistoryService {
    private final ClaimHistoryRepository claimHistoryRepository;
    private final ClaimHistoryMapper   claimHistoryMapper;

    public List<ClaimHistoryDTO> getHistoriesByClaimId(Long claimId) {
        return claimHistoryRepository.findByClaimIdOrderByActionDateDesc(claimId)
                .stream()
                .map(claimHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
    public ClaimHistoryDTO saveHistory(Claim claim, User user, ClaimStatus action, String comment) {

        ClaimHistory history = new ClaimHistory();
        history.setClaim(claim);
        history.setUser(user);
        history.setStatus(action);
        history.setComment(comment);
        history.setActionDate(LocalDateTime.now());
        ClaimHistory  savedHistory = claimHistoryRepository.save(history);
        return claimHistoryMapper.toDTO(savedHistory);
    }
}
