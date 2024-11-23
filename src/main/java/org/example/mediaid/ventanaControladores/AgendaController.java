package org.example.mediaid.ventanaControladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Separator;
import org.controlsfx.control.CheckListView;
import org.example.mediaid.modelos.Paciente;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgendaController {

    @FXML private TextField NombrePaciente;
    @FXML private TextField CedulaPaciente;
    @FXML private DatePicker NacimientoPaciente;
    @FXML private CheckListView<Paciente.Condicion> CondicionesPaciente;
    @FXML private CheckBox EsPecionadoPaciente;
    @FXML private Button BtnEditarPaciente;
    @FXML private TextField Busqueda;
    @FXML private ScrollPane ScrollMostrarPacientes;

    // Lista de pacientes
    private ObservableList<Paciente> listaPacientes;
    private static final String FILE_PATH = "src/main/java/org/example/mediaid/json/pacientes.json";

    private final ObjectMapper objectMapper;

    @FXML private Label mensajePacienteGuardado;  // Label para mostrar el mensaje

    public AgendaController() {
        // Inicializa el ObjectMapper con el módulo para manejar LocalDate
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void initialize() {
        // Inicializar la lista de condiciones con los valores del enum
        CondicionesPaciente.setItems(FXCollections.observableArrayList(Paciente.Condicion.values()));

        // Cargar los pacientes desde el archivo JSON
        listaPacientes = FXCollections.observableArrayList(cargarPacientes());
        if (listaPacientes == null) {
            listaPacientes = FXCollections.observableArrayList();
        }

        // Mostrar los pacientes
        mostrarPacientes(listaPacientes);

        // Configurar el campo de búsqueda
        Busqueda.textProperty().addListener((observable, oldValue, newValue) -> buscarPacientes(newValue));
    }

    @FXML
    private void agregarPaciente() {
        // Obtener los datos del paciente del formulario
        String nombre = NombrePaciente.getText();
        String cedula = CedulaPaciente.getText();
        LocalDate fechaNacimiento = NacimientoPaciente.getValue();

        // Si hay varias condiciones seleccionadas, se obtienen todas como una lista
        List<Paciente.Condicion> condicionesSeleccionadas = CondicionesPaciente.getItems().stream()
                .filter(condicion -> CondicionesPaciente.getSelectionModel().getSelectedItems().contains(condicion))
                .collect(Collectors.toList());

        boolean esPensionado = EsPecionadoPaciente.isSelected();

        // Crear un nuevo paciente
        Paciente nuevoPaciente = new Paciente(nombre, cedula, fechaNacimiento, condicionesSeleccionadas, esPensionado);

        // Agregarlo a la lista
        listaPacientes.add(nuevoPaciente);

        // Guardar la lista actualizada en el archivo JSON
        guardarPacientes(listaPacientes);

        // Limpiar los campos
        NombrePaciente.clear();
        CedulaPaciente.clear();
        NacimientoPaciente.setValue(null);
        CondicionesPaciente.getSelectionModel().clearSelection();
        EsPecionadoPaciente.setSelected(false);

        // Mostrar los pacientes actualizados
        mostrarPacientes(listaPacientes);

        // Mostrar el mensaje de paciente guardado
        mostrarMensajePacienteGuardado();
    }

    // Mostrar los pacientes en el UI
    private void mostrarPacientes(List<Paciente> pacientes) {
        // Limpiar el contenido actual
        VBox vbox = new VBox(10);  // Contenedor VBox con un espaciado de 10px entre filas
        ScrollMostrarPacientes.setContent(vbox);

        // Agregar los pacientes al VBox (cada paciente en una fila)
        for (Paciente paciente : pacientes) {
            // Formatear la información del paciente de forma limpia
            String pacienteInfo = "Nombre: " + paciente.getNombre() + "\n" +
                    "Cédula: " + paciente.getCedula() + "\n" +
                    "Fecha Nacimiento: " + paciente.getFechaNacimiento().toString() + "\n" +
                    "Condiciones: " + paciente.getCondicion().stream()
                    .map(Paciente.Condicion::toString)
                    .collect(Collectors.joining(", ")) + "\n" +
                    "Pensionado: " + (paciente.isEsPensionado() ? "Sí" : "No");

            Text pacienteText = new Text(pacienteInfo);
            vbox.getChildren().add(pacienteText);  // Añadir cada paciente como un nuevo Text

            // Separador entre pacientes
            Separator separator = new Separator();
            vbox.getChildren().add(separator);
        }
    }

    // Mostrar mensaje de "Paciente guardado"
    private void mostrarMensajePacienteGuardado() {
        mensajePacienteGuardado.setText("Paciente guardado correctamente!");
        // Puedes agregar un temporizador para que el mensaje desaparezca después de unos segundos:
        new Thread(() -> {
            try {
                Thread.sleep(3000);  // Espera 3 segundos
                mensajePacienteGuardado.setText("");  // Limpia el mensaje
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Buscar pacientes
    private void buscarPacientes(String criterio) {
        List<Paciente> pacientesFiltrados = listaPacientes.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                        p.getCondicion().stream().anyMatch(c -> c.toString().toLowerCase().contains(criterio.toLowerCase())) ||
                        p.getCedula().contains(criterio))
                .collect(Collectors.toList());

        // Mostrar los pacientes filtrados
        mostrarPacientes(pacientesFiltrados);
    }

    // Guardar pacientes en el archivo JSON
    private void guardarPacientes(List<Paciente> pacientes) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), pacientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar pacientes desde el archivo JSON
    private List<Paciente> cargarPacientes() {
        try {
            File archivo = new File(FILE_PATH);
            if (archivo.exists()) {
                List<Paciente> pacientes = objectMapper.readValue(archivo,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Paciente.class));
                return pacientes != null ? pacientes : new ArrayList<>();  // Evitar null
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }
}


