package org.openjfx;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.global.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("lobby"));
        stage.setScene(scene);
        stage.setHeight(400);
        stage.show();
        scene.getStylesheets().add(App.class.getResource("stylesheet.css").toString());
        setDictionary();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Scene getScene() {
        return scene;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public void setDictionary(){
        Gson gson = new Gson();
        String[] items = new String[0];
        try {
            items = gson.fromJson(new FileReader(App.class.getResource("dictionary.cfg").getPath()), String[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for( String i: items){
            Data.dictionnary.add(i);
        }
    }

}