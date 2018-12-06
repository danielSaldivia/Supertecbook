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
import Model.Tecnico;
import Model.TipoTecnico;
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
public class TipoTecnicoJpaController implements Serializable {

    public TipoTecnicoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoTecnico tipoTecnico) throws RollbackFailureException, Exception {
        if (tipoTecnico.getTecnicoCollection() == null) {
            tipoTecnico.setTecnicoCollection(new ArrayList<Tecnico>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Tecnico> attachedTecnicoCollection = new ArrayList<Tecnico>();
            for (Tecnico tecnicoCollectionTecnicoToAttach : tipoTecnico.getTecnicoCollection()) {
                tecnicoCollectionTecnicoToAttach = em.getReference(tecnicoCollectionTecnicoToAttach.getClass(), tecnicoCollectionTecnicoToAttach.getId());
                attachedTecnicoCollection.add(tecnicoCollectionTecnicoToAttach);
            }
            tipoTecnico.setTecnicoCollection(attachedTecnicoCollection);
            em.persist(tipoTecnico);
            for (Tecnico tecnicoCollectionTecnico : tipoTecnico.getTecnicoCollection()) {
                TipoTecnico oldEspecialidadOfTecnicoCollectionTecnico = tecnicoCollectionTecnico.getEspecialidad();
                tecnicoCollectionTecnico.setEspecialidad(tipoTecnico);
                tecnicoCollectionTecnico = em.merge(tecnicoCollectionTecnico);
                if (oldEspecialidadOfTecnicoCollectionTecnico != null) {
                    oldEspecialidadOfTecnicoCollectionTecnico.getTecnicoCollection().remove(tecnicoCollectionTecnico);
                    oldEspecialidadOfTecnicoCollectionTecnico = em.merge(oldEspecialidadOfTecnicoCollectionTecnico);
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

    public void edit(TipoTecnico tipoTecnico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoTecnico persistentTipoTecnico = em.find(TipoTecnico.class, tipoTecnico.getId());
            Collection<Tecnico> tecnicoCollectionOld = persistentTipoTecnico.getTecnicoCollection();
            Collection<Tecnico> tecnicoCollectionNew = tipoTecnico.getTecnicoCollection();
            Collection<Tecnico> attachedTecnicoCollectionNew = new ArrayList<Tecnico>();
            for (Tecnico tecnicoCollectionNewTecnicoToAttach : tecnicoCollectionNew) {
                tecnicoCollectionNewTecnicoToAttach = em.getReference(tecnicoCollectionNewTecnicoToAttach.getClass(), tecnicoCollectionNewTecnicoToAttach.getId());
                attachedTecnicoCollectionNew.add(tecnicoCollectionNewTecnicoToAttach);
            }
            tecnicoCollectionNew = attachedTecnicoCollectionNew;
            tipoTecnico.setTecnicoCollection(tecnicoCollectionNew);
            tipoTecnico = em.merge(tipoTecnico);
            for (Tecnico tecnicoCollectionOldTecnico : tecnicoCollectionOld) {
                if (!tecnicoCollectionNew.contains(tecnicoCollectionOldTecnico)) {
                    tecnicoCollectionOldTecnico.setEspecialidad(null);
                    tecnicoCollectionOldTecnico = em.merge(tecnicoCollectionOldTecnico);
                }
            }
            for (Tecnico tecnicoCollectionNewTecnico : tecnicoCollectionNew) {
                if (!tecnicoCollectionOld.contains(tecnicoCollectionNewTecnico)) {
                    TipoTecnico oldEspecialidadOfTecnicoCollectionNewTecnico = tecnicoCollectionNewTecnico.getEspecialidad();
                    tecnicoCollectionNewTecnico.setEspecialidad(tipoTecnico);
                    tecnicoCollectionNewTecnico = em.merge(tecnicoCollectionNewTecnico);
                    if (oldEspecialidadOfTecnicoCollectionNewTecnico != null && !oldEspecialidadOfTecnicoCollectionNewTecnico.equals(tipoTecnico)) {
                        oldEspecialidadOfTecnicoCollectionNewTecnico.getTecnicoCollection().remove(tecnicoCollectionNewTecnico);
                        oldEspecialidadOfTecnicoCollectionNewTecnico = em.merge(oldEspecialidadOfTecnicoCollectionNewTecnico);
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
                Integer id = tipoTecnico.getId();
                if (findTipoTecnico(id) == null) {
                    throw new NonexistentEntityException("The tipoTecnico with id " + id + " no longer exists.");
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
            TipoTecnico tipoTecnico;
            try {
                tipoTecnico = em.getReference(TipoTecnico.class, id);
                tipoTecnico.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoTecnico with id " + id + " no longer exists.", enfe);
            }
            Collection<Tecnico> tecnicoCollection = tipoTecnico.getTecnicoCollection();
            for (Tecnico tecnicoCollectionTecnico : tecnicoCollection) {
                tecnicoCollectionTecnico.setEspecialidad(null);
                tecnicoCollectionTecnico = em.merge(tecnicoCollectionTecnico);
            }
            em.remove(tipoTecnico);
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

    public List<TipoTecnico> findTipoTecnicoEntities() {
        return findTipoTecnicoEntities(true, -1, -1);
    }

    public List<TipoTecnico> findTipoTecnicoEntities(int maxResults, int firstResult) {
        return findTipoTecnicoEntities(false, maxResults, firstResult);
    }

    private List<TipoTecnico> findTipoTecnicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoTecnico.class));
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

    public TipoTecnico findTipoTecnico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoTecnico.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoTecnicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoTecnico> rt = cq.from(TipoTecnico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
