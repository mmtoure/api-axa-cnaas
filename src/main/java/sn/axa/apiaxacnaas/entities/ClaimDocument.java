package sn.axa.apiaxacnaas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_claimDocuments")
public class ClaimDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType; // PDF, JPG, PNG
    private Long fileSize;
    private String filePath;

    @Enumerated(EnumType.STRING)
    private ClaimDocumentType type;  // EX: RAPPORT_MEDICAL, FACTURE, CIN, PV_POLICE

    @ManyToOne
    @JoinColumn(name = "claim_id")
    @JsonIgnore
    private Claim claim;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
