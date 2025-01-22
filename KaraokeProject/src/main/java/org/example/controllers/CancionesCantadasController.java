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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.Hibernate.Canciones;
import org.example.Hibernate.CancionesCantada;
import org.example.Hibernate.CancionesCantadaId;
import org.example.models.CancionesCantadas;
import org.example.models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

    private Usuario usuarioSeleccionado;

    private ObservableList<CancionesCantadas> cancionesList = FXCollections.observableArrayList();

    private Integer usuarioId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        fecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        vecesCantadas.setCellValueFactory(cellData -> cellData.getValue().vecesCantadaProperty().asObject());
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
            Stage stage = new Stage();
            stage.setTitle("Karaoke App");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista Canciones.fxml");
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
            System.err.println("Error al cargar la vista Canciones.fxml");
            e.printStackTrace();
        }
    }



    @FXML
    void onEliminarAction(ActionEvent event) {
        CancionesCantadas cancionesCantadasSeleccionado = tableCancionesCantadas.getSelectionModel().getSelectedItem();
        if (cancionesCantadasSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Cancion");
            alert.setHeaderText("Estas segurode elimnar esta cancion");
            ButtonType botonSi = new ButtonType("Si");
            ButtonType botonNo = new ButtonType("No");
            alert.getButtonTypes().setAll(botonSi, botonNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == botonSi) {
                eliminarCancionCantadas(cancionesCantadasSeleccionado);
                cargarTablaCancionesCantadas();
            }
        }

    }

    private void eliminarCancionCantadas(CancionesCantadas cancion) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String titulo = cancion.getTitulo();
            Integer cancionId = cancion.getId();
            Canciones cancionEntidad = em.find(Canciones.class, cancionId);
            CancionesCantadaId id = new CancionesCantadaId(cancionEntidad, titulo);
            CancionesCantada cancionCantadaHibernate = em.find(CancionesCantada.class, id);

            if (cancionCantadaHibernate != null) {
                em.remove(cancionCantadaHibernate);
                em.getTransaction().commit();
                System.out.println("Canción eliminada exitosamente.");
            } else {
                System.err.println("No se encontró la canción en la base de datos.");
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
    void onFiltrarAction(ActionEvent event) {
        if (datePicker.getValue() != null) {
            LocalDate fechaSeleccionada = datePicker.getValue();
            cargarTablaFecha(fechaSeleccionada);
        }
    }



    public void cargarTablaFecha(LocalDate fechaSeleccionada) {
        if (usuarioId == null) {
            System.err.println("El ID del usuario es null. No se puede cargar la tabla.");
            return;
        }

        cancionesList.clear();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM CancionesCantada c WHERE c.idUsuario = :usuarioId AND c.fecha = :fechaSeleccionada ORDER BY c.vecesCantada DESC";
            TypedQuery<CancionesCantada> query = em.createQuery(jpql, CancionesCantada.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("fechaSeleccionada", java.sql.Date.valueOf(fechaSeleccionada));
            List<CancionesCantada> result = query.getResultList();

            // Convertimos cada CancionesCantada a CancionesCantadas
            for (CancionesCantada cancion : result) {
                CancionesCantadas c = new CancionesCantadas(
                        cancion.getIdUsuario(),
                        cancion.getTitulo(),
                        cancion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        cancion.getVecesCantada()
                );
                cancionesList.add(c);
            }

            tableCancionesCantadas.setItems(cancionesList);
            tableCancionesCantadas.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos con Hibernate.");
        } finally {
            em.close();
            emf.close();
        }
    }

    public void cargarTablaCancionesCantadas() {
        if (usuarioId == null) {
            System.err.println("El ID del usuario es null. No se puede cargar la tabla.");
            return;
        }

        cancionesList.clear();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM CancionesCantada c WHERE c.idUsuario = :usuarioId ORDER BY c.vecesCantada DESC";
            TypedQuery<CancionesCantada> query = em.createQuery(jpql, CancionesCantada.class);
            query.setParameter("usuarioId", usuarioId);
            List<CancionesCantada> result = query.getResultList();

            // Convertimos cada CancionesCantada a CancionesCantadas
            for (CancionesCantada cancion : result) {
                CancionesCantadas c = new CancionesCantadas(
                        cancion.getIdUsuario(),
                        cancion.getTitulo(),
                        cancion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        cancion.getVecesCantada()
                );
                cancionesList.add(c);
            }

            tableCancionesCantadas.setItems(cancionesList);
            tableCancionesCantadas.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos con Hibernate.");
        } finally {
            em.close();
            emf.close();
        }
    }





    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
        cargarTablaCancionesCantadas();
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

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public ObservableList<CancionesCantadas> getCancionesList() {
        return cancionesList;
    }

    public void setCancionesList(ObservableList<CancionesCantadas> cancionesList) {
        this.cancionesList = cancionesList;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }
}
