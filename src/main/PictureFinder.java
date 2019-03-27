package main;

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

    public PictureFinder() {
        pictureList = new LinkedList<>();
        setPicLimit(Integer.MAX_VALUE);
//        pictureList.add("https://www.e-kwiaty.pl/ekwiaty/images/bo164-d03d.jpg");
//        pictureList.add("https://www.e-kwiaty.pl/ekwiaty/images/bo002-xl.jpg");
//        pictureList.add("https://www.e-kwiaty.pl/ekwiaty/images/bo208-xl.jpg");
    }

    private Pattern picturePattern = Pattern.compile("\"(https?[^\"?]+(jpg|png|gif))(?:\\?[^\"]+)?\"");
    //private static final Pattern picturePattern = Pattern.compile("\"(https?[^\"]+(jpg|png|gif))\"");

    //TODO: weryfikować czy obrazek już był ściagany
    public void search(String text) {
        pictureList.clear();
        Matcher matcher = picturePattern.matcher(text);
        while (matcher.find()) {
            if(pictureList.size() >= picLimit)
                break;

            String url = matcher.group(1);
            pictureList.add(url);
            System.out.println(url);
        }
    }


    public void clear(){
        pictureList.clear();
    }

    public String nextPicture(){
        return pictureList.poll();
    }

    public String getPicture(int index){
        return pictureList.get(index);
    }

    public void setPicLimit(int picLimit) {
        this.picLimit = picLimit;
    }

    public LinkedList<String> getPictureList() {
        return pictureList;
    }

    public void setPossibleExtensions(List<String> extensions){
        //TODO: generowanie regexa
    }
}
