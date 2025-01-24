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

    private EntityManagerFactory emf;
    private EntityManager em;

    private CancionesCantadasController cancionesCantadasController;

    private void cerrar() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAceptarAction(ActionEvent event) {
        String tituloCancion = cancion.getValue();
        int vecesCantada = escuchar.getValue();
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        // Consultar si la canción existe
        String jpql = "SELECT c FROM Canciones c WHERE c.titulo = :titulo";
        try {
            TypedQuery<Canciones> query = em.createQuery(jpql, Canciones.class);
            query.setParameter("titulo", tituloCancion);
            Canciones cancionEntidad = query.getSingleResult();

            // Crear la entrada de CancionesCantadas
            CancionesCantada nuevaCancionCantada = new CancionesCantada();
            nuevaCancionCantada.setCancion(cancionEntidad);
            nuevaCancionCantada.setTitulo(tituloCancion);
            nuevaCancionCantada.setFecha(sqlDate);
            nuevaCancionCantada.setVecesCantada(vecesCantada);
            nuevaCancionCantada.setIdUsuario(usuarioId);

            // Persistir la nueva canción cantada
            em.getTransaction().begin();
            em.persist(nuevaCancionCantada);
            em.getTransaction().commit();

            // Cerrar y actualizar la tabla
            cerrar();
            cancionesCantadasController.cargarTablaCancionesCantadas();

        } catch (NoResultException e) {
            System.out.println("Canción no encontrada con el título: " + tituloCancion);
        } catch (PersistenceException e) {
            e.printStackTrace();
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
        escuchar.getValueFactory().setValue(1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el Spinner
        escuchar.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        // Inicializar el EntityManager
        emf = Persistence.createEntityManagerFactory("KaraokePU");
        em = emf.createEntityManager();

        // Cargar canciones en el ChoiceBox
        cargarTituloCancionesCantadas();
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
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

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public CancionesCantadasController getCancionesCantadasController() {
        return cancionesCantadasController;
    }

    public void setCancionesCantadasController(CancionesCantadasController cancionesCantadasController) {
        this.cancionesCantadasController = cancionesCantadasController;
    }
}
