package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Claim;
import sn.axa.apiaxacnaas.entities.Insured;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    public void notifyNewInsured(InsuredDTO insured) {
        messagingTemplate.convertAndSend("/topic/insured",
            Map.of(
                    "type", "NEW_INSURED",
                    "message", "Nouvel assuré créé",
                    "data", insured
            )
        );
    }
    public void notifyNewClaim(ClaimDTO claim) {
        messagingTemplate.convertAndSend("/topic/claim",
                Map.of(
                        "type", "NEW_CLAIM",
                        "message", "Nouveau Sinistre créé",
                        "data", claim
                )
        );
    }

}
