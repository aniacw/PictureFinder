package main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PictureFinder {
    private static class FileData {
        String filename;
        String extension;
        String url;
    }

    private LinkedList<String> pictureList;
    private int picLimit;
    private HashSet<String> uniquePics;

    public PictureFinder() {
        pictureList = new LinkedList<>();
        setPicLimit(Integer.MAX_VALUE);
        uniquePics = new HashSet();
    }

    private Pattern picturePattern = Pattern.compile("\"(https?[^\"?]+(jpg|png|gif))(?:\\?[^\"]+)?\"");
    //private static final Pattern picturePattern = Pattern.compile("\"(https?[^\"]+(jpg|png|gif))\"");

    //TODO: weryfikować czy obrazek już był ściagany
    //ok tylko pokazuje za duzo zjec w statusbar
    void search(String text) {
        clear();
        Matcher matcher = picturePattern.matcher(text);
        while (matcher.find()) {
            if (pictureList.size() >= picLimit)
                break;

            String url = matcher.group(1);
            if (uniquePics.add(url)) {
                pictureList.add(url);
            }
        }
    }


    void add(String pic) {
        if (uniquePics.add(pic))
            pictureList.add(pic);
    }


    private void clear() {
        pictureList.clear();
    }

    public String nextPicture() {
        return pictureList.poll();
    }

    public String getPicture(int index) {
        return pictureList.get(index);
    }

    private void setPicLimit(int picLimit) {
        this.picLimit = picLimit;
    }

    LinkedList<String> getPictureList() {
        return pictureList;
    }

    public void setPossibleExtensions(List<String> extensions) {
        //TODO: generowanie regexa
    }
}
