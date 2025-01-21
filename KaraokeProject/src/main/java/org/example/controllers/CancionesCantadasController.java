package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.example.Hibernate.CancionesCantada;
import org.example.Hibernate.Usuarios;
import org.example.models.CancionesCantadas;
import org.example.models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CancionesCantadasController implements Initializable {


    @FXML
    private DatePicker datePicker;

    @FXML
    private Button filtrar;

    @FXML
    private TableColumn<CancionesCantadas, Date> fecha;

    @FXML
    private TableColumn<CancionesCantadas, Integer> id;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<CancionesCantadas> tableCancionesCantadas;

    @FXML
    private TableColumn<CancionesCantadas, String> titulo;

    @FXML
    private TableColumn<CancionesCantadas, Integer> vecesCantadas;

    private Usuario usuarioSeleccionado;

    private UsuariosController usuariosController;

    private ObservableList<CancionesCantadas> cancionesList = FXCollections.observableArrayList();

    private Integer usuarioId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        fecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        vecesCantadas.setCellValueFactory(cellData -> cellData.getValue().vecesCantadaProperty().asObject());
        cargarTablaCancionesCantadas();
    }


    @FXML
    void onFiltrarAction(ActionEvent event) {
        if (datePicker.getValue()!= null){
            Date fechaSeleccionada = java.sql.Date.valueOf(datePicker.getValue());
            cargarTablaFecha(fechaSeleccionada);
        }
    }


    public void cargarTablaFecha(Date fechaSeleccionada) {
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
            query.setParameter("fechaSeleccionada", fechaSeleccionada); // Usar fecha seleccionada en la consulta
            List<CancionesCantada> result = query.getResultList();

            for (CancionesCantada cancion : result) {
                CancionesCantadas c = new CancionesCantadas(
                        cancion.getIdUsuario(),
                        cancion.getTitulo(),
                        cancion.getFecha(),
                        cancion.getVecesCantada()
                );
                cancionesList.add(c);
            }

            // Actualizar la tabla
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
            query.setParameter("usuarioId", usuarioId); // Usar usuarioId en la consulta
            List<CancionesCantada> result = query.getResultList();

            for (CancionesCantada cancion : result) {
                CancionesCantadas c = new CancionesCantadas(
                        cancion.getIdUsuario(),
                        cancion.getTitulo(),
                        cancion.getFecha(),
                        cancion.getVecesCantada()
                );
                cancionesList.add(c);
            }

            // Configurar columnas de la tabla
            TableColumn<CancionesCantadas, Integer> id = new TableColumn<>("Id");
            id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

            TableColumn<CancionesCantadas, String> titulo = new TableColumn<>("Titulo");
            titulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());

            TableColumn<CancionesCantadas, Date> fecha = new TableColumn<>("Fecha");
            fecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());

            TableColumn<CancionesCantadas, Integer> veces = new TableColumn<>("Veces");
            veces.setCellValueFactory(cellData -> cellData.getValue().vecesCantadaProperty().asObject());

            tableCancionesCantadas.getColumns().setAll(id, titulo, fecha, veces);
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






    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;

    }


    public TableColumn<CancionesCantadas, Date> getFecha() {
        return fecha;
    }

    public void setFecha(TableColumn<CancionesCantadas, Date> fecha) {
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

    public ObservableList<CancionesCantadas> getCancionesList() {
        return cancionesList;
    }

    public void setCancionesList(ObservableList<CancionesCantadas> cancionesList) {
        this.cancionesList = cancionesList;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
        cargarTablaCancionesCantadas();
    }



}
