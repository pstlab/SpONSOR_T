package it.cnr.istc.pst.sponsor.server.db;

/**
 * AffiliationId
 */
public class AffiliationId {

    private Long userId;
    private Long organizationId;

    public AffiliationId() {
    }

    public AffiliationId(Long userId, Long organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
    }
}