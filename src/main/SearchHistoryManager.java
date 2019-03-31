package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.LinkedList;

public class SearchHistoryManager {

    private LinkedList<String> historyHelpList;
    private ObservableList<String> historyOL;

     SearchHistoryManager() {
        historyHelpList = new LinkedList<>();
        historyOL = FXCollections.observableArrayList(historyHelpList);
    }

    void historyDownload() {
        try {
            FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Ania\\Desktop\\picsHistory", "history.txt"));
            for (String link : historyOL) {
                fileWriter.write(link);
                fileWriter.write('\n');
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void addToHistory(String newUrl) {
        if (historyHelpList.size() == 10)
            historyHelpList.removeLast();
        historyHelpList.add(newUrl);
    }


    void readFromHistory() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Ania\\Desktop\\picsHistory\\history.txt"));
        try {
            String link = bufferedReader.readLine();
            historyHelpList.add(link);//***************
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public LinkedList<String> getHistoryHelpList() {
        return historyHelpList;
    }


    public ObservableList<String> getHistoryOL() {
        return historyOL;
    }
}



