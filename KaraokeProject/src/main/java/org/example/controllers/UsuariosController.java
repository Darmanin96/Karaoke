package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.example.Conexion.UsuarioRepository;
import org.example.Hibernate.Usuarios;
import org.example.models.Usuario;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsuariosController implements Initializable {

    @FXML
    private TableColumn<Usuario, String> email;

    @FXML
    private TableColumn<Usuario, String> nombre;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Usuarios> tableUsuarios;

    private UsuarioRepository usuarioRepository = new UsuarioRepository();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        cargarUsuarios();

    }

    private void cargarUsuarios() {
        List<Usuarios> listaUsuarios = usuarioRepository.obtenerUsuarios();
        ObservableList<Usuarios> usuariosObservableList = FXCollections.observableArrayList(listaUsuarios);
        tableUsuarios.setItems(usuariosObservableList);
    }

    public TableColumn<Usuario, String> getEmail() {
        return email;
    }

    public void setEmail(TableColumn<Usuario, String> email) {
        this.email = email;
    }

    public TableColumn<Usuario, String> getNombre() {
        return nombre;
    }

    public void setNombre(TableColumn<Usuario, String> nombre) {
        this.nombre = nombre;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public TableView<Usuarios> getTableUsuarios() {
        return tableUsuarios;
    }

    public void setTableUsuarios(TableView<Usuarios> tableUsuarios) {
        this.tableUsuarios = tableUsuarios;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
}
