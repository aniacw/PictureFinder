package main;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PictureFinder {

    private LinkedList<String> pictureList;
    private int picLimit;

    public PictureFinder() {
        pictureList = new LinkedList<>();
//        pictureList.add("https://www.e-kwiaty.pl/ekwiaty/images/bo164-d03d.jpg");
//        pictureList.add("https://www.e-kwiaty.pl/ekwiaty/images/bo002-xl.jpg");
//        pictureList.add("https://www.e-kwiaty.pl/ekwiaty/images/bo208-xl.jpg");
    }

    private static final Pattern picturePattern = Pattern.compile("(https?[^\"]+)(jpg|png|gif)");


    public void search(String text) {
        Matcher matcher = picturePattern.matcher(text);
        while (matcher.find()) {
            if(pictureList.size() >= picLimit)
                break;

            String url = matcher.group();
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
}
