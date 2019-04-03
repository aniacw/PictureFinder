package main;

import java.util.ArrayList;
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


    PictureFinder() {
        pictureList = new LinkedList<>();
        setPicLimit(Integer.MAX_VALUE);
        uniquePics = new HashSet();
    }

    private Pattern picturePattern = Pattern.compile("\"(https?[^\"?]+(jpg|png|gif))(?:\\?[^\"]+)?\"");
    //private static final Pattern picturePattern = Pattern.compile("\"(https?[^\"]+(jpg|png|gif))\"");

    //TODO: weryfikować czy obrazek już był ściagany
    void search(String text, List<String> extensionList) {
        clear();
       // Matcher matcher = picturePattern.matcher(text);
        Matcher matcher = createPattern(extensionList).matcher(text);
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
        uniquePics.clear();
        pictureList.clear();
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


    public Pattern createPattern(List<String> extensions){

        String beginning = "\"(https?[^\"?]+(";
        String extension = "";
        String end = "))(?:\\?[^\"]+)?\"";

        StringBuilder builder = new StringBuilder();
        if (extensions.size() > 0) {
            builder.append(extensions.get(0));
            for (int i = 1; i < extensions.size(); ++i) {
                builder.append('|');
                builder.append(extensions.get(i));
            }
            extension = builder.toString();
        }
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append(beginning);
        patternBuilder.append(extension);
        patternBuilder.append(end);

        String createdPattern = patternBuilder.toString();

        System.out.println(createdPattern);


        return Pattern.compile(createdPattern);
    }

//    public void convertRelativeToAbsolute(String searchedUrl){
//        Pattern mainPath =  Pattern.compile("\"(https?)[^\"]+/$");
//        Pattern relativePath = Pattern.compile("\"(/[^\"?]+jpg)(?:\\?[^\"]+)?\"");
//        String mainPathString;
//        String relativePathString;
//        Matcher matcher = mainPath.matcher(searchedUrl);
//        if(matcher.find())
//            mainPathString = matcher.group(1);
//
//        Matcher matcher1 = relativePath.matcher()
//
//
//    }
}
