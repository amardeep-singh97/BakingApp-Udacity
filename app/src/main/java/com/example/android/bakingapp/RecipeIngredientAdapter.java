package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amardeep on 10/24/2017.
 */

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<RecipeIngredient> mRecipeListIngredient;
    public RecipeIngredientAdapter(Context context){
        Log.v("KLKKLLK","bbbbbbb");
        mContext=context;

    }
    public void setData(ArrayList<RecipeList> recipesIn){
        this.mRecipeListIngredient = recipesIn.get(0).getIngredients();
        notifyDataSetChanged();
        Log.v("KLKKLLK","aaaaaaa");
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingre,parent,false);
        viewHolder =new MyItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.v("RECIPEINGREDIENT",mRecipeListIngredient.get(position).getIngredient());
        ((MyItemViewHolder) holder).quantity.setText(String.valueOf(mRecipeListIngredient.get(position).getQuantity()));
        ((MyItemViewHolder) holder).ingredient.setText(mRecipeListIngredient.get(position).getIngredient());
        ((MyItemViewHolder) holder).measure.setText(mRecipeListIngredient.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        if(mRecipeListIngredient==null||mRecipeListIngredient.size()==0){
            return -1;
        }
        return mRecipeListIngredient.size();
    }
    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        TextView quantity;
        TextView measure;
        TextView ingredient;
        public MyItemViewHolder(View rootview) {
            super(rootview);
            quantity = (TextView)rootview.findViewById(R.id.detail_quantity);
            measure = (TextView)rootview.findViewById(R.id.detail_measures);
            ingredient = (TextView)rootview.findViewById(R.id.detail_ingredient);
        }
    }
}
