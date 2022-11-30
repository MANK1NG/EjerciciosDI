package com.example.crearagenda;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import com.example.crearagenda.MainApp;


public class RootLayoutController {

    // hace referencia al main
    private MainApp mainApp;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }


    // Abre un FileChooser para permitir que el usuario seleccione una libreta de direcciones para cargar.

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Poner una extensión de filtros
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Mostrar diálogo de archivo abierto
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);

        }
    }

    // Guarda el archivo en el archivo de persona que está abierto actualmente. Si no hay archivo abierto, se muestra el cuadro de diálogo "guardar como".
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    // Abre un FileChooser para permitir que el usuario seleccione una libreta de direcciones para cargar.
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        //  Poner una extensión de filtros
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Mostrar diálogo de archivo abierto
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Asegúrate de que tenga la extensión correcta.
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }


    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("Sobre mí");
        alert.setContentText("Autor: Iván Palomino \nWebsite: http://code.makery.ch");

        alert.showAndWait();
    }

   //Cierra la aplicación
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}