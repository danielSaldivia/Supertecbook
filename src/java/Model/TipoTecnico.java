/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "tipo_tecnico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTecnico.findAll", query = "SELECT t FROM TipoTecnico t")
    , @NamedQuery(name = "TipoTecnico.findById", query = "SELECT t FROM TipoTecnico t WHERE t.id = :id")
    , @NamedQuery(name = "TipoTecnico.findByEspecialidad", query = "SELECT t FROM TipoTecnico t WHERE t.especialidad = :especialidad")})
public class TipoTecnico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "Especialidad")
    private String especialidad;
    @OneToMany(mappedBy = "especialidad")
    private Collection<Tecnico> tecnicoCollection;

    public TipoTecnico() {
    }

    public TipoTecnico(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @XmlTransient
    public Collection<Tecnico> getTecnicoCollection() {
        return tecnicoCollection;
    }

    public void setTecnicoCollection(Collection<Tecnico> tecnicoCollection) {
        this.tecnicoCollection = tecnicoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTecnico)) {
            return false;
        }
        TipoTecnico other = (TipoTecnico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.TipoTecnico[ id=" + id + " ]";
    }
    
}
