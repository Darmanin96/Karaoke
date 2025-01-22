package org.example.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Canciones {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty titulo = new SimpleStringProperty();
    private StringProperty artista = new SimpleStringProperty();

    public Canciones(int id, String titulo, String artista) {
        this.id.set(id);
        this.titulo.set(titulo);
        this.artista.set(artista);

    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitulo() {
        return titulo.get();
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public String getArtista() {
        return artista.get();
    }

    public StringProperty artistaProperty() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista.set(artista);
    }
}
