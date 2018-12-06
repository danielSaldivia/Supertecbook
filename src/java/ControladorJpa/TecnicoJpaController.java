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
import Model.TipoTecnico;
import Model.Solicitud;
import java.util.ArrayList;
import java.util.Collection;
import Model.Local;
import Model.Tecnico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yo
 */
public class TecnicoJpaController implements Serializable {

    public TecnicoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tecnico tecnico) throws RollbackFailureException, Exception {
        if (tecnico.getSolicitudCollection() == null) {
            tecnico.setSolicitudCollection(new ArrayList<Solicitud>());
        }
        if (tecnico.getLocalCollection() == null) {
            tecnico.setLocalCollection(new ArrayList<Local>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoTecnico especialidad = tecnico.getEspecialidad();
            if (especialidad != null) {
                especialidad = em.getReference(especialidad.getClass(), especialidad.getId());
                tecnico.setEspecialidad(especialidad);
            }
            Collection<Solicitud> attachedSolicitudCollection = new ArrayList<Solicitud>();
            for (Solicitud solicitudCollectionSolicitudToAttach : tecnico.getSolicitudCollection()) {
                solicitudCollectionSolicitudToAttach = em.getReference(solicitudCollectionSolicitudToAttach.getClass(), solicitudCollectionSolicitudToAttach.getId());
                attachedSolicitudCollection.add(solicitudCollectionSolicitudToAttach);
            }
            tecnico.setSolicitudCollection(attachedSolicitudCollection);
            Collection<Local> attachedLocalCollection = new ArrayList<Local>();
            for (Local localCollectionLocalToAttach : tecnico.getLocalCollection()) {
                localCollectionLocalToAttach = em.getReference(localCollectionLocalToAttach.getClass(), localCollectionLocalToAttach.getId());
                attachedLocalCollection.add(localCollectionLocalToAttach);
            }
            tecnico.setLocalCollection(attachedLocalCollection);
            em.persist(tecnico);
            if (especialidad != null) {
                especialidad.getTecnicoCollection().add(tecnico);
                especialidad = em.merge(especialidad);
            }
            for (Solicitud solicitudCollectionSolicitud : tecnico.getSolicitudCollection()) {
                Tecnico oldTecnicoOfSolicitudCollectionSolicitud = solicitudCollectionSolicitud.getTecnico();
                solicitudCollectionSolicitud.setTecnico(tecnico);
                solicitudCollectionSolicitud = em.merge(solicitudCollectionSolicitud);
                if (oldTecnicoOfSolicitudCollectionSolicitud != null) {
                    oldTecnicoOfSolicitudCollectionSolicitud.getSolicitudCollection().remove(solicitudCollectionSolicitud);
                    oldTecnicoOfSolicitudCollectionSolicitud = em.merge(oldTecnicoOfSolicitudCollectionSolicitud);
                }
            }
            for (Local localCollectionLocal : tecnico.getLocalCollection()) {
                Tecnico oldTecnicoOfLocalCollectionLocal = localCollectionLocal.getTecnico();
                localCollectionLocal.setTecnico(tecnico);
                localCollectionLocal = em.merge(localCollectionLocal);
                if (oldTecnicoOfLocalCollectionLocal != null) {
                    oldTecnicoOfLocalCollectionLocal.getLocalCollection().remove(localCollectionLocal);
                    oldTecnicoOfLocalCollectionLocal = em.merge(oldTecnicoOfLocalCollectionLocal);
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

    public void edit(Tecnico tecnico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tecnico persistentTecnico = em.find(Tecnico.class, tecnico.getId());
            TipoTecnico especialidadOld = persistentTecnico.getEspecialidad();
            TipoTecnico especialidadNew = tecnico.getEspecialidad();
            Collection<Solicitud> solicitudCollectionOld = persistentTecnico.getSolicitudCollection();
            Collection<Solicitud> solicitudCollectionNew = tecnico.getSolicitudCollection();
            Collection<Local> localCollectionOld = persistentTecnico.getLocalCollection();
            Collection<Local> localCollectionNew = tecnico.getLocalCollection();
            if (especialidadNew != null) {
                especialidadNew = em.getReference(especialidadNew.getClass(), especialidadNew.getId());
                tecnico.setEspecialidad(especialidadNew);
            }
            Collection<Solicitud> attachedSolicitudCollectionNew = new ArrayList<Solicitud>();
            for (Solicitud solicitudCollectionNewSolicitudToAttach : solicitudCollectionNew) {
                solicitudCollectionNewSolicitudToAttach = em.getReference(solicitudCollectionNewSolicitudToAttach.getClass(), solicitudCollectionNewSolicitudToAttach.getId());
                attachedSolicitudCollectionNew.add(solicitudCollectionNewSolicitudToAttach);
            }
            solicitudCollectionNew = attachedSolicitudCollectionNew;
            tecnico.setSolicitudCollection(solicitudCollectionNew);
            Collection<Local> attachedLocalCollectionNew = new ArrayList<Local>();
            for (Local localCollectionNewLocalToAttach : localCollectionNew) {
                localCollectionNewLocalToAttach = em.getReference(localCollectionNewLocalToAttach.getClass(), localCollectionNewLocalToAttach.getId());
                attachedLocalCollectionNew.add(localCollectionNewLocalToAttach);
            }
            localCollectionNew = attachedLocalCollectionNew;
            tecnico.setLocalCollection(localCollectionNew);
            tecnico = em.merge(tecnico);
            if (especialidadOld != null && !especialidadOld.equals(especialidadNew)) {
                especialidadOld.getTecnicoCollection().remove(tecnico);
                especialidadOld = em.merge(especialidadOld);
            }
            if (especialidadNew != null && !especialidadNew.equals(especialidadOld)) {
                especialidadNew.getTecnicoCollection().add(tecnico);
                especialidadNew = em.merge(especialidadNew);
            }
            for (Solicitud solicitudCollectionOldSolicitud : solicitudCollectionOld) {
                if (!solicitudCollectionNew.contains(solicitudCollectionOldSolicitud)) {
                    solicitudCollectionOldSolicitud.setTecnico(null);
                    solicitudCollectionOldSolicitud = em.merge(solicitudCollectionOldSolicitud);
                }
            }
            for (Solicitud solicitudCollectionNewSolicitud : solicitudCollectionNew) {
                if (!solicitudCollectionOld.contains(solicitudCollectionNewSolicitud)) {
                    Tecnico oldTecnicoOfSolicitudCollectionNewSolicitud = solicitudCollectionNewSolicitud.getTecnico();
                    solicitudCollectionNewSolicitud.setTecnico(tecnico);
                    solicitudCollectionNewSolicitud = em.merge(solicitudCollectionNewSolicitud);
                    if (oldTecnicoOfSolicitudCollectionNewSolicitud != null && !oldTecnicoOfSolicitudCollectionNewSolicitud.equals(tecnico)) {
                        oldTecnicoOfSolicitudCollectionNewSolicitud.getSolicitudCollection().remove(solicitudCollectionNewSolicitud);
                        oldTecnicoOfSolicitudCollectionNewSolicitud = em.merge(oldTecnicoOfSolicitudCollectionNewSolicitud);
                    }
                }
            }
            for (Local localCollectionOldLocal : localCollectionOld) {
                if (!localCollectionNew.contains(localCollectionOldLocal)) {
                    localCollectionOldLocal.setTecnico(null);
                    localCollectionOldLocal = em.merge(localCollectionOldLocal);
                }
            }
            for (Local localCollectionNewLocal : localCollectionNew) {
                if (!localCollectionOld.contains(localCollectionNewLocal)) {
                    Tecnico oldTecnicoOfLocalCollectionNewLocal = localCollectionNewLocal.getTecnico();
                    localCollectionNewLocal.setTecnico(tecnico);
                    localCollectionNewLocal = em.merge(localCollectionNewLocal);
                    if (oldTecnicoOfLocalCollectionNewLocal != null && !oldTecnicoOfLocalCollectionNewLocal.equals(tecnico)) {
                        oldTecnicoOfLocalCollectionNewLocal.getLocalCollection().remove(localCollectionNewLocal);
                        oldTecnicoOfLocalCollectionNewLocal = em.merge(oldTecnicoOfLocalCollectionNewLocal);
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
                Integer id = tecnico.getId();
                if (findTecnico(id) == null) {
                    throw new NonexistentEntityException("The tecnico with id " + id + " no longer exists.");
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
            Tecnico tecnico;
            try {
                tecnico = em.getReference(Tecnico.class, id);
                tecnico.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tecnico with id " + id + " no longer exists.", enfe);
            }
            TipoTecnico especialidad = tecnico.getEspecialidad();
            if (especialidad != null) {
                especialidad.getTecnicoCollection().remove(tecnico);
                especialidad = em.merge(especialidad);
            }
            Collection<Solicitud> solicitudCollection = tecnico.getSolicitudCollection();
            for (Solicitud solicitudCollectionSolicitud : solicitudCollection) {
                solicitudCollectionSolicitud.setTecnico(null);
                solicitudCollectionSolicitud = em.merge(solicitudCollectionSolicitud);
            }
            Collection<Local> localCollection = tecnico.getLocalCollection();
            for (Local localCollectionLocal : localCollection) {
                localCollectionLocal.setTecnico(null);
                localCollectionLocal = em.merge(localCollectionLocal);
            }
            em.remove(tecnico);
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

    public List<Tecnico> findTecnicoEntities() {
        return findTecnicoEntities(true, -1, -1);
    }

    public List<Tecnico> findTecnicoEntities(int maxResults, int firstResult) {
        return findTecnicoEntities(false, maxResults, firstResult);
    }

    private List<Tecnico> findTecnicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tecnico.class));
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

    public Tecnico findTecnico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tecnico.class, id);
        } finally {
            em.close();
        }
    }

    public int getTecnicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tecnico> rt = cq.from(Tecnico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
