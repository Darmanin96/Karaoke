package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

    @FXML
    void onAceptarAction(ActionEvent event) {
        // Recuperamos los datos del formulario
        String tituloCancion = cancion.getValue();
        int vecesCantada = escuchar.getValue();

        // Validar los campos
        if (tituloCancion == null || vecesCantada <= 0) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error");
            alerta.setContentText("Por favor, selecciona una canción y establece un número de veces cantadas válido.");
            alerta.showAndWait();
            return;
        }

        // Buscar el id de la canción seleccionada en la base de datos
        int cancionId = -1;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id FROM canciones WHERE titulo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, tituloCancion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    cancionId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error");
            alerta.setContentText("Hubo un error al acceder a la base de datos.");
            alerta.showAndWait();
            return;
        }

        // Verificar si se encontró el id de la canción
        if (cancionId == -1) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error");
            alerta.setContentText("No se encontró la canción en la base de datos.");
            alerta.showAndWait();
            return;
        }

        // Obtener el id_usuario (esto depende de tu aplicación)
        int idUsuario = obtenerIdUsuarioActivo();

        // Insertar la nueva canción cantada
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO canciones_cantadas (cancion_id, titulo, fecha, veces_cantada, id_usuario) VALUES (?, ?, ?, ?, ?)";
            // Crear la fecha actual (usando la fecha SQL)
            Date fechaActual = Date.valueOf(LocalDate.now());

            // Preparar la consulta para la inserción
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, cancionId);  // Establece el id de la canción
                stmt.setString(2, tituloCancion);  // Establece el título de la canción
                stmt.setDate(3, fechaActual);  // Establece la fecha actual
                stmt.setInt(4, vecesCantada);  // Establece las veces cantadas
                stmt.setInt(5, idUsuario);  // Establece el id del usuario

                // Ejecutar la consulta
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Si la inserción fue exitosa
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setHeaderText("Éxito");
                    alerta.setContentText("La canción se ha añadido correctamente.");
                    alerta.showAndWait();

                    // Limpiar los campos después de guardar
                    cancion.getSelectionModel().clearSelection();
                    escuchar.getValueFactory().setValue(1);  // Resetea el Spinner a 1
                } else {
                    // Si no se pudo insertar
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setHeaderText("Error");
                    alerta.setContentText("Hubo un problema al añadir la canción.");
                    alerta.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error");
            alerta.setContentText("Hubo un error al acceder a la base de datos.");
            alerta.showAndWait();
        }
    }

    // Método ejemplo para obtener el id del usuario activo
    private int obtenerIdUsuarioActivo() {
        // Este método debería devolver el id del usuario que está actualmente autenticado
        // Esto dependerá de cómo gestionas la autenticación de usuarios en tu aplicación
        return 1; // Aquí estamos devolviendo un valor fijo como ejemplo
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


}
