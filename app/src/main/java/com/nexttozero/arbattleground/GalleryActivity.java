package com.nexttozero.arbattleground;




import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class GalleryActivity extends AppCompatActivity {
    public static final String MODEL_ADAPTER_TEST = "ModelAdapterTest";
    public static final String JSON_OBJECT = "JSON OBJECT";
    public RecyclerView rv_previewModel;



    ArrayList<PreviewModel> previewModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        rv_previewModel = findViewById(R.id.rvPreviewModel);


       // previewModels = PreviewModel.createModelsList(20);

        previewModels = new ArrayList<PreviewModel>(4);


        populateArrayList();

        PreviewModelAdapter modelAdapter = new PreviewModelAdapter(previewModels);


        String test = "" + modelAdapter.getItemCount();
        Log.d(MODEL_ADAPTER_TEST, test);

        if(rv_previewModel != null){

            Log.d(MODEL_ADAPTER_TEST,"rv_previewModel is ACTIVE.");

        }

        rv_previewModel.setAdapter(modelAdapter);
        rv_previewModel.setLayoutManager(new LinearLayoutManager(this));

        //Work in Progress. Should allow the user to touch an entry on the list, but it's non-functional right now.
        rv_previewModel.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public void onTouchEvent(RecyclerView recycler, MotionEvent event) {



            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recycler, MotionEvent event) {
                return false;
            }

        });



        
    }
    

    public void populateArrayList(){

        Log.d(JSON_OBJECT,"Starting ArrayList Population");

        try{
            JSONObject jsObj = new JSONObject( loadJSONFromAsset());
            JSONArray jsArray =jsObj.getJSONArray("models");

            for(int i=0; i< jsArray.length();i++){
                JSONObject jsEntry = jsArray.getJSONObject(i);
                String nameIn = jsEntry.getString("name");
                String iconIn = jsEntry.getString("icon");
                String previewIn = jsEntry.getString("preview_image");
                String descIn = jsEntry.getString("description");
                Log.d(JSON_OBJECT, "Name" + nameIn);

                previewModels.add(new PreviewModel(nameIn, iconIn, previewIn, descIn));

            }


        }
        catch(JSONException e){
            Log.d(JSON_OBJECT," ArrayList Population Failed.");
            e.printStackTrace();
        }



    }


    public String loadJSONFromAsset() {
        Log.d(JSON_OBJECT,"Starting loadJSONFromAsset");
        String json = null;
        try {
            InputStream is = this.getAssets().open("models.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.d(JSON_OBJECT,"loadJSONFromAsset Failed");
            ex.printStackTrace();

            return null;
        }
        return json;
    }
    
}
