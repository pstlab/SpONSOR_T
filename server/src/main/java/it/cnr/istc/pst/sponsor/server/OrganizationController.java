package it.cnr.istc.pst.sponsor.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.ConflictResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import it.cnr.istc.pst.sponsor.api.Affiliation;
import it.cnr.istc.pst.sponsor.api.Organization;
import it.cnr.istc.pst.sponsor.server.db.OrganizationEntity;

/**
 * OrganizationController
 */
public class OrganizationController {

    static final Logger LOG = LoggerFactory.getLogger(OrganizationController.class);

    static public void getAllOrganizations(Context ctx) {
        LOG.info("retrieving all organizations..");
        EntityManager em = App.EMF.createEntityManager();
        List<OrganizationEntity> organization_entities = em
                .createQuery("SELECT oe FROM OrganizationEntity oe", OrganizationEntity.class).getResultList();

        List<Organization> organizations = new ArrayList<>(organization_entities.size());
        for (OrganizationEntity organization_entity : organization_entities)
            organizations.add(new Organization(organization_entity.getId(), organization_entity.getName(), null));

        ctx.json(organizations);
    }

    static public void createOrganization(Context ctx) {
        String name = ctx.formParam("name");
        LOG.info("creating new organization {}..", name);
        EntityManager em = App.EMF.createEntityManager();

        OrganizationEntity organization_entity = new OrganizationEntity();
        organization_entity.setName(name);

        try {
            em.getTransaction().begin();
            em.persist(organization_entity);
            em.getTransaction().commit();
            ctx.status(201);
        } catch (Exception ex) {
            throw new ConflictResponse();
        }
    }

    static public void getOrganization(Context ctx) {
        LOG.info("retrieving organization {}..", ctx.pathParam("id"));
        EntityManager em = App.EMF.createEntityManager();
        OrganizationEntity organization_entity = em.find(OrganizationEntity.class, Long.valueOf(ctx.pathParam("id")));
        if (organization_entity == null)
            throw new NotFoundResponse();

        Organization organization = new Organization(organization_entity.getId(), organization_entity.getName(),
                organization_entity.getAffiliated().stream().map(aff -> new Affiliation(aff.getUser().getId(),
                        aff.getOrganization().getId(), aff.getAffiliationDate())).collect(Collectors.toList()));
        ctx.json(organization);
    }

    static public void updateOrganization(Context ctx) {
        LOG.info("updating organization {}..", ctx.pathParam("id"));
        Organization organization = ctx.bodyAsClass(Organization.class);

        EntityManager em = App.EMF.createEntityManager();
        OrganizationEntity organization_entity = em.find(OrganizationEntity.class, Long.valueOf(ctx.pathParam("id")));
        if (organization_entity == null)
            throw new NotFoundResponse();

        em.getTransaction().begin();
        organization_entity.setName(organization.getName());
        em.getTransaction().commit();
        ctx.status(204);
    }

    static public void deleteOrganization(Context ctx) {
        LOG.info("deleting organization {}..", ctx.pathParam("id"));
        EntityManager em = App.EMF.createEntityManager();
        OrganizationEntity organization_entity = em.find(OrganizationEntity.class, Long.valueOf(ctx.pathParam("id")));
        if (organization_entity == null)
            throw new NotFoundResponse();

        em.getTransaction().begin();
        em.remove(organization_entity);
        em.getTransaction().commit();
        ctx.status(204);
    }
}