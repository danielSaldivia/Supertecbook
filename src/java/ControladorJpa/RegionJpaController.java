/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladorJpa;

import ControladorJpa.exceptions.NonexistentEntityException;
import ControladorJpa.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Comuna;
import Model.Region;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yo
 */
public class RegionJpaController implements Serializable {

    public RegionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Region region) throws RollbackFailureException, Exception {
        if (region.getComunaCollection() == null) {
            region.setComunaCollection(new ArrayList<Comuna>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Comuna> attachedComunaCollection = new ArrayList<Comuna>();
            for (Comuna comunaCollectionComunaToAttach : region.getComunaCollection()) {
                comunaCollectionComunaToAttach = em.getReference(comunaCollectionComunaToAttach.getClass(), comunaCollectionComunaToAttach.getId());
                attachedComunaCollection.add(comunaCollectionComunaToAttach);
            }
            region.setComunaCollection(attachedComunaCollection);
            em.persist(region);
            for (Comuna comunaCollectionComuna : region.getComunaCollection()) {
                Region oldRegionOfComunaCollectionComuna = comunaCollectionComuna.getRegion();
                comunaCollectionComuna.setRegion(region);
                comunaCollectionComuna = em.merge(comunaCollectionComuna);
                if (oldRegionOfComunaCollectionComuna != null) {
                    oldRegionOfComunaCollectionComuna.getComunaCollection().remove(comunaCollectionComuna);
                    oldRegionOfComunaCollectionComuna = em.merge(oldRegionOfComunaCollectionComuna);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Region region) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Region persistentRegion = em.find(Region.class, region.getId());
            Collection<Comuna> comunaCollectionOld = persistentRegion.getComunaCollection();
            Collection<Comuna> comunaCollectionNew = region.getComunaCollection();
            Collection<Comuna> attachedComunaCollectionNew = new ArrayList<Comuna>();
            for (Comuna comunaCollectionNewComunaToAttach : comunaCollectionNew) {
                comunaCollectionNewComunaToAttach = em.getReference(comunaCollectionNewComunaToAttach.getClass(), comunaCollectionNewComunaToAttach.getId());
                attachedComunaCollectionNew.add(comunaCollectionNewComunaToAttach);
            }
            comunaCollectionNew = attachedComunaCollectionNew;
            region.setComunaCollection(comunaCollectionNew);
            region = em.merge(region);
            for (Comuna comunaCollectionOldComuna : comunaCollectionOld) {
                if (!comunaCollectionNew.contains(comunaCollectionOldComuna)) {
                    comunaCollectionOldComuna.setRegion(null);
                    comunaCollectionOldComuna = em.merge(comunaCollectionOldComuna);
                }
            }
            for (Comuna comunaCollectionNewComuna : comunaCollectionNew) {
                if (!comunaCollectionOld.contains(comunaCollectionNewComuna)) {
                    Region oldRegionOfComunaCollectionNewComuna = comunaCollectionNewComuna.getRegion();
                    comunaCollectionNewComuna.setRegion(region);
                    comunaCollectionNewComuna = em.merge(comunaCollectionNewComuna);
                    if (oldRegionOfComunaCollectionNewComuna != null && !oldRegionOfComunaCollectionNewComuna.equals(region)) {
                        oldRegionOfComunaCollectionNewComuna.getComunaCollection().remove(comunaCollectionNewComuna);
                        oldRegionOfComunaCollectionNewComuna = em.merge(oldRegionOfComunaCollectionNewComuna);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = region.getId();
                if (findRegion(id) == null) {
                    throw new NonexistentEntityException("The region with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Region region;
            try {
                region = em.getReference(Region.class, id);
                region.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The region with id " + id + " no longer exists.", enfe);
            }
            Collection<Comuna> comunaCollection = region.getComunaCollection();
            for (Comuna comunaCollectionComuna : comunaCollection) {
                comunaCollectionComuna.setRegion(null);
                comunaCollectionComuna = em.merge(comunaCollectionComuna);
            }
            em.remove(region);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Region> findRegionEntities() {
        return findRegionEntities(true, -1, -1);
    }

    public List<Region> findRegionEntities(int maxResults, int firstResult) {
        return findRegionEntities(false, maxResults, firstResult);
    }

    private List<Region> findRegionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Region.class));
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

    public Region findRegion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Region.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Region> rt = cq.from(Region.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
