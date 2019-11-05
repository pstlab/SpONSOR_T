package it.cnr.istc.pst.sponsor.server.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * OrganizationEntity
 */
@Entity
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AffiliationEntity.class, fetch = FetchType.EAGER)
    private Collection<AffiliationEntity> affiliated = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the affiliated
     */
    public Collection<AffiliationEntity> getAffiliated() {
        return Collections.unmodifiableCollection(affiliated);
    }

    public void addAffiliated(AffiliationEntity affiliation) {
        affiliated.add(affiliation);
    }

    public void removeAffiliated(AffiliationEntity affiliation) {
        affiliated.remove(affiliation);
    }
}