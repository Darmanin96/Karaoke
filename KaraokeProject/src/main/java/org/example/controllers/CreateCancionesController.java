package org.example.controllers;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.Hibernate.Canciones;

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


    private void cerrar() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onCancelarAction(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText("Confirmación");
        alerta.setContentText("¿Estás seguro?");
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == botonSi) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        } else {

        }
    }

    @FXML
    void onLimpiarAction(ActionEvent event) {
        titulo.clear();
        artista.clear();
    }


    @FXML
    void onAceptarAction(ActionEvent event) {
            String tituloCambio = this.titulo.getText().trim();
            String artistaCambio = this.artista.getText().trim();
            if (tituloCambio.isEmpty() || artistaCambio.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Por favor, introducir todos los campos");
                alerta.setHeaderText(null);
                alerta.show();
            }

            UsuarioRepository usuarioRepository = new UsuarioRepository();
            if (usuarioRepository.existeCancion(tituloCambio, artistaCambio)) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("El título o el artista ya existe");
                alerta.show();
            }else {
                Canciones canciones = new Canciones();
                canciones.setTitulo(tituloCambio);
                canciones.setArtista(artistaCambio);
                usuarioRepository.guardarCancion(canciones);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Confirmado");
                alerta.setHeaderText("Cancion guardada con exito");
                alerta.show();
                cerrar();
            }
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
