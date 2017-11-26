package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amardeep on 10/28/2017.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    OnItemClickListener lisn;
    private List<RecipeStep> mRecipeSteps;
    public RecipeStepAdapter(Context context,OnItemClickListener listen) {
        this.mContext=context;
        this.lisn = listen;
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeStep d);
    }
    public void setData(ArrayList<RecipeList> recipeLists){
        this.mRecipeSteps = recipeLists.get(0).getSteps();
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_steps,parent,false);
        viewHolder =new MyItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyItemViewHolder) holder).StepName.setText(mRecipeSteps.get(position).getShortDescription());
        ((MyItemViewHolder) holder).bind(mRecipeSteps.get(position),lisn);
    }

    @Override
    public int getItemCount() {
        if(mRecipeSteps==null||mRecipeSteps.size()==0){
            return -1;
        }
        return mRecipeSteps.size();
    }
    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        TextView StepName;
        public MyItemViewHolder(View rootview) {
            super(rootview);
            StepName = (TextView)rootview.findViewById(R.id.recipe_step_text);
        }
        public void bind(final RecipeStep d, final OnItemClickListener listener){
            StepName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(d);
                }
            });
        }
    }
}
