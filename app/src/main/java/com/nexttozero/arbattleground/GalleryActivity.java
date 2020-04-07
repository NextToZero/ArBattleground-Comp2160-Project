package com.nexttozero.arbattleground;




import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

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

    public final int MODEL_COUNT = 4;



    ArrayList<PreviewModel> previewModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Creates the Gallery activity.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        rv_previewModel = findViewById(R.id.rvPreviewModel);


       //Creation of PreviewModels arraylist. This value can be changed via the MODEL_COUNT constant up above. Eventually
        //to be made dynamic, so it reads from the JSON File.

        previewModels = new ArrayList<PreviewModel>(MODEL_COUNT);


        populateArrayList();

        PreviewModelAdapter modelAdapter = new PreviewModelAdapter(previewModels, this);


        String test = "" + modelAdapter.getItemCount();
        Log.d(MODEL_ADAPTER_TEST, test);

        //Logs for bugtesting.

        /**
         *
         *
         * This was an area I had more difficulty than I would have expected. At this point,
         * the following if statement *could* be removed. However, I'm trying to make it a habit to
         * leave more Logs, for easy debugging.
         *
         *
         *
         */

        if(rv_previewModel != null){

            Log.d(MODEL_ADAPTER_TEST,"rv_previewModel is ACTIVE.");

        }

        rv_previewModel.setAdapter(modelAdapter);
        rv_previewModel.setLayoutManager(new LinearLayoutManager(this));

        //Work in Progress. Should allow the user to touch an entry on the list, but it's non-functional right now.
        /**
         *
         *
         * Another feature I would like to implement eventually. Users could select a model from the Gallery, as well as the Quick Select.
         * However, I had a rough time getting the addOnItemTouchListener to actually do what it should.
         *
         *
         *
         *
         *
         */

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

        //method to populate ListArray for Recycler view.
        //pulls from the Models json.

        Log.d(JSON_OBJECT,"Starting ArrayList Population");

        try{
            JSONObject jsObj = new JSONObject( loadJSONFromAsset());
            JSONArray jsArray =jsObj.getJSONArray("models");

            for(int i=0; i< jsArray.length();i++){
                JSONObject jsEntry = jsArray.getJSONObject(i);

                //retrieves all fields from each JsonObject.

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
