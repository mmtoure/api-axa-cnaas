package sn.axa.apiaxacnaas.entities;

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

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType; // PDF, JPG, PNG

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimDocumentType type;  // EX: RAPPORT_MEDICAL, FACTURE, CIN, PV_POLICE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
