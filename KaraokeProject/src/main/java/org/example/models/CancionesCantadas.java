package org.example.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Date;

public class CancionesCantadas {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty titulo = new SimpleStringProperty();
    private ObjectProperty<Date> fecha = new SimpleObjectProperty<>();
    private IntegerProperty vecesCantada = new SimpleIntegerProperty(1);
    private IntegerProperty idUsuario = new SimpleIntegerProperty();

    // Constructor
    public CancionesCantadas(int id, String titulo, Date fecha, int vecesCantada) {
        this.id.set(id);
        this.titulo.set(titulo);
        this.fecha.set(fecha);
        this.vecesCantada.set(vecesCantada);
    }


    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public Date getFecha() {
        return fecha.get();
    }

    public void setFecha(Date fecha) {
        this.fecha.set(fecha);
    }

    public ObjectProperty<Date> fechaProperty() {
        return fecha;
    }

    public int getVecesCantada() {
        return vecesCantada.get();
    }

    public void setVecesCantada(int vecesCantada) {
        this.vecesCantada.set(vecesCantada);
    }

    public IntegerProperty vecesCantadaProperty() {
        return vecesCantada;
    }

    public int getIdUsuario() {
        return idUsuario.get();
    }

    public IntegerProperty idUsuarioProperty() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario.set(idUsuario);
    }
}
