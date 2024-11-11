package org.example.mediaid;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private RadioButton BtJefe; // El radioButton de "Jefe"
    @FXML
    private RadioButton BtAuxiliar; // El radioButton de "Auxiliar"
    @FXML
    private TextField usuarioField; // Campo para el nombre de usuario
    @FXML
    private PasswordField passwordField; // Campo para la contraseña
    @FXML
    private Button accederBtn; // El botón de acceso

    private static final String USUARIO_JEFE = "jefe";
    private static final String CONTRASENA_JEFE = "jefe123";

    private static final String USUARIO_AUXILIAR = "auxiliar";
    private static final String CONTRASENA_AUXILIAR = "auxiliar123";

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText();
        String contrasena = passwordField.getText();

        System.out.println("Usuario ingresado: " + usuario); // Depuración
        System.out.println("Contraseña ingresada: " + contrasena); // Depuración

        if (!BtJefe.isSelected() && !BtAuxiliar.isSelected()) {
            showAlert("Error", "Debe seleccionar al menos un rol (Jefe o Auxiliar).", AlertType.ERROR);
            return;
        }

        if (BtJefe.isSelected() && usuario.equals(USUARIO_JEFE) && contrasena.equals(CONTRASENA_JEFE)  && !BtAuxiliar.isSelected()) {
            openMainWindow();
        } else if (BtAuxiliar.isSelected() && usuario.equals(USUARIO_AUXILIAR) && contrasena.equals(CONTRASENA_AUXILIAR) && !BtJefe.isSelected()) {
            openMainWindow();
        } else {
            showAlert("Acceso Incorrecto", "Datos incorrectos, intente nuevamente.", AlertType.ERROR);
        }

    }

    private void openMainWindow() {
        try {
            // Cargar MainWindow.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Obtener el controlador de MainWindow.fxml
            MainWindowController mainController = loader.getController();

            // Ahora acceder al controlador del Sidebar (que está dentro de MainWindow)
            SidebarAndNavbarController sidebarController = mainController.getSidebarController();

            // Establecer el nombre de usuario en SidebarAndNavbarController
            sidebarController.setNombreUsuario(usuarioField.getText());

            // Mostrar la ventana principal
            stage.show();

            // Cerrar la ventana de login
            Stage currentStage = (Stage) accederBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
