package com.nexttozero.arbattleground;

import java.util.ArrayList;

public class PreviewModel {

    private String mName;
    private String mDesc;

    public PreviewModel(String name, String desc){
        mName = name;
        mDesc = desc;



    }

    public String getName(){

        return mName;

    }

    public String getDesc(){

        return mDesc;

    }

    private static int lastModelID = 0;

    public static ArrayList<PreviewModel> createModelsList(int numModels) {
        ArrayList<PreviewModel> previewModels = new ArrayList<PreviewModel>();

        for (int i = 1; i <= numModels; i++) {
            previewModels.add(new PreviewModel("Model " + ++lastModelID, "BlankDesc"));
        }

        return previewModels;
    }



}
