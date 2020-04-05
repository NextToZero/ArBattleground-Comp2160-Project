package com.nexttozero.arbattleground;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PreviewModelAdapter extends RecyclerView.Adapter<PreviewModelAdapter.ViewHolder> {

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

    @Override
    public void onBindViewHolder(PreviewModelAdapter.ViewHolder viewHolder, int position) {
        PreviewModel model = mPreviewModels.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return mPreviewModels.size();
    }

    // Store a member variable for the previewModel
    public List<PreviewModel> mPreviewModels;

    // Pass in the PreviewModel array into the constructor
    public PreviewModelAdapter(List<PreviewModel> previewModels) {
        mPreviewModels = previewModels;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button messageButton;


        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.model_name);

        }
    }
}