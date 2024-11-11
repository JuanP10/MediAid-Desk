module org.example.mediaid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens org.example.mediaid to javafx.fxml;
    exports org.example.mediaid;
}