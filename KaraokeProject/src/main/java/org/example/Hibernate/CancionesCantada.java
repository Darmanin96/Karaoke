package org.example.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "canciones_cantadas")
@IdClass(CancionesCantadaId.class)
public class CancionesCantada {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancion_id", referencedColumnName = "id")
    private Canciones cancion;

    @Id
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "veces_cantada")
    private int vecesCantada = 1;

    @Column(name = "id_usuario")
    private int idUsuario;

    // Getters y setters
    public Canciones getCancion() {
        return cancion;
    }

    public void setCancion(Canciones cancion) {
        this.cancion = cancion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getVecesCantada() {
        return vecesCantada;
    }

    public void setVecesCantada(int vecesCantada) {
        this.vecesCantada = vecesCantada;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
