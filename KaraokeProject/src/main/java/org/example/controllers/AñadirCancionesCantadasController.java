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
        String tituloCancion = cancion.getValue();  // Obtienes el título de la canción seleccionada en el ChoiceBox
        int vecesCantada = escuchar.getValue();
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        System.out.println("Título de la canción: " + tituloCancion);
        System.out.println("Veces cantada: " + vecesCantada);
        System.out.println("Fecha actual: " + localDate);

        // Crear la consulta JPQL para obtener la entidad Canciones por su título
        String jpql = "SELECT c FROM Canciones c WHERE c.titulo = :titulo";
        try {
            TypedQuery<Canciones> query = em.createQuery(jpql, Canciones.class);
            query.setParameter("titulo", tituloCancion);

            // Ejecutar la consulta y obtener la entidad Canciones
            Canciones cancionEntidad = query.getSingleResult();
            System.out.println("Canción encontrada: " + cancionEntidad.getTitulo());
            CancionesCantada nuevaCancionCantada = new CancionesCantada();
            nuevaCancionCantada.setCancion(cancionEntidad);  // Asignamos la entidad Canciones
            nuevaCancionCantada.setTitulo(tituloCancion);    // Asignamos el título
            nuevaCancionCantada.setFecha(sqlDate);           // Asignamos la fecha
            nuevaCancionCantada.setVecesCantada(vecesCantada);
            nuevaCancionCantada.setIdUsuario(usuarioId);    // Asigna el id del usuario si lo tienes

            // Persistir la entidad CancionesCantada
            em.getTransaction().begin();
            em.persist(nuevaCancionCantada);
            em.getTransaction().commit();

            System.out.println("Canción cantada insertada correctamente.");
            cerrar();
            cancionesCantadasController.cargarTablaCancionesCantadas();
        } catch (NoResultException e) {
            System.out.println("No se encontró ninguna canción con el título: " + tituloCancion);
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
        // Inicializar spinner
        escuchar.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        escuchar.getValueFactory().setValue(1);

        // Inicializar EntityManager
        emf = Persistence.createEntityManagerFactory("KaraokePU");  // Asegúrate de que el nombre de la unidad de persistencia sea correcto
        em = emf.createEntityManager();

        // Cargar canciones en el ChoiceBox
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
