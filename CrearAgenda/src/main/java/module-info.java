module com.example.crearagenda {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;

    requires java.xml.bind;


    opens com.example.crearagenda to javafx.fxml;
    exports com.example.crearagenda;
    exports com.example.crearagenda.model;
    opens com.example.crearagenda.model to javafx.fxml;
}