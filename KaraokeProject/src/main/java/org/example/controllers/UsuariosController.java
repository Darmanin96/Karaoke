package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.Hibernate.Usuarios;
import org.example.models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsuariosController implements Initializable {

    private String correoAutenticado;

    @FXML
    private TableColumn<Usuario, Integer> id;

    @FXML
    private TableColumn<Usuario, String> email;

    @FXML
    private TableColumn<Usuario, String> nombre;

    @FXML
    private BorderPane root;

    @FXML
    private Button desconectar;

    @FXML
    private Button eliminar;

    @FXML
    private Button modificar;

    @FXML
    private TableView<Usuario> tableUsuarios;

    private ObservableList<Usuario> usuariosList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modificar.setDisable(true);
        eliminar.setDisable(true);
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        cargarTablaAlumno();

        tableUsuarios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                verificarUsuarioSeleccionado(newValue);
            }
        });

        tableUsuarios.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
                if (usuarioSeleccionado != null) {
                    if (usuarioSeleccionado.getEmail().equals(correoAutenticado)) {
                        DosClipsSeleccionado(usuarioSeleccionado);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Advertencia");
                        alert.setHeaderText("Acción no permitida");
                        alert.setContentText("No puedes realizar esta acción sobre un usuario que no es el autenticado.");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    private void verificarUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        if (usuarioSeleccionado.getEmail().equals(correoAutenticado)) {
            modificar.setDisable(false);
            eliminar.setDisable(false);
        } else {
            modificar.setDisable(true);
            eliminar.setDisable(true);
        }
    }

    // Acción al hacer doble clic en un usuario
    public void DosClipsSeleccionado(Usuario usuario) {
        if (usuario != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Canciones.fxml"));
                Parent root = loader.load();
                CancionesCantadasController cancionesCantadasController = loader.getController();
                if (cancionesCantadasController != null) {
                    cancionesCantadasController.setUsuarioId(usuario.getId());
                } else {
                    System.err.println("El controlador CancionesCantadasController es null");
                }

                // Cambiar la escena al nuevo FXML
                Stage currentStage = (Stage) tableUsuarios.getScene().getWindow();
                Scene newScene = new Scene(root);
                currentStage.setScene(newScene);
                currentStage.setTitle("Ver estadísticas");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún usuario");
            alert.setContentText("Por favor, selecciona un usuario de la tabla para modificar.");
            alert.showAndWait();
        }
    }

    @FXML
    void onDesconectarAction(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText("Confirmación");
        alerta.setContentText("¿Estás seguro que desea desconectarse?");
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == botonSi) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Loggin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) desconectar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onEliminarAction(ActionEvent event) {
        Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setHeaderText("Confirmación");
            alerta.setContentText("¿Estás seguro que deseas eliminar este usuario?");
            ButtonType botonSi = new ButtonType("Sí");
            ButtonType botonNo = new ButtonType("No");
            alerta.getButtonTypes().setAll(botonSi, botonNo);
            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == botonSi) {
                eliminarUsuario(usuarioSeleccionado);
                cargarTablaAlumno();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("No se ha seleccionado ningún usuario");
            alerta.setContentText("Por favor, selecciona un usuario de la tabla para eliminar.");
            alerta.showAndWait();
        }
    }

    private void eliminarUsuario(Usuario usuario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Usuarios usuarioHibernate = em.find(Usuarios.class, usuario.getId());
            if (usuarioHibernate != null) {
                em.remove(usuarioHibernate);
                em.getTransaction().commit();
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("No se encontró el usuario en la base de datos.");
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    @FXML
    void onModificarAction(ActionEvent event) {
        cargar();
    }

    public void cargar() {
        Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModificarUsuario.fxml"));
                Parent root = loader.load();
                ModificarUsuarioController modificarUsuarioController = loader.getController();
                modificarUsuarioController.setUsuarioSeleccionado(usuarioSeleccionado);
                modificarUsuarioController.setUsuariosController(this);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Modificar Usuario");
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún usuario");
            alert.setContentText("Por favor, selecciona un usuario de la tabla para modificar.");
            alert.showAndWait();
        }
    }

    public void cargarTablaAlumno() {
        usuariosList.clear();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "SELECT u FROM Usuarios u";
            TypedQuery<Usuarios> query = em.createQuery(jpql, Usuarios.class);
            List<Usuarios> result = query.getResultList();
            for (Usuarios usuarioHibernate : result) {
                Usuario usuario = new Usuario(
                        usuarioHibernate.getId(),
                        usuarioHibernate.getNombre(),
                        usuarioHibernate.getEmail()
                );
                usuariosList.add(usuario);
            }

            tableUsuarios.setItems(usuariosList);
            tableUsuarios.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos con Hibernate.");
        } finally {
            em.close();
            emf.close();
        }
    }

    // Setters y Getters
    public String getCorreoAutenticado() {
        return correoAutenticado;
    }

    public void setCorreoAutenticado(String correoAutenticado) {
        this.correoAutenticado = correoAutenticado;
    }

    public TableColumn<Usuario, Integer> getId() {
        return id;
    }

    public void setId(TableColumn<Usuario, Integer> id) {
        this.id = id;
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

    public Button getDesconectar() {
        return desconectar;
    }

    public void setDesconectar(Button desconectar) {
        this.desconectar = desconectar;
    }

    public Button getEliminar() {
        return eliminar;
    }

    public void setEliminar(Button eliminar) {
        this.eliminar = eliminar;
    }

    public Button getModificar() {
        return modificar;
    }

    public void setModificar(Button modificar) {
        this.modificar = modificar;
    }

    public TableView<Usuario> getTableUsuarios() {
        return tableUsuarios;
    }

    public void setTableUsuarios(TableView<Usuario> tableUsuarios) {
        this.tableUsuarios = tableUsuarios;
    }

    public ObservableList<Usuario> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(ObservableList<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
    }
}
