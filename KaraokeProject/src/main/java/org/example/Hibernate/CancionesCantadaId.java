package org.example.Hibernate;

import java.io.Serializable;
import java.util.Objects;

public class CancionesCantadaId implements Serializable {

    private Canciones cancion;
    private String titulo;

    public CancionesCantadaId() {
    }

    public CancionesCantadaId(Canciones cancion, String titulo) {
        this.cancion = cancion;
        this.titulo = titulo;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancionesCantadaId that = (CancionesCantadaId) o;
        return Objects.equals(cancion, that.cancion) && Objects.equals(titulo, that.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cancion, titulo);
    }
}
