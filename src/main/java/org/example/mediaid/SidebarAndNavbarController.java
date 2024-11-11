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

    private String usuarioActual ;

    // Método para inicializar el controlador y configurar los eventos de los botones
    @FXML
    public void initialize() {
        // Configurar acciones de los botones
        BtonBuzon.setOnMouseClicked(event -> abrirVentana("Buzon.fxml"));
        BtonAgenda.setOnMouseClicked(event -> abrirVentana("Agenda.fxml"));
        Btoncalendario.setOnMouseClicked(event -> abrirVentana("Calendario.fxml"));

        // Configurar la acción del campo de búsqueda
        Busqueda.setOnAction(event -> realizarBusqueda(Busqueda.getText()));
    }

    // Método para abrir una nueva ventana
    private void abrirVentana(String archivoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para realizar una búsqueda en el programa
    private void realizarBusqueda(String palabra) {
        // Aquí agregarías la lógica para buscar la palabra en el programa
        System.out.println("Buscando: " + palabra);
    }

    // Método para establecer el nombre del usuario que ingresó
    public void setNombreUsuario (String usuarioActual) {
        NombreUsuario.setText("Usuario: " + usuarioActual);
    }
}
