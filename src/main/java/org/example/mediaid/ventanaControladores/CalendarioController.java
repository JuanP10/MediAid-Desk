package org.example.mediaid.ventanaControladores;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.mediaid.modelos.Medicamento;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarioController {

    @FXML
    private TextField TextNombreMedicamento;

    @FXML
    private TextField TextCantidad;

    @FXML
    private MenuButton BtonTipoMedicamento;

    @FXML
    private DatePicker BtonFecha;

    @FXML
    private MenuButton BtonFormaAdquisicion;

    @FXML
    private Button BtonAgregarMedicamento;

    private final String filePath = "src/main/java/org/example/mediaid/json/medicamentos.json";

    @FXML
    private void initialize() {
        // Poblar el menú de TipoMedicamento
        for (Medicamento.TipoMedicamento tipo : Medicamento.TipoMedicamento.values()) {
            MenuItem menuItem = new MenuItem(tipo.name());
            menuItem.setOnAction(event -> BtonTipoMedicamento.setText(menuItem.getText())); // Establece el texto del botón
            BtonTipoMedicamento.getItems().add(menuItem);
        }

        // Poblar el menú de FormaAdquisicion
        for (Medicamento.FormaAdquisicion forma : Medicamento.FormaAdquisicion.values()) {
            MenuItem menuItem = new MenuItem(forma.name());
            menuItem.setOnAction(event -> BtonFormaAdquisicion.setText(menuItem.getText())); // Establece el texto del botón
            BtonFormaAdquisicion.getItems().add(menuItem);
        }
    }


    @FXML
    private void agregarMedicamento() {
        String nombre = TextNombreMedicamento.getText().trim();
        String cantidadStr = TextCantidad.getText().trim();
        Medicamento.TipoMedicamento tipoMedicamento;
        LocalDate fechaVencimiento = BtonFecha.getValue();
        Medicamento.FormaAdquisicion formaAdquisicion;

        // Validar campos
        if (nombre.isEmpty() || cantidadStr.isEmpty() || fechaVencimiento == null ||
                BtonTipoMedicamento.getText().equals("Tipo Medicamento") ||
                BtonFormaAdquisicion.getText().equals("Forma de Adquisición")) {
            showAlert("Error", "Por favor, completa todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        // Validar cantidad
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            showAlert("Error", "La cantidad debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        // Obtener valores de los enum
        try {
            tipoMedicamento = Medicamento.TipoMedicamento.valueOf(BtonTipoMedicamento.getText().toUpperCase());
            formaAdquisicion = Medicamento.FormaAdquisicion.valueOf(BtonFormaAdquisicion.getText().toUpperCase());
        } catch (IllegalArgumentException e) {
            showAlert("Error", "Tipo de medicamento o forma de adquisición no válidos.", Alert.AlertType.ERROR);
            return;
        }

        // Crear objeto medicamento
        Medicamento medicamento = new Medicamento(nombre, cantidad, tipoMedicamento, fechaVencimiento, formaAdquisicion);

        // Configurar ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            File file = new File(filePath);
            List<Medicamento> medicamentos;

            // Leer el archivo si existe
            if (file.exists()) {
                medicamentos = objectMapper.readValue(file, new TypeReference<List<Medicamento>>() {});
            } else {
                medicamentos = new ArrayList<>();
            }

            // Actualizar o agregar el medicamento
            boolean encontrado = false;
            for (Medicamento existente : medicamentos) {
                if (existente.getNombre().equalsIgnoreCase(medicamento.getNombre())) {
                    existente.aumentarCantidad(medicamento.getCantidad());
                    existente.setFechaVencimiento(
                            medicamento.getFechaVencimiento().isAfter(existente.getFechaVencimiento())
                                    ? medicamento.getFechaVencimiento()
                                    : existente.getFechaVencimiento()
                    );
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                medicamentos.add(medicamento);
            }

            // Guardar el archivo actualizado
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, medicamentos);

            showAlert("Éxito", "El medicamento fue agregado o actualizado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Hubo un problema al guardar el medicamento.", Alert.AlertType.ERROR);
        }
    }



    private void limpiarCampos() {
        TextNombreMedicamento.clear();
        TextCantidad.clear();
        BtonTipoMedicamento.setText("Tipo Medicamento");
        BtonFecha.setValue(null);
        BtonFormaAdquisicion.setText("Forma de Adquisición");
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
