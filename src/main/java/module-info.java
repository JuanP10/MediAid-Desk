module org.example.mediaid {
    requires javafx.controls;
    requires javafx.fxml;
    // Dependencia de Jackson
    opens org.example.mediaid.modelos to com.fasterxml.jackson.databind;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.json;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires itextpdf;
    requires layout;
    requires io;
    requires kernel;

    opens org.example.mediaid to javafx.fxml;
    exports org.example.mediaid;
    exports org.example.mediaid.ventanaControladores;
    opens org.example.mediaid.ventanaControladores to javafx.fxml;
}