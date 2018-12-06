/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yo
 */
@Entity
@Table(name = "tecnico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tecnico.findAll", query = "SELECT t FROM Tecnico t")
    , @NamedQuery(name = "Tecnico.findById", query = "SELECT t FROM Tecnico t WHERE t.id = :id")
    , @NamedQuery(name = "Tecnico.findByRut", query = "SELECT t FROM Tecnico t WHERE t.rut = :rut")
    , @NamedQuery(name = "Tecnico.findByCorreo", query = "SELECT t FROM Tecnico t WHERE t.correo = :correo")
    , @NamedQuery(name = "Tecnico.findByNombre", query = "SELECT t FROM Tecnico t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Tecnico.findByUsuario", query = "SELECT t FROM Tecnico t WHERE t.usuario = :usuario")
    , @NamedQuery(name = "Tecnico.findByContrasenia", query = "SELECT t FROM Tecnico t WHERE t.contrasenia = :contrasenia")
    , @NamedQuery(name = "Tecnico.findByFechaNacimiento", query = "SELECT t FROM Tecnico t WHERE t.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Tecnico.findByTelefono", query = "SELECT t FROM Tecnico t WHERE t.telefono = :telefono")})
public class Tecnico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "Rut")
    private String rut;
    @Size(max = 50)
    @Column(name = "Correo")
    private String correo;
    @Size(max = 50)
    @Column(name = "Nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "Usuario")
    private String usuario;
    @Size(max = 50)
    @Column(name = "Contrasenia")
    private String contrasenia;
    @Column(name = "Fecha_Nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Size(max = 50)
    @Column(name = "Telefono")
    private String telefono;
    @OneToMany(mappedBy = "tecnico")
    private Collection<Solicitud> solicitudCollection;
    @JoinColumn(name = "Especialidad", referencedColumnName = "Id")
    @ManyToOne
    private TipoTecnico especialidad;
    @OneToMany(mappedBy = "tecnico")
    private Collection<Local> localCollection;

    public Tecnico() {
    }

    public Tecnico(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public Collection<Solicitud> getSolicitudCollection() {
        return solicitudCollection;
    }

    public void setSolicitudCollection(Collection<Solicitud> solicitudCollection) {
        this.solicitudCollection = solicitudCollection;
    }

    public TipoTecnico getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(TipoTecnico especialidad) {
        this.especialidad = especialidad;
    }

    @XmlTransient
    public Collection<Local> getLocalCollection() {
        return localCollection;
    }

    public void setLocalCollection(Collection<Local> localCollection) {
        this.localCollection = localCollection;
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
        if (!(object instanceof Tecnico)) {
            return false;
        }
        Tecnico other = (Tecnico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Tecnico[ id=" + id + " ]";
    }
    
}
