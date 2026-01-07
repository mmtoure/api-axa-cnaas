package sn.axa.apiaxacnaas.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_sinistreDocuments")
public class SinistreDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Lob
    private byte[] file;
    private String fileContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sinistre sinistre;

}
