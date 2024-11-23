package org.example.mediaid.ventanaControladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.mediaid.modelos.Medicamento;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindowController {

    @FXML
    private GridPane GridPaneMedicamentos;  // El GridPane donde se mostrarán los medicamentos

    @FXML
    private Pane PaneEditarMedicamento;  // El Pane donde se editarán los medicamentos

    @FXML
    private TextField CantidadEdit;  // Campo para editar cantidad

    @FXML
    private DatePicker FechaEdit;  // Campo para editar la fecha de vencimiento

    @FXML
    private Text NombreEdit;  // Campo solo de lectura para mostrar el nombre del medicamento

    @FXML
    private Text CategoriaEdit;  // Campo solo de lectura para mostrar la categoría

    @FXML
    private Button BtnEditar;  // Botón para guardar los cambios

    @FXML private MenuButton BusqTipo;

    @FXML
    private DatePicker BusqFecha;

    @FXML private Button BtonAgregar;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox vboxMedicamentos;
    @FXML private Text fechaEdit;
    @FXML private DatePicker BusFecha;
    @FXML private MenuButton BusqDonante;
    @FXML private MenuButton BusqCategoria;
    @FXML
    private SidebarAndNavbarController sidebarController;
    private Medicamento medicamentoSeleccionado;  // Medicamento que está siendo editado

    @FXML private GridPane gridPaneMedicamentos;

    @FXML private Text fechaActualVencimiento;
    @FXML private DatePicker modificarFechaVencimiento;


    public SidebarAndNavbarController getSidebarController() {
        return sidebarController;
    }
    private ObservableList<Medicamento> listaMedicamentos;

    private void cargarMedicamentos() {
        try {
            // Deserializar JSON a lista de Medicamentos
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Medicamento> medicamentos = mapper.readValue(
                    new File("src/main/java/org/example/mediaid/json/medicamentos.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Medicamento.class)
            );

            listaMedicamentos = javafx.collections.FXCollections.observableArrayList(medicamentos);

            // Limpiar el GridPane antes de agregar nuevos elementos
            GridPaneMedicamentos.getChildren().clear();

            // Iterar sobre la lista de medicamentos y agregar una fila para cada uno
            for (int i = 0; i < medicamentos.size(); i++) {
                Medicamento medicamento = medicamentos.get(i);

                // Crear los textos y botones para cada medicamento
                Text nombreText = new Text(medicamento.getNombre());
                Text tipoText = new Text(medicamento.getTipo().toString());
                Text fechaText = new Text(medicamento.getFechaVencimiento().toString());

                Button editarButton = new Button("Editar");
                editarButton.setOnAction(event -> mostrarFormularioEditar(medicamento));

                // Agregar los elementos al GridPane en la fila correspondiente
                GridPaneMedicamentos.add(nombreText, 0, i);
                GridPaneMedicamentos.add(tipoText, 1, i);
                GridPaneMedicamentos.add(fechaText, 2, i);
                GridPaneMedicamentos.add(editarButton, 3, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        cargarMedicamentos();
        inicializarMenuButton();
        inicializarBuscadorFecha();
    }

    private void inicializarMenuButton() {
        if (BusqTipo != null) {
            // Obtener los valores del enum TipoMedicamento
            for (Medicamento.TipoMedicamento tipo : Medicamento.TipoMedicamento.values()) {
                // Crear un nuevo MenuItem para cada tipo
                MenuItem menuItem = new MenuItem(tipo.name());

                // Añadir una acción al seleccionar el tipo
                menuItem.setOnAction(event -> {
                    BusqTipo.setText(tipo.name()); // Mostrar el tipo seleccionado en el botón
                    filtrarPorTipo(tipo.name()); // Filtrar medicamentos por el tipo seleccionado
                });

                // Agregar el MenuItem al MenuButton
                BusqTipo.getItems().add(menuItem);
            }
        } else {
            System.out.println("BusqTipo no está inicializado.");
        }
    }


    private void inicializarBuscadorFecha() {
        BusqFecha.setOnAction(event -> {
            LocalDate fechaSeleccionada = BusqFecha.getValue();
            if (fechaSeleccionada != null) {
                filtrarPorFecha(fechaSeleccionada);
            }
        });
    }
    private void filtrarPorTipo(String tipo) {
        if (listaMedicamentos == null) {
            System.out.println("La lista de medicamentos no está inicializada.");
            return;
        }

        // Filtrar medicamentos por tipo
        List<Medicamento> filtrados = listaMedicamentos.stream()
                .filter(medicamento -> medicamento.getTipo().name().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());

        // Mostrar medicamentos filtrados en el GridPane
        mostrarMedicamentos(filtrados);
    }


    private void filtrarPorFecha(LocalDate fecha) {
        List<Medicamento> filtrados = listaMedicamentos.stream()
                .filter(m -> !m.getFechaVencimiento().isBefore(fecha))
                .collect(Collectors.toList());
        mostrarMedicamentos(filtrados);
    }
    private void mostrarMedicamentos(List<Medicamento> medicamentos) {
        GridPaneMedicamentos.getChildren().clear(); // Limpiar el GridPane antes de mostrar medicamentos

        for (int i = 0; i < medicamentos.size(); i++) {
            Medicamento medicamento = medicamentos.get(i);

            // Crear nodos para mostrar la información del medicamento
            Text nombreText = new Text(medicamento.getNombre());
            Text tipoText = new Text(medicamento.getTipo().toString());
            Text fechaText = new Text(medicamento.getFechaVencimiento().toString());

            Button editarButton = new Button("Editar");
            editarButton.setOnAction(event -> mostrarFormularioEditar(medicamento));

            // Agregar al GridPane
            GridPaneMedicamentos.add(nombreText, 0, i);
            GridPaneMedicamentos.add(tipoText, 1, i);
            GridPaneMedicamentos.add(fechaText, 2, i);
            GridPaneMedicamentos.add(editarButton, 3, i);
        }
    }

    @FXML
    public void editarMedicamento() {
        // Validar si se ha seleccionado una nueva fecha
        if (modificarFechaVencimiento.getValue() != null) {
            // Actualizar la fecha de vencimiento en el medicamento seleccionado
            medicamentoSeleccionado.setFechaVencimiento(modificarFechaVencimiento.getValue());
        }

        // Validar y actualizar la cantidad
        try {
            int nuevaCantidad = Integer.parseInt(CantidadEdit.getText());
            medicamentoSeleccionado.setCantidad(nuevaCantidad);
        } catch (NumberFormatException e) {
            // Si la cantidad no es un número válido, se puede mostrar un mensaje de error
            System.out.println("Cantidad no válida.");
            return;
        }

        // Ahora que hemos actualizado el medicamento, necesitamos guardarlo en el archivo JSON
        guardarMedicamentosEnJson();

        mostrarAlertaMedicamentoActualizado();

    }
    private void mostrarAlertaMedicamentoActualizado() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "El medicamento ha sido actualizado con éxito.", ButtonType.OK);
        alerta.setTitle("Medicamento Actualizado");
        alerta.setHeaderText(null);  // Puedes poner un mensaje adicional si lo deseas
        alerta.showAndWait();
    }

    ///Método para mostrar los medicamentos en el Pane Editarrr
    private void mostrarFormularioEditar(Medicamento medicamento) {
        // Mostrar el Pane de edición
        PaneEditarMedicamento.setVisible(true);

        // Guardar el medicamento seleccionado para editarlo
        medicamentoSeleccionado = medicamento;

        // Llenar los campos con los datos actuales del medicamento
        NombreEdit.setText(medicamento.getNombre());
        CategoriaEdit.setText(medicamento.getTipo().toString());
        CantidadEdit.setText(String.valueOf(medicamento.getCantidad()));
        fechaActualVencimiento.setText(medicamento.getFechaVencimiento().toString());
        modificarFechaVencimiento.setValue(medicamento.getFechaVencimiento());  // Establecer la fecha actual
    }

    private void guardarMedicamentosEnJson() {
        try {
            // Crear el objeto ObjectMapper para convertir los medicamentos a JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            // Leer los medicamentos existentes
            List<Medicamento> medicamentos = mapper.readValue(new File("src/main/java/org/example/mediaid/json/medicamentos.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Medicamento.class));

            // Actualizar el medicamento modificado en la lista
            for (int i = 0; i < medicamentos.size(); i++) {
                if (medicamentos.get(i).getNombre().equals(medicamentoSeleccionado.getNombre())) {
                    medicamentos.set(i, medicamentoSeleccionado);  // Reemplazar con el medicamento actualizado
                    break;
                }
            }

            // Guardar la lista actualizada en el archivo JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/java/org/example/mediaid/json/medicamentos.json"), medicamentos);

            // Informar que la actualización fue exitosa
            System.out.println("Medicamento actualizado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar los medicamentos.");
        }
    }


    // Método auxiliar para cargar los medicamentos desde el archivo JSON
    private List<Medicamento> cargarMedicamentosDesdeArchivo() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(new File("src/main/java/org/example/mediaid/json/medicamentos.json"), mapper.getTypeFactory().constructCollectionType(List.class, Medicamento.class));
    }

    @FXML
    private void resetearListaMedicamentos() {
        cargarMedicamentos(); // Reutilizamos la función que ya carga todos los medicamentos
    }

}

