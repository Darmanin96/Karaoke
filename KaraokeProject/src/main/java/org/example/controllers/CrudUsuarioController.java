package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.Conexion.UsuarioRepository;
import org.example.Hibernate.Usuarios;
import org.mindrot.jbcrypt.BCrypt;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CrudUsuarioController implements Initializable {

    @FXML
    private Button Aceptar;

    @FXML
    private PasswordField Contraseña;

    @FXML
    private TextField Email;

    @FXML
    private Button Limpiar;

    @FXML
    private TextField Nombre;

    @FXML
    private BorderPane root;

    @FXML
    private Button Cancelar;

    private void cerrar() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private boolean esEmailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return Pattern.matches(regex, email);
    }

    @FXML
    void onAceptarAction(ActionEvent event) {
        String nombre = Nombre.getText().trim();
        String email = Email.getText().trim();
        String contrasena = Contraseña.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Por favor, introducir todos los campos");
            return;
        }

        if (!esEmailValido(email)) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Por favor, introducir un email válido");
            alerta.show();
            return;
        }

        UsuarioRepository usuarioRepository = new UsuarioRepository();
        if (usuarioRepository.existeNombreOEmail(nombre, email)) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error");
            alerta.setHeaderText("El nombre o el email ya existe");
            alerta.show();
        } else {
            String contrasenaCifrada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
            Usuarios usuario = new Usuarios();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setContrasena(contrasenaCifrada);
            usuarioRepository.guardarUsuario(usuario);
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Confirmacion");
            alerta.setHeaderText("Usuario guardado");
            alerta.show();
            cerrar();
        }
    }


    @FXML
    void onLimpiarAction(ActionEvent event) {
            Nombre.clear();
            Email.clear();
            Contraseña.clear();
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





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Button getAceptar() {
        return Aceptar;
    }

    public void setAceptar(Button aceptar) {
        Aceptar = aceptar;
    }

    public PasswordField getContraseña() {
        return Contraseña;
    }

    public void setContraseña(PasswordField contraseña) {
        Contraseña = contraseña;
    }

    public TextField getEmail() {
        return Email;
    }

    public void setEmail(TextField email) {
        Email = email;
    }

    public Button getLimpiar() {
        return Limpiar;
    }

    public void setLimpiar(Button limpiar) {
        Limpiar = limpiar;
    }

    public TextField getNombre() {
        return Nombre;
    }

    public void setNombre(TextField nombre) {
        Nombre = nombre;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
}
