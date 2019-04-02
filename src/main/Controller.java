package main;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    @FXML
    TextField savePathTextField;

    @FXML
    Label statusBar;

    @FXML
    Button
            download,
            search,
            mute;

    @FXML
    CheckBox
            jpgCheckBox,
            pngCheckBox,
            gifCheckBox,
            pdfCheckBox,
            aviCheckBox,
            docCheckBox,
            exeCheckBox,
            zipCheckBox;

    @FXML
    ComboBox urlComboBox;

    @FXML
    ListView<String> foundPicsLV;


    private PictureFinder pictureFinder;
    private LinkedList<String> pictures;
    Stage directoryChooserStage;
    //TODO: historia do oddzielnej klasy, zapisywanie historii do pliku można robić tylko podczas zamykania programu

    private static SearchHistoryManager searchHistoryManager;
    private ObservableList<String> searchHistoryOL;
    private List<String> selectedExtensions;
    private List<CheckBox> checkBoxes;
    private BackgroundMusic backgroundMusic;

    public static SearchHistoryManager getSearchHistoryManager() {
        return searchHistoryManager;
    }

    public void initialize() throws FileNotFoundException {
        pictureFinder = new PictureFinder();
        pictures = pictureFinder.getPictureList();
        urlComboBox.setEditable(true);
        searchHistoryManager = new SearchHistoryManager();
        searchHistoryOL = searchHistoryManager.getHistoryOL();
        searchHistoryManager.readFromHistory();
        urlComboBox.setItems(searchHistoryOL);
        foundPicsLV.setCellFactory(TextFieldListCell.forListView());
        for (int i = 0; i < 10; ++i)
            foundPicsLV.getItems().add("");
        checkBoxes = new ArrayList<>(Arrays.asList(jpgCheckBox, pngCheckBox, gifCheckBox));
        selectedExtensions = new ArrayList<>();
        backgroundMusic = new BackgroundMusic();
        setMuteImage();
    }

    public void setMuteImage(){
        Image image = new Image(getClass().getResourceAsStream("..\\mute.png"));
        ImageView imageView = new ImageView(image);
        mute.setGraphic(imageView);
    }


    public void muteButtonClicked(){
        mute.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent){
                backgroundMusic.getMediaPlayer().setMute(true);
            }
        } );
    }


    public void selectExtensions(){
        for(CheckBox checkBox : checkBoxes){
            if(checkBox.isSelected())
                selectedExtensions.add(checkBox.getText());
        }
    }


    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
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

            String fileName = picToSave.substring(picToSave.lastIndexOf('/') + 1);
            InputStream inputStream = new URL(picToSave).openStream();
            Files.copy(inputStream, Paths.get(savePathTextField.getText() + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        statusBar.setText("Images downloaded and saved");
    }


//    public void fileExtensionSelect() {
//        //TODO: zamiast tego sprawdzać check boxy przy wywolaniu search
//        jpgCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!jpgCheckBox.isSelected()) {
//                    //don't look for .jpg
//
//                }
//            }
//        });
//
//        pngCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!pngCheckBox.isSelected()) {
//                    //don't look for .png
//                }
//            }
//        });
//
//        gifCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!gifCheckBox.isSelected()) {
//                    //don't look for .gif
//                }
//            }
//        });
//    }


    public void onSearchButtonClicked() {
        String url = (String) urlComboBox.getSelectionModel().getSelectedItem();
       // searchHistoryManager.addToHistory(url);
//        System.out.println(searchHistoryManager.getHistoryHelpList());
//        System.out.println(searchHistoryManager.getHistoryOL());


       // fileExtensionSelect();
        selectExtensions();
        try {
            String pageContent = downloadPage(url);
            pictureFinder.search(pageContent, selectedExtensions);
            statusBar.setText("Page scanned successfully");
        } catch (IOException e) {
            e.printStackTrace();
            statusBar.setText("Connection failed");
        }

        for (String p : pictures)
            pictureFinder.add(p);

        foundPicsLV.setItems(FXCollections.observableArrayList(pictures));
        searchHistoryReset();
        statusBar.setText(pictureFinder.getPictureList().size() + " pictures found successfully");
    }


    private void searchHistoryReset(){
        searchHistoryOL.setAll(searchHistoryManager.getHistoryHelpList());
        urlComboBox.setItems(searchHistoryOL);
    }

}