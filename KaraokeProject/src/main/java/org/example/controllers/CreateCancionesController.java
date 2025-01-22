package org.example.controllers;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.*;
import java.util.*;

public class CreateCancionesController implements Initializable {


    @FXML
    private Button aceptar;

    @FXML
    private TextField artista;

    @FXML
    private Button cancelar;

    @FXML
    private Button limpiar;

    @FXML
    private BorderPane root;

    @FXML
    private TextField titulo;

    @FXML
    void onCancelarAction(ActionEvent event) {
            
    }

    @FXML
    void onLimpiarAction(ActionEvent event) {

    }


    @FXML
    void onAceptarAction(ActionEvent event) {

    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public Button getAceptar() {
        return aceptar;
    }

    public void setAceptar(Button aceptar) {
        this.aceptar = aceptar;
    }

    public TextField getArtista() {
        return artista;
    }

    public void setArtista(TextField artista) {
        this.artista = artista;
    }

    public Button getCancelar() {
        return cancelar;
    }

    public void setCancelar(Button cancelar) {
        this.cancelar = cancelar;
    }

    public Button getLimpiar() {
        return limpiar;
    }

    public void setLimpiar(Button limpiar) {
        this.limpiar = limpiar;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public TextField getTitulo() {
        return titulo;
    }

    public void setTitulo(TextField titulo) {
        this.titulo = titulo;
    }
}
