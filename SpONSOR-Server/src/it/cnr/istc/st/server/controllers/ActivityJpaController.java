/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.Activity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import it.cnr.istc.st.server.entity.GroupActivity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ActivityJpaController implements Serializable {

    public ActivityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Activity activity) {
        if (activity.getGroups() == null) {
            activity.setGroups(new ArrayList<GroupActivity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<GroupActivity> attachedGroups = new ArrayList<GroupActivity>();
            for (GroupActivity groupsGroupActivityToAttach : activity.getGroups()) {
                groupsGroupActivityToAttach = em.getReference(groupsGroupActivityToAttach.getClass(), groupsGroupActivityToAttach.getId());
                attachedGroups.add(groupsGroupActivityToAttach);
            }
            activity.setGroups(attachedGroups);
            em.persist(activity);
            for (GroupActivity groupsGroupActivity : activity.getGroups()) {
                Activity oldActivityOfGroupsGroupActivity = groupsGroupActivity.getActivity();
                groupsGroupActivity.setActivity(activity);
                groupsGroupActivity = em.merge(groupsGroupActivity);
                if (oldActivityOfGroupsGroupActivity != null) {
                    oldActivityOfGroupsGroupActivity.getGroups().remove(groupsGroupActivity);
                    oldActivityOfGroupsGroupActivity = em.merge(oldActivityOfGroupsGroupActivity);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Activity activity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Activity persistentActivity = em.find(Activity.class, activity.getId());
            List<GroupActivity> groupsOld = persistentActivity.getGroups();
            List<GroupActivity> groupsNew = activity.getGroups();
            List<GroupActivity> attachedGroupsNew = new ArrayList<GroupActivity>();
            for (GroupActivity groupsNewGroupActivityToAttach : groupsNew) {
                groupsNewGroupActivityToAttach = em.getReference(groupsNewGroupActivityToAttach.getClass(), groupsNewGroupActivityToAttach.getId());
                attachedGroupsNew.add(groupsNewGroupActivityToAttach);
            }
            groupsNew = attachedGroupsNew;
            activity.setGroups(groupsNew);
            activity = em.merge(activity);
            for (GroupActivity groupsOldGroupActivity : groupsOld) {
                if (!groupsNew.contains(groupsOldGroupActivity)) {
                    groupsOldGroupActivity.setActivity(null);
                    groupsOldGroupActivity = em.merge(groupsOldGroupActivity);
                }
            }
            for (GroupActivity groupsNewGroupActivity : groupsNew) {
                if (!groupsOld.contains(groupsNewGroupActivity)) {
                    Activity oldActivityOfGroupsNewGroupActivity = groupsNewGroupActivity.getActivity();
                    groupsNewGroupActivity.setActivity(activity);
                    groupsNewGroupActivity = em.merge(groupsNewGroupActivity);
                    if (oldActivityOfGroupsNewGroupActivity != null && !oldActivityOfGroupsNewGroupActivity.equals(activity)) {
                        oldActivityOfGroupsNewGroupActivity.getGroups().remove(groupsNewGroupActivity);
                        oldActivityOfGroupsNewGroupActivity = em.merge(oldActivityOfGroupsNewGroupActivity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = activity.getId();
                if (findActivity(id) == null) {
                    throw new NonexistentEntityException("The activity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Activity activity;
            try {
                activity = em.getReference(Activity.class, id);
                activity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activity with id " + id + " no longer exists.", enfe);
            }
            List<GroupActivity> groups = activity.getGroups();
            for (GroupActivity groupsGroupActivity : groups) {
                groupsGroupActivity.setActivity(null);
                groupsGroupActivity = em.merge(groupsGroupActivity);
            }
            em.remove(activity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Activity> findActivityEntities() {
        return findActivityEntities(true, -1, -1);
    }

    public List<Activity> findActivityEntities(int maxResults, int firstResult) {
        return findActivityEntities(false, maxResults, firstResult);
    }

    private List<Activity> findActivityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Activity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Activity findActivity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Activity.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Activity> rt = cq.from(Activity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
