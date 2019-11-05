package it.cnr.istc.pst.sponsor.server.db;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * AffiliationEntity
 */
@Entity
@IdClass(AffiliationId.class)
public class AffiliationEntity {

    @Id
    private Long userId;
    @Id
    private Long organizationId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "id")
    private UserEntity user;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "organizationId", referencedColumnName = "id")
    private OrganizationEntity organization;
    private LocalDate affiliation_date;

    /**
     * @return the user
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserEntity user) {
        this.user = user;
        userId = user.getId();
    }

    /**
     * @return the organization
     */
    public OrganizationEntity getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
        organizationId = organization.getId();
    }

    /**
     * @return the affiliation_date
     */
    public LocalDate getAffiliationDate() {
        return affiliation_date;
    }

    /**
     * @param affiliation_date the affiliation_date to set
     */
    public void setAffiliationDate(LocalDate affiliation_date) {
        this.affiliation_date = affiliation_date;
    }
}