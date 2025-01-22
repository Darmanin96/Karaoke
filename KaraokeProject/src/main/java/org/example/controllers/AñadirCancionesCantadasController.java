package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AñadirCancionesCantadasController implements Initializable {

    @FXML
    private Button aceptar;

    @FXML
    private Button cancelar;

    @FXML
    private Spinner<Integer> escuchar;

    @FXML
    private Button limpiar;

    @FXML
    private BorderPane root;

    @FXML
    private TextField titulo;

    @FXML
    private TextField artista;

    @FXML
    void onAceptarAction(ActionEvent event) {
            String tituloCambio = titulo.getText().trim();
            Integer escucharCambio = escuchar.getValue().intValue();
            String artistaCambio = artista.getText().trim();
            if (tituloCambio.isEmpty() || escucharCambio <0 || artistaCambio.isEmpty() ) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("Por favor ingrese todos los campos");
                alerta.show();
            }




    }

    @FXML
    void onCancelarAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onLimpiarAction(ActionEvent event) {
        escuchar.getValueFactory().setValue(1);
        titulo.clear();
        artista.clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        escuchar.setValueFactory(valueFactory);
    }


    public Button getAceptar() {
        return aceptar;
    }

    public void setAceptar(Button aceptar) {
        this.aceptar = aceptar;
    }

    public Button getCancelar() {
        return cancelar;
    }

    public void setCancelar(Button cancelar) {
        this.cancelar = cancelar;
    }

    public Spinner<Integer> getEscuchar() {
        return escuchar;
    }

    public void setEscuchar(Spinner<Integer> escuchar) {
        this.escuchar = escuchar;
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
