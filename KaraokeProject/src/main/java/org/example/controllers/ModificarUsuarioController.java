package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.example.models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class ModificarUsuarioController implements Initializable {

    @FXML
    private Button Aceptar;

    @FXML
    private Button Limpiar;

    @FXML
    private TextField contraseña;

    @FXML
    private TextField email;

    @FXML
    private BorderPane root;

    private UsuariosController usuariosController;

    private Usuario usuarioSeleccionado;

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;

        if (usuarioSeleccionado != null) {
            email.setText(usuarioSeleccionado.getEmail());
        }
    }

    public void setUsuariosController(UsuariosController usuariosController) {
        this.usuariosController = usuariosController;
    }

    @FXML
    void onAceptarAction(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún usuario");
            alert.setContentText("Por favor, selecciona un usuario antes de continuar.");
            alert.showAndWait();
            return;
        }

        String nuevoEmail = email.getText().trim();
        String nuevaContrasena = contraseña.getText().trim();

        if (nuevoEmail.isEmpty() || nuevaContrasena.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, complete todos los campos.");
            alert.showAndWait();
            return;
        }

        // Actualizar el usuario
        actualizarUsuario(usuarioSeleccionado.getId(), nuevoEmail, nuevaContrasena);

        // Actualizar la tabla en el controlador principal
        if (usuariosController != null) {
            usuariosController.cargarTablaAlumno();
        }

        // Cerrar la ventana
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText("Usuario actualizado");
        alert.setContentText("El usuario se actualizó correctamente.");
        alert.showAndWait();

        root.getScene().getWindow().hide();
    }

    public void actualizarUsuario(int id, String nuevoEmail, String nuevaContrasena) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Cifrar la contraseña
            String contrasenaCifrada = org.mindrot.jbcrypt.BCrypt.hashpw(nuevaContrasena, org.mindrot.jbcrypt.BCrypt.gensalt());

            // Actualizar el usuario
            String hql = "UPDATE Usuarios u SET u.email = :nuevoEmail, u.contrasena = :nuevaContrasena WHERE u.id = :id";
            int updatedCount = em.createQuery(hql)
                    .setParameter("nuevoEmail", nuevoEmail)
                    .setParameter("nuevaContrasena", contrasenaCifrada)
                    .setParameter("id", id)
                    .executeUpdate();

            em.getTransaction().commit();

            if (updatedCount > 0) {
                System.out.println("Usuario actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún usuario con el ID especificado.");
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
    void onLimpiarAction(ActionEvent event) {
        contraseña.clear();
        email.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización, si es necesaria
    }


    public Button getAceptar() {
        return Aceptar;
    }

    public void setAceptar(Button aceptar) {
        Aceptar = aceptar;
    }

    public Button getLimpiar() {
        return Limpiar;
    }

    public void setLimpiar(Button limpiar) {
        Limpiar = limpiar;
    }

    public TextField getContraseña() {
        return contraseña;
    }

    public void setContraseña(TextField contraseña) {
        this.contraseña = contraseña;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public UsuariosController getUsuariosController() {
        return usuariosController;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }
}
