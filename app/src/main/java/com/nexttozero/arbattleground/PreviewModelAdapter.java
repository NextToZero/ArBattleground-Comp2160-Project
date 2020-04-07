package com.nexttozero.arbattleground;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PreviewModelAdapter extends RecyclerView.Adapter<PreviewModelAdapter.ViewHolder> {
    public Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View modelView = inflater.inflate(R.layout.item_model, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(modelView);
        return viewHolder;
    }

    public static int getImageId(Context contextIn, String imageName) {
        return contextIn.getResources().getIdentifier("drawable/" + imageName, null, contextIn.getPackageName());
    }

    @Override
    public void onBindViewHolder(PreviewModelAdapter.ViewHolder viewHolder, int position) {
        PreviewModel model = mPreviewModels.get(position);

        // Set item views based on your views and data model
        TextView nameText = viewHolder.nameTextView;
        nameText.setText(model.getName());

        TextView descText = viewHolder.descTextView;
        descText.setText(model.getDesc());

        ImageView iconImage = viewHolder.iconImageView;
        iconImage.setImageResource(getImageId(context,model.getIcon()));

        ImageView previewImage = viewHolder.previewImageView;
        previewImage.setImageResource(getImageId(context, model.getImage()));


/**
 *
 * I was really stumped by this one. Trying to use a string as the input for setImageResource.
 * Kinda strange how you can't access the resources until you bring in Context from a proper activity.
 * Would this have been better run through the main activity?
 *
        ImageView previewImage = viewHolder.previewImageView;
        String previewStr = "R.drawable." + model.getImage();
        previewImage.setImageResource(Integer.parseInt(previewStr));
**/




    }

    @Override
    public int getItemCount() {
        return mPreviewModels.size();
    }

    // Store a member variable for the previewModel
    public List<PreviewModel> mPreviewModels;

    // Pass in the PreviewModel array into the constructor
    public PreviewModelAdapter(List<PreviewModel> previewModels, Context contextIn) {
        mPreviewModels = previewModels;
        context = contextIn;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descTextView;
        public ImageView iconImageView;
        public ImageView previewImageView;



        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.model_name);
            descTextView = (TextView) itemView.findViewById(R.id.model_desc);
            iconImageView = (ImageView) itemView.findViewById(R.id.model_icon);
            previewImageView = (ImageView) itemView.findViewById(R.id.model_preview);
        }
    }
}