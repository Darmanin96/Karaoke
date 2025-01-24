package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.persistence.*;
import org.example.Hibernate.*;
import org.example.Hibernate.Canciones;
import org.example.models.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AñadirCancionesCantadasController implements Initializable {

    @FXML
    private Button aceptar;

    @FXML
    private Button cancelar;

    @FXML
    private ChoiceBox<String> cancion;

    @FXML
    private Spinner<Integer> escuchar;

    @FXML
    private Button limpiar;

    @FXML
    private BorderPane root;

    private Integer usuarioId;


    @FXML
    void onAceptarAction(ActionEvent event) {
        String tituloCancion = cancion.getValue(); // Título seleccionado
        int vecesCantada = escuchar.getValue(); // Veces cantada
        System.out.println(vecesCantada);
        System.out.println(tituloCancion);
        System.out.println(usuarioId);

        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        if (tituloCancion != null && !tituloCancion.isEmpty() && usuarioId != null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();
                String jpql = "SELECT c.id FROM Canciones c WHERE c.titulo = :titulo";
                TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
                query.setParameter("titulo", tituloCancion);

                Integer cancionId = query.getSingleResult();

                // Obtener la canción desde la base de datos antes de asociarla
                Canciones cancion = em.find(Canciones.class, cancionId);

                if (cancion != null) {
                    // Crear y persistir la CancionesCantada
                    CancionesCantada cancionesCantada = new CancionesCantada();
                    cancionesCantada.setTitulo(tituloCancion);
                    cancionesCantada.setVecesCantada(vecesCantada);
                    cancionesCantada.setIdUsuario(usuarioId);
                    cancionesCantada.setFecha(sqlDate);
                    cancionesCantada.setCancion(cancion);  // Asignamos el objeto Canciones

                    em.persist(cancionesCantada);  // Guardamos la entidad CancionesCantada
                    em.getTransaction().commit();  // Confirmamos la transacción
                    System.out.println("Inserción realizada con éxito en canciones_cantadas.");
                } else {
                    System.err.println("Canción no encontrada en la base de datos.");
                    em.getTransaction().rollback();  // Deshacemos la transacción si no se encuentra la canción
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error al insertar en canciones_cantadas.");
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback(); // Deshacer la transacción en caso de error
                }
            } finally {
                em.close();
                emf.close();
            }
        } else {
            System.err.println("No se ha proporcionado toda la información necesaria.");
        }
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
        }
    }

    public void cargarTituloCancionesCantadas() {
        ObservableList<String> listaCanciones = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT titulo FROM canciones";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                listaCanciones.add(rs.getString("titulo"));
            }
            cancion.setItems(listaCanciones);
            if (!listaCanciones.isEmpty()) {
                cancion.getSelectionModel().select(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLimpiarAction(ActionEvent event) {
        cancion.getSelectionModel().clearSelection();
        escuchar.getValueFactory().setValue(1);  // Restablecer el Spinner a 1
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        escuchar.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        escuchar.getValueFactory().setValue(1);
        cargarTituloCancionesCantadas();
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

    public ChoiceBox<String> getCancion() {
        return cancion;
    }

    public void setCancion(ChoiceBox<String> cancion) {
        this.cancion = cancion;
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

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }


}
