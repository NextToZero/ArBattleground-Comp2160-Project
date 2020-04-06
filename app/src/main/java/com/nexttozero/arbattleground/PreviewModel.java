package com.nexttozero.arbattleground;

import java.io.InputStream;
import java.util.ArrayList;

public class PreviewModel {

    private String mName;
    private String mIcon;
    private String mImage;
    private String mDesc;

    public PreviewModel(String name, String icon, String image, String desc){
        mName = name;
        mIcon = icon;
        mImage = image;
        mDesc = desc;




    }

    public String getName(){
        return mName;
    }

    public String getDesc(){
        return mDesc;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getImage() {
        return mImage;
    }

    private static int lastModelID = 0;




}
