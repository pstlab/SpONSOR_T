package it.cnr.istc.pst.sponsor.api;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Affiliation
 */
public class Affiliation {

    private Long user_id;
    private Long organization_id;
    private LocalDate affiliation_date;

    @JsonCreator
    public Affiliation(@JsonProperty("userId") Long user_id, @JsonProperty("organizationId") Long organization_id,
            @JsonProperty("affiliationDate") LocalDate affiliation_date) {
        this.user_id = user_id;
        this.organization_id = organization_id;
        this.affiliation_date = affiliation_date;
    }

    /**
     * @return the user_id
     */
    public Long getUserId() {
        return user_id;
    }

    /**
     * @return the organization_id
     */
    public Long getOrganizationId() {
        return organization_id;
    }

    /**
     * @return the affiliation_date
     */
    public LocalDate getAffiliationDate() {
        return affiliation_date;
    }
}