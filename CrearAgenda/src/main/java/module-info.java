module com.example.crearagenda {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.crearagenda to javafx.fxml;
    exports com.example.crearagenda;
}