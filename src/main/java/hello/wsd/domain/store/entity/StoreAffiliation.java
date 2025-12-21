package hello.wsd.domain.store.entity;

import hello.wsd.domain.affliation.entity.Affiliation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_affiliation", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "store_id", "affiliation_id" })
})
public class StoreAffiliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliation_id", nullable = false)
    private Affiliation affiliation;

    @Builder
    public StoreAffiliation(Store store, Affiliation affiliation) {
        this.store = store;
        this.affiliation = affiliation;
    }
}
