package com.nexttozero.arbattleground;




import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class GalleryActivity extends AppCompatActivity {
    public static final String MODEL_ADAPTER_TEST = "ModelAdapterTest";
    public RecyclerView rv_previewModel;



    ArrayList<PreviewModel> previewModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        rv_previewModel = findViewById(R.id.rvPreviewModel);


        previewModels = PreviewModel.createModelsList(20);

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
    

    
    
}
