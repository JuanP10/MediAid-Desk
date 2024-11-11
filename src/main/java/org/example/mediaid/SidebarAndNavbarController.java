package org.example.mediaid;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SidebarAndNavbarController {

    @FXML
    private ImageView BtonBuzon;

    @FXML
    private ImageView BtonAgenda;

    @FXML
    private ImageView Btoncalendario;

    @FXML
    private TextField Busqueda;

    @FXML
    private Label NombreUsuario;

    @FXML
    private Label LogoHome;

    private String usuarioActual;

    // Mapa para almacenar las ventanas abiertas
    private final Map<String, Stage> ventanasAbiertas = new HashMap<>();

    // Método para inicializar el controlador y configurar los eventos de los botones
    @FXML
    public void initialize() {
        // Configurar acciones de los botones
        BtonBuzon.setOnMouseClicked(event -> abrirVentana("Buzon.fxml"));
        BtonAgenda.setOnMouseClicked(event -> abrirVentana("Agenda.fxml"));
        Btoncalendario.setOnMouseClicked(event -> abrirVentana("Calendario.fxml"));
        LogoHome.setOnMouseClicked(event -> abrirVentana("MainWindow.fxml"));

        // Configurar la acción del campo de búsqueda
        Busqueda.setOnAction(event -> realizarBusqueda(Busqueda.getText()));
    }

    // Método para abrir una nueva ventana
    private void abrirVentana(String archivoFXML) {
        // Comprobar si la ventana ya está abierta
        if (ventanasAbiertas.containsKey(archivoFXML)) {
            // Si ya está abierta, solo la traemos al frente
            ventanasAbiertas.get(archivoFXML).toFront();
        } else {
            try {
                // Si no está abierta, cerramos la ventana principal
                Stage stagePrincipal = (Stage) BtonBuzon.getScene().getWindow();
                stagePrincipal.close();

                // Cargar la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                // Almacenar la nueva ventana en el mapa
                ventanasAbiertas.put(archivoFXML, stage);

                // Configurar un evento para cerrar la ventana cuando se cierre
                stage.setOnCloseRequest(event -> ventanasAbiertas.remove(archivoFXML));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para realizar una búsqueda en el programa
    private void realizarBusqueda(String palabra) {
        // Aquí agregarías la lógica para buscar la palabra en el programa
        System.out.println("Buscando: " + palabra);
    }

    // Método para establecer el nombre del usuario que ingresó
    public void setNombreUsuario(String usuarioActual) {
        NombreUsuario.setText("Usuario: " + usuarioActual);
    }
}

