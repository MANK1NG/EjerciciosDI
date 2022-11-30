package com.example.crearagenda;
import java.io.File;
import java.io.IOException;

import java.util.prefs.Preferences;

import com.example.crearagenda.model.PersonListWrapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.*;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();


    public MainApp() {
        // Añadir datos de las personas
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }



    //Método para retornar los datos de las personas

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    //Iniciar el escenario
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        // Poner el codigo
        //TODO
        Image image = new Image("file:src/main/resources/images/com.example.crearagenda/Agenda.png");
        primaryStage.getIcons().add(image);
        initRootLayout();

        showPersonOverview();
    }

    //Inicar el root
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Mostrar la escena del root
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Mostrar la persona dentro del root
    public void showPersonOverview() {
        try {
            // Cargar los datos de la persona
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("PersonaOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Poner los datos de la persona en el centro
            rootLayout.setCenter(personOverview);
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean showPersonEditDialog(Person person) {
        try {
            // Cargue el archivo fxml y cree una nueva etapa para el cuadro de diálogo emergente.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crear el escenario del diálogo.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Establecer la persona en el controlador.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Mostrar el cuadro de diálogo y esperar hasta que el usuario lo cierre.
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //retorna el ecenario
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public File getPersonFilePath() {
       Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        //Creo una variable de tipo caracter que obtenga la ruta del fichero
        String filePath = prefs.get("filePath", null);
        //If para devolver ruta en ell caso de haber fichero

        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Cambia el titulo del escenario
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Cambia el titulo del escenario
            primaryStage.setTitle("AddressApp");
        }
    }

    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Lectura de XML del archivo y desagregación.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Guarde la ruta del archivo en el registro.
            setPersonFilePath(file);

        } catch (Exception e) { // captura cualquier excepción
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudieron cargar los datos");
            alert.setContentText("No se pudieron cargar los datos del archivo:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Envolviendo los datos de nuestra persona.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Ordenar y guardar XML en el archivo.
            m.marshal(wrapper, file);

            //Guarde la ruta del archivo en el registro.
            setPersonFilePath(file);
        } catch (Exception  e) { // captura cualquier excepción
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha podido guardar");
            alert.setContentText("No se pudieron cargar los datos del archivo:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}