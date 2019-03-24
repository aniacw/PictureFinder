package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    @FXML
    ImageView test;

    @FXML
    TextField savePathTextField;

    @FXML
    Label statusBar;

    @FXML
    Button
            download,
            search;

    @FXML
    CheckBox
            jpgCheckBox,
            pngCheckBox,
            gifCheckBox;

    @FXML
    ComboBox urlComboBox;

    private PictureFinder pictureFinder;
    private LinkedList<String> pictures;
    Stage directoryChooserStage;
    private ObservableList<String> searchHistory;
    private LinkedList<String> searchHistoryHelpList;


    public void initialize() {
        pictureFinder = new PictureFinder();
        pictureFinder.setPicLimit(5);
        pictures = new LinkedList<>();
        jpgCheckBox.setSelected(true);
        pngCheckBox.setSelected(true);
        gifCheckBox.setSelected(true);
        urlComboBox.setEditable(true);
        searchHistoryHelpList = new LinkedList<>();
        searchHistory = FXCollections.observableArrayList();
    }


    private static String downloadPage(String website) throws IOException {
        URL url = new URL(website);
        URLConnection connection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }


    public void onDownloadButtonClicked(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(directoryChooserStage);
        savePathTextField.setText(selectedDirectory.getAbsolutePath());

        for (String pic : pictureFinder.getPictureList()) {
            String picToSave = pic;

            String fileName = picToSave.substring(picToSave.lastIndexOf('/') + 1, picToSave.length());
            //InputStream inputStream = new URL(pictureFinder.nextPicture()).openStream();
            InputStream inputStream = new URL(picToSave).openStream();
            Files.copy(inputStream, Paths.get(savePathTextField.getText() + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        statusBar.setText("Images downloaded and saved");
    }


    public void fileExtensionSelect() {
        jpgCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!jpgCheckBox.isSelected()) {
                    //don't look for .jpg
                }
            }
        });

        pngCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!pngCheckBox.isSelected()) {
                    //don't look for .png
                }
            }
        });

        gifCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!gifCheckBox.isSelected()) {
                    //don't look for .gif
                }
            }
        });
    }


    private void addToHistory(String newUrl) {
        if (searchHistoryHelpList.size() == 10)
            searchHistoryHelpList.removeLast();

        searchHistoryHelpList.add(newUrl);
    }


    public void onSearchButtonClicked() {
        String url = urlComboBox.getAccessibleText();
        //"https://www.e-kwiaty.pl/";
        addToHistory(url);
        String pictureFound = pictureFinder.nextPicture();
        fileExtensionSelect();
        int picCount = 0;

        while (pictureFound != null) {

                      try {
                String pageContent = downloadPage(url);
                pictureFinder.search(pageContent);
            picCount++;
            statusBar.setText(picCount + " pictures found successfully");

            System.out.println(pictureFound);

            } catch (IOException e) {
                e.printStackTrace();
                statusBar.setText("Connection failed");
            }
            pictureFound = pictureFinder.nextPicture();
        }
        searchHistory.setAll(searchHistoryHelpList);
        urlComboBox.setItems(searchHistory);
    }

}