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
 * UserEntity
 */
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AffiliationEntity.class, fetch = FetchType.EAGER)
    private Collection<AffiliationEntity> affiliations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the affiliations
     */
    public Collection<AffiliationEntity> getAffiliations() {
        return Collections.unmodifiableCollection(affiliations);
    }

    public void addAffiliation(AffiliationEntity affiliation) {
        affiliations.add(affiliation);
    }

    public void removeAffiliation(AffiliationEntity affiliation) {
        affiliations.remove(affiliation);
    }
}