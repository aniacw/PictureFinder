package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    @FXML
    TextArea content;

    private PictureFinder pictureFinder;
    private LinkedList<String> pictures;
    Stage directoryChooserStage;
    //TODO: historia do oddzielnej klasy, zapisywanie historii do pliku można robić tylko podczas zamykania programu
    private ObservableList<String> searchHistory;
    private LinkedList<String> searchHistoryHelpList;


    @Override
    public void finalize(){

    }


    public void initialize() throws FileNotFoundException {
        pictureFinder = new PictureFinder();
        //pictureFinder.setPicLimit(5);//??
        pictures = new LinkedList<>();
        jpgCheckBox.setSelected(true);
        pngCheckBox.setSelected(true);
        gifCheckBox.setSelected(true);
        urlComboBox.setEditable(true);
        searchHistoryHelpList = new LinkedList<>();
        searchHistory = FXCollections.observableArrayList();
        readFromHistory();
        urlComboBox.setItems(searchHistory);
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


    public void onDownloadButtonClicked() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(directoryChooserStage);
        savePathTextField.setText(selectedDirectory.getAbsolutePath());

        for (String pic : pictureFinder.getPictureList()) {
            String picToSave = pic;

            String fileName = picToSave.substring(picToSave.lastIndexOf('/') + 1, picToSave.length());
            InputStream inputStream = new URL(picToSave).openStream();
            Files.copy(inputStream, Paths.get(savePathTextField.getText() + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        statusBar.setText("Images downloaded and saved");
    }


    private void historyDownload() {
        try {
            FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Ania\\Desktop\\picsHistory", "history.txt"));
            for (String link : searchHistory){
                fileWriter.write(link);
                fileWriter.write('\n');
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void fileExtensionSelect() {
        //TODO: zamiast tego sprawdzać check boxy przy wywolaniu search
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

    private void readFromHistory() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Ania\\Desktop\\picsHistory\\history.txt"));
        try {
            String link = bufferedReader.readLine();
            searchHistory.add(link);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onSearchButtonClicked() {
        String url = (String) urlComboBox.getSelectionModel().getSelectedItem();
        //https://www.cntraveller.com/gallery/pictures-of-spain
        addToHistory(url);
        //   String pictureFound = pictureFinder.nextPicture();
        String pictureFound = "xxxxx";

        fileExtensionSelect();
        int picCount = 0;

        // while (pictureFound != null) {
        try {
            String pageContent = downloadPage(url);
            pictureFinder.search(pageContent);
            content.setText(pageContent);
            statusBar.setText("Page scanned successfully");
        } catch (IOException e) {
            e.printStackTrace();
            statusBar.setText("Connection failed");
        }

        searchHistory.setAll(searchHistoryHelpList);
        urlComboBox.setItems(searchHistory);
        historyDownload();
        statusBar.setText(pictureFinder.getPictureList().size() + " pictures found successfully");
    }

}