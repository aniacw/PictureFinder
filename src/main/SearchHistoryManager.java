package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.LinkedList;

public class SearchHistoryManager {

    private LinkedList<String> historyHelpList;
    private ObservableList<String> historyOL;

    public SearchHistoryManager() {
        historyHelpList = new LinkedList<>();
        historyOL = FXCollections.observableArrayList(historyHelpList);
    }

    public void historyDownload() {
        try {
            FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Ania\\Desktop\\picsHistory", "history.txt"));
            for (String link : historyOL) {
                System.out.println(historyHelpList);
                System.out.println(link);
                fileWriter.write(link);
                fileWriter.write('\n');
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addToHistory(String newUrl) {
        if (historyHelpList.size() == 10)
            historyHelpList.removeLast();
        historyHelpList.add(newUrl);
        historyOL = FXCollections.observableArrayList(historyHelpList);
       // historyOL.add(newUrl);
        //System.out.println(historyHelpList);
    }


   public void readFromHistory() throws FileNotFoundException {
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



