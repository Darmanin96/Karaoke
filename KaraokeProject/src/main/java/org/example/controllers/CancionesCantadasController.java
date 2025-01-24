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
import javax.persistence.*;
import org.example.Hibernate.*;
import org.example.models.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CancionesCantadasController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button filtrar;

    @FXML
    private TableColumn<CancionesCantadas, LocalDate> fecha;

    @FXML
    private TableColumn<CancionesCantadas, Integer> id;

    @FXML
    private BorderPane root;

    @FXML
    private Button escuchar;

    @FXML
    private TableView<CancionesCantadas> tableCancionesCantadas;

    @FXML
    private TableColumn<CancionesCantadas, String> titulo;

    @FXML
    private TableColumn<CancionesCantadas, Integer> vecesCantadas;

    private Integer usuarioId;

    private ObservableList<CancionesCantadas> cancionesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        fecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        vecesCantadas.setCellValueFactory(cellData -> cellData.getValue().vecesCantadaProperty().asObject());

        // Formatear la fecha
        fecha.setCellFactory(column -> new TableCell<CancionesCantadas, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        cargarTablaCancionesCantadas();
    }

    @FXML
    void onEscucharAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AñadirCancionesCantadas.fxml"));
            Parent root = loader.load();
            AñadirCancionesCantadasController controller = loader.getController();
            controller.setUsuarioId(usuarioId); // Asignar el usuarioId
            controller.setCancionesCantadasController(this); // Inyectar el controlador CancionesCantadasController
            Stage stage = new Stage();
            stage.setTitle("Karaoke App");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista AñadirCancionesCantadas.fxml");
            e.printStackTrace();
        }
    }


    @FXML
    void onAñadirAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateCancion.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Karaoke App");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista Crear Cancion.");
            e.printStackTrace();
        }
    }

    @FXML
    void onEliminarAction(ActionEvent event) {
        // Obtener la canción seleccionada
        CancionesCantadas cancionSeleccionada = tableCancionesCantadas.getSelectionModel().getSelectedItem();

        if (cancionSeleccionada == null) {
            // Si no se seleccionó ninguna canción
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selección inválida");
            alert.setHeaderText("No se ha seleccionado ninguna canción");
            alert.setContentText("Por favor, selecciona una canción para eliminar.");
            alert.showAndWait();
            return;
        }

        String tituloCancion = cancionSeleccionada.getTitulo();

        // Confirmación antes de eliminar
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar esta canción?");
        alert.setContentText("Se eliminará la canción con el título: " + tituloCancion);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eliminarCancion(tituloCancion);
        }
    }

    private void eliminarCancion(String tituloCancion) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String jpql = "SELECT c FROM CancionesCantada c WHERE c.titulo = :tituloCancion";
            TypedQuery<CancionesCantada> query = em.createQuery(jpql, CancionesCantada.class);
            query.setParameter("tituloCancion", tituloCancion);
            CancionesCantada cancion = query.getSingleResult();
            em.remove(cancion);
            em.getTransaction().commit();
            cargarTablaCancionesCantadas();
        } catch (NoResultException e) {
            System.out.println("Canción no encontrada: " + tituloCancion);
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    @FXML
    void onFiltrarAction(ActionEvent event) {
        if (datePicker.getValue() != null) {
            LocalDate fechaSeleccionada = datePicker.getValue();
            cargarTablaFecha(fechaSeleccionada);
        }
    }

    public void cargarTablaFecha(LocalDate fechaSeleccionada) {
        if (usuarioId == null) return;
        cancionesList.clear();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM CancionesCantada c WHERE c.idUsuario = :usuarioId AND c.fecha = :fechaSeleccionada ORDER BY c.vecesCantada DESC";
            TypedQuery<CancionesCantada> query = em.createQuery(jpql, CancionesCantada.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("fechaSeleccionada", java.sql.Date.valueOf(fechaSeleccionada));
            List<CancionesCantada> result = query.getResultList();
            for (CancionesCantada cancion : result) {
                CancionesCantadas c = new CancionesCantadas(cancion.getIdUsuario(), cancion.getTitulo(),
                        cancion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        cancion.getVecesCantada());
                cancionesList.add(c);
            }
            tableCancionesCantadas.setItems(cancionesList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    public void cargarTablaCancionesCantadas() {
        cancionesList.clear();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM CancionesCantada c WHERE c.idUsuario = :usuarioId ORDER BY c.vecesCantada DESC";
            TypedQuery<CancionesCantada> query = em.createQuery(jpql, CancionesCantada.class);
            query.setParameter("usuarioId", usuarioId);
            List<CancionesCantada> result = query.getResultList();
            for (CancionesCantada cancion : result) {
                CancionesCantadas c = new CancionesCantadas(cancion.getIdUsuario(), cancion.getTitulo(),
                        cancion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        cancion.getVecesCantada());
                cancionesList.add(c);
            }
            tableCancionesCantadas.setItems(cancionesList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
        cargarTablaCancionesCantadas();
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public Button getFiltrar() {
        return filtrar;
    }

    public void setFiltrar(Button filtrar) {
        this.filtrar = filtrar;
    }

    public TableColumn<CancionesCantadas, LocalDate> getFecha() {
        return fecha;
    }

    public void setFecha(TableColumn<CancionesCantadas, LocalDate> fecha) {
        this.fecha = fecha;
    }

    public TableColumn<CancionesCantadas, Integer> getId() {
        return id;
    }

    public void setId(TableColumn<CancionesCantadas, Integer> id) {
        this.id = id;
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public Button getEscuchar() {
        return escuchar;
    }

    public void setEscuchar(Button escuchar) {
        this.escuchar = escuchar;
    }

    public TableView<CancionesCantadas> getTableCancionesCantadas() {
        return tableCancionesCantadas;
    }

    public void setTableCancionesCantadas(TableView<CancionesCantadas> tableCancionesCantadas) {
        this.tableCancionesCantadas = tableCancionesCantadas;
    }

    public TableColumn<CancionesCantadas, String> getTitulo() {
        return titulo;
    }

    public void setTitulo(TableColumn<CancionesCantadas, String> titulo) {
        this.titulo = titulo;
    }

    public TableColumn<CancionesCantadas, Integer> getVecesCantadas() {
        return vecesCantadas;
    }

    public void setVecesCantadas(TableColumn<CancionesCantadas, Integer> vecesCantadas) {
        this.vecesCantadas = vecesCantadas;
    }

    public ObservableList<CancionesCantadas> getCancionesList() {
        return cancionesList;
    }

    public void setCancionesList(ObservableList<CancionesCantadas> cancionesList) {
        this.cancionesList = cancionesList;
    }
}
