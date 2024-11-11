package org.example.mediaid;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class MainWindowController {

    @FXML private Button BtnEditar;
    @FXML private Button BtonAgregar;
    @FXML private TextField CantidadEdit;
    @FXML private Text NombreEdit;
    @FXML private Text CategoriaEdit;
    @FXML private Text fechaEdit;
    @FXML private ImageView ImageEditMedicamento;
    @FXML private DatePicker BusFecha;
    @FXML private MenuButton BusqDonante;
    @FXML private MenuButton BusqCategoria;
    @FXML
    private SidebarAndNavbarController sidebarController;

    public SidebarAndNavbarController getSidebarController() {
        return sidebarController;
    }

    // Método que se ejecuta cuando el botón "Editar" es presionado
    @FXML
    private void handleEditar() {
        String cantidad = CantidadEdit.getText();
        String fecha = BusFecha.getValue() != null ? BusFecha.getValue().toString() : "No seleccionada";

        // Mostrar la información editada
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Fecha de vencimiento: " + fecha);

    }

    // Método que se ejecuta cuando el botón "Agregar" es presionado
    @FXML
    private void handleAgregar() {
        // Obtiene la fecha seleccionada
        String fecha = BusFecha.getValue() != null ? BusFecha.getValue().toString() : "No seleccionada";

        // Aquí puedes agregar la lógica para agregar un nuevo medicamento
        System.out.println("Fecha de vencimiento: " + fecha);

        // Agregar más lógica según la necesidad
    }

    // Método que inicializa los valores y configura los elementos al iniciar la interfaz
    @FXML
    public void initialize() {
        // Ejemplo de cómo puedes configurar algunos de los valores de la interfaz
        NombreEdit.setText("Medicamento Ejemplo");
        CategoriaEdit.setText("Categoría Ejemplo");
        fechaEdit.setText("2024-11-10");

        // Aquí puedes configurar otros elementos de la interfaz según sea necesario
    }

    // Método para cambiar la selección del menú de Donantes
    @FXML
    private void handleDonanteAction(MenuItem item) {
        // Acción para el ítem seleccionado en el menú Donante
        System.out.println("Donante seleccionado: " + item.getText());
    }

    // Método para cambiar la selección del menú de Categoría
    @FXML
    private void handleCategoriaAction(MenuItem item) {
        // Acción para el ítem seleccionado en el menú Categoría
        System.out.println("Categoría seleccionada: " + item.getText());
    }
}

