/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladorJpa;

import ControladorJpa.exceptions.NonexistentEntityException;
import ControladorJpa.exceptions.RollbackFailureException;
import Model.Comuna;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Region;
import Model.Local;
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
public class ComunaJpaController implements Serializable {

    public ComunaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comuna comuna) throws RollbackFailureException, Exception {
        if (comuna.getLocalCollection() == null) {
            comuna.setLocalCollection(new ArrayList<Local>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Region region = comuna.getRegion();
            if (region != null) {
                region = em.getReference(region.getClass(), region.getId());
                comuna.setRegion(region);
            }
            Collection<Local> attachedLocalCollection = new ArrayList<Local>();
            for (Local localCollectionLocalToAttach : comuna.getLocalCollection()) {
                localCollectionLocalToAttach = em.getReference(localCollectionLocalToAttach.getClass(), localCollectionLocalToAttach.getId());
                attachedLocalCollection.add(localCollectionLocalToAttach);
            }
            comuna.setLocalCollection(attachedLocalCollection);
            em.persist(comuna);
            if (region != null) {
                region.getComunaCollection().add(comuna);
                region = em.merge(region);
            }
            for (Local localCollectionLocal : comuna.getLocalCollection()) {
                Comuna oldComunaOfLocalCollectionLocal = localCollectionLocal.getComuna();
                localCollectionLocal.setComuna(comuna);
                localCollectionLocal = em.merge(localCollectionLocal);
                if (oldComunaOfLocalCollectionLocal != null) {
                    oldComunaOfLocalCollectionLocal.getLocalCollection().remove(localCollectionLocal);
                    oldComunaOfLocalCollectionLocal = em.merge(oldComunaOfLocalCollectionLocal);
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

    public void edit(Comuna comuna) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comuna persistentComuna = em.find(Comuna.class, comuna.getId());
            Region regionOld = persistentComuna.getRegion();
            Region regionNew = comuna.getRegion();
            Collection<Local> localCollectionOld = persistentComuna.getLocalCollection();
            Collection<Local> localCollectionNew = comuna.getLocalCollection();
            if (regionNew != null) {
                regionNew = em.getReference(regionNew.getClass(), regionNew.getId());
                comuna.setRegion(regionNew);
            }
            Collection<Local> attachedLocalCollectionNew = new ArrayList<Local>();
            for (Local localCollectionNewLocalToAttach : localCollectionNew) {
                localCollectionNewLocalToAttach = em.getReference(localCollectionNewLocalToAttach.getClass(), localCollectionNewLocalToAttach.getId());
                attachedLocalCollectionNew.add(localCollectionNewLocalToAttach);
            }
            localCollectionNew = attachedLocalCollectionNew;
            comuna.setLocalCollection(localCollectionNew);
            comuna = em.merge(comuna);
            if (regionOld != null && !regionOld.equals(regionNew)) {
                regionOld.getComunaCollection().remove(comuna);
                regionOld = em.merge(regionOld);
            }
            if (regionNew != null && !regionNew.equals(regionOld)) {
                regionNew.getComunaCollection().add(comuna);
                regionNew = em.merge(regionNew);
            }
            for (Local localCollectionOldLocal : localCollectionOld) {
                if (!localCollectionNew.contains(localCollectionOldLocal)) {
                    localCollectionOldLocal.setComuna(null);
                    localCollectionOldLocal = em.merge(localCollectionOldLocal);
                }
            }
            for (Local localCollectionNewLocal : localCollectionNew) {
                if (!localCollectionOld.contains(localCollectionNewLocal)) {
                    Comuna oldComunaOfLocalCollectionNewLocal = localCollectionNewLocal.getComuna();
                    localCollectionNewLocal.setComuna(comuna);
                    localCollectionNewLocal = em.merge(localCollectionNewLocal);
                    if (oldComunaOfLocalCollectionNewLocal != null && !oldComunaOfLocalCollectionNewLocal.equals(comuna)) {
                        oldComunaOfLocalCollectionNewLocal.getLocalCollection().remove(localCollectionNewLocal);
                        oldComunaOfLocalCollectionNewLocal = em.merge(oldComunaOfLocalCollectionNewLocal);
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
                Integer id = comuna.getId();
                if (findComuna(id) == null) {
                    throw new NonexistentEntityException("The comuna with id " + id + " no longer exists.");
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
            Comuna comuna;
            try {
                comuna = em.getReference(Comuna.class, id);
                comuna.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comuna with id " + id + " no longer exists.", enfe);
            }
            Region region = comuna.getRegion();
            if (region != null) {
                region.getComunaCollection().remove(comuna);
                region = em.merge(region);
            }
            Collection<Local> localCollection = comuna.getLocalCollection();
            for (Local localCollectionLocal : localCollection) {
                localCollectionLocal.setComuna(null);
                localCollectionLocal = em.merge(localCollectionLocal);
            }
            em.remove(comuna);
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

    public List<Comuna> findComunaEntities() {
        return findComunaEntities(true, -1, -1);
    }

    public List<Comuna> findComunaEntities(int maxResults, int firstResult) {
        return findComunaEntities(false, maxResults, firstResult);
    }

    private List<Comuna> findComunaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comuna.class));
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

    public Comuna findComuna(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comuna.class, id);
        } finally {
            em.close();
        }
    }

    public int getComunaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comuna> rt = cq.from(Comuna.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
