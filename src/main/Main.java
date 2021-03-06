package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Picture Finder");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    @Override
    public void stop(){
        Controller.getSearchHistoryManager().historyDownload();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
