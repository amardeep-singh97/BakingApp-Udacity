package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amardeep on 10/22/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<RecipeList> mRecipeList;
    OnItemClickListener lisn;
    String imageUrl ;

    public interface OnItemClickListener {
        void onItemClick(RecipeList d);
    }

    public RecipeListAdapter(OnItemClickListener listen){
        this.lisn = listen;
    }
    public void setData(Context cont,ArrayList<RecipeList> recipe){
        this.mContext=cont;
        this.mRecipeList=recipe;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recipelist,parent,false);
        viewHolder =new MyItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyItemViewHolder) holder).mTextView.setText(mRecipeList.get(position).getName());
        ((MyItemViewHolder) holder).bind(mRecipeList.get(position),lisn);
        imageUrl = mRecipeList.get(position).getImage();

        if(!TextUtils.isEmpty(imageUrl)){
            Uri builuri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builuri).into(((MyItemViewHolder) holder).mImageView);
        }else {
            if(position==0){
                ((MyItemViewHolder) holder).mImageView.setImageResource(R.drawable.nutella);
            }
            if(position==1){
                ((MyItemViewHolder) holder).mImageView.setImageResource(R.drawable.brownie);
            }
            if(position==2){
                ((MyItemViewHolder) holder).mImageView.setImageResource(R.drawable.cake);
            }
            if(position==3){
                ((MyItemViewHolder) holder).mImageView.setImageResource(R.drawable.cheese);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mRecipeList==null||mRecipeList.size()==0){
            return -1;
        }
        return mRecipeList.size();
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        public MyItemViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.title);
            mImageView = (ImageView)v.findViewById(R.id.recipeImage);
        }
        public void bind(final RecipeList d, final OnItemClickListener listener){
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(d);
                }
            });
        }
    }
}
