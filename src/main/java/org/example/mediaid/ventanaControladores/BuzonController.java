package org.example.mediaid.ventanaControladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.mediaid.modelos.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BuzonController {

    @FXML private MenuButton MenuPaciente;
    @FXML private MenuButton MenuEnfermeroquerealizo;
    @FXML private MenuButton MenuMedicamento;
    @FXML private TextField TextCantidadSuministrada;
    @FXML private DatePicker BtonFechaSuministro;
    @FXML private Button BtonAgregarMedicamento;
    @FXML private Button DescargarSuministros;

    private ObservableList<Paciente> listaPacientes;
    private ObservableList<Enfermero> listaEnfermeros;
    private ObservableList<Medicamento> listaMedicamentos;

    private static final String FILE_PACIENTES_PATH = "src/main/java/org/example/mediaid/json/pacientes.json";
    private static final String FILE_ENFERMEROS_PATH = "src/main/java/org/example/mediaid/json/enfermeros.json";
    private static final String FILE_MEDICAMENTOS_PATH = "src/main/java/org/example/mediaid/json/medicamentos.json";
    private static final String FILE_SUMINISTROS_PATH = "src/main/java/org/example/mediaid/json/suministros.json";

    private ObjectMapper objectMapper;

    public BuzonController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void initialize() {
        DescargarSuministros.setOnAction(event -> descargarSuministrosComoPDF());

        assert MenuPaciente != null : "MenuPaciente no está correctamente inyectado en el FXML";

        // Cargar los datos desde los archivos JSON
        listaPacientes = FXCollections.observableArrayList(cargarPacientes());
        listaEnfermeros = FXCollections.observableArrayList(cargarEnfermeros());
        listaMedicamentos = FXCollections.observableArrayList(cargarMedicamentos());

        // Agregar los pacientes al MenuButton
        listaPacientes.forEach(paciente -> {
            MenuItem menuItem = new MenuItem(paciente.getNombre());
            menuItem.setOnAction(event -> MenuPaciente.setText(paciente.getNombre()));
            MenuPaciente.getItems().add(menuItem);
        });

        // Agregar los enfermeros al MenuButton
        listaEnfermeros.forEach(enfermero -> {
            MenuItem menuItem = new MenuItem(enfermero.getNombre());
            menuItem.setOnAction(event -> MenuEnfermeroquerealizo.setText(enfermero.getNombre()));
            MenuEnfermeroquerealizo.getItems().add(menuItem);
        });

        // Agregar los medicamentos al MenuButton
        listaMedicamentos.forEach(medicamento -> {
            MenuItem menuItem = new MenuItem(medicamento.getNombre());
            menuItem.setOnAction(event -> MenuMedicamento.setText(medicamento.getNombre()));
            MenuMedicamento.getItems().add(menuItem);
        });

        // Configurar el botón de agregar suministro
        BtonAgregarMedicamento.setOnAction(event -> agregarSuministro());
    }

    private void descargarSuministrosComoPDF() {
        try {
            // Usa el ObjectMapper configurado previamente en el constructor
            List<Suministro> suministros = objectMapper.readValue(
                    new File("src/main/java/org/example/mediaid/json/suministros.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Suministro.class)
            );

            // Crear el archivo PDF
            File pdfFile = new File("Suministros.pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            PdfDocument pdfDocument = new PdfDocument();
            Document document = new Document(pdfDocument.getPageSize());

            // Crear una tabla con los datos de los suministros
            Table table = new Table(3); // 3 columnas para nombre, cantidad y fecha
            table.addHeaderCell("Nombre");
            table.addHeaderCell("Cantidad");
            table.addHeaderCell("Fecha");

            // Llenar la tabla con los datos de los suministros
            for (Suministro suministro : suministros) {
                table.addCell(suministro.getPaciente().getNombre());
                table.addCell(suministro.getMedicamento().getNombre());
                table.addCell(String.valueOf(suministro.getCantidad()));
                table.addCell(suministro.getFechaSuministro().toString());
                table.addCell(suministro.getEnfermero().getNombre());
            }

            // Agregar la tabla al documento
            document.add((Element) table);

            // Cerrar el documento y la conexión con el archivo
            document.close();

            // Mostrar una alerta informando que el PDF se ha generado correctamente
            mostrarAlerta("PDF generado", "El archivo PDF con los suministros ha sido generado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un error al generar el PDF.");
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);  // Se puede personalizar si se desea
        alert.showAndWait();
    }

    private List<Paciente> cargarPacientes() {
        try {
            return objectMapper.readValue(new File(FILE_PACIENTES_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Paciente.class));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private List<Enfermero> cargarEnfermeros() {
        try {
            return objectMapper.readValue(new File(FILE_ENFERMEROS_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Enfermero.class));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private List<Medicamento> cargarMedicamentos() {
        try {
            return objectMapper.readValue(new File(FILE_MEDICAMENTOS_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Medicamento.class));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @FXML
    private void agregarSuministro() {
        // Obtener los datos del formulario
        Paciente pacienteSeleccionado = listaPacientes.stream()
                .filter(p -> p.getNombre().equals(MenuPaciente.getText()))
                .findFirst()
                .orElse(null);
        Enfermero enfermeroSeleccionado = listaEnfermeros.stream()
                .filter(e -> e.getNombre().equals(MenuEnfermeroquerealizo.getText()))
                .findFirst()
                .orElse(null);
        Medicamento medicamentoSeleccionado = listaMedicamentos.stream()
                .filter(m -> m.getNombre().equals(MenuMedicamento.getText()))
                .findFirst()
                .orElse(null);

        int cantidad;
        try {
            cantidad = Integer.parseInt(TextCantidadSuministrada.getText());
        } catch (NumberFormatException e) {
            mostrarMensajeError("Error", "Cantidad no válida.");
            return;  // Salir del método si la cantidad no es válida
        }

        LocalDate fechaSuministro = BtonFechaSuministro.getValue();

        // Verificar si el medicamento tiene suficiente cantidad
        if (medicamentoSeleccionado != null && pacienteSeleccionado != null && enfermeroSeleccionado != null && fechaSuministro != null) {
            // Intentar reducir la cantidad del medicamento
            if (medicamentoSeleccionado.reducirCantidad(cantidad)) {
                // Crear el suministro
                Suministro suministro = new Suministro(pacienteSeleccionado, medicamentoSeleccionado, cantidad, fechaSuministro, enfermeroSeleccionado);
                guardarSuministro(suministro);

                // Actualizar la lista de medicamentos y guardarla
                actualizarMedicamentos();

                mostrarMensajeSuministroGuardado();
            } else {
                // Mostrar un mensaje de error si la cantidad es insuficiente
                mostrarMensajeError("Error", "Cantidad insuficiente de medicamento.");
            }
        } else {
            // Mostrar mensaje de error si algún dato está incompleto
            mostrarMensajeError("Error", "Datos incompletos.");
        }
    }

    private void actualizarMedicamentos() {
        try {
            objectMapper.writeValue(new File(FILE_MEDICAMENTOS_PATH), listaMedicamentos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void guardarSuministro(Suministro suministro) {
        try {
            List<Suministro> suministros = cargarSuministros();
            suministros.add(suministro);
            objectMapper.writeValue(new File(FILE_SUMINISTROS_PATH), suministros);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Suministro> cargarSuministros() {
        try {
            List<Suministro> suministros = objectMapper.readValue(new File(FILE_SUMINISTROS_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Suministro.class));
            return suministros != null ? suministros : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Método para mostrar mensajes de éxito
    private void mostrarMensajeSuministroGuardado() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Suministro Guardado");
        alert.setHeaderText(null);
        alert.setContentText("Suministro guardado correctamente.");
        alert.showAndWait();
    }

    // Método para mostrar mensajes de error
    private void mostrarMensajeError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
