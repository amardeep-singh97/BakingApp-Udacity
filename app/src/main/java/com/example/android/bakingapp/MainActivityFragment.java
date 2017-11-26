package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amardeep on 10/22/2017.
 */

public class MainActivityFragment extends Fragment{
    @Nullable private SimpleIdlingResource mIdlingResource;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.activity_main_fragment,container,false);
        final RecipeListAdapter mAdapter = new RecipeListAdapter(new RecipeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecipeList d) {
                final Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("key",d.getName());
                Bundle selectedRecipeBundle = new Bundle();
                ArrayList<RecipeList> selectedRecipe = new ArrayList<>();
                selectedRecipe.add(d);
                selectedRecipeBundle.putParcelableArrayList("Selected_Recipes",selectedRecipe);
                intent.putExtras(selectedRecipeBundle);
                startActivity(intent);
            }
        });
        final RecyclerView recyclerView = (RecyclerView)rootview.findViewById(R.id.RecipelistRecyclerView);
        if (isInLandscapeMode(getContext())){
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(mLayoutManager);
        }else {
            LinearLayoutManager mManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mManager);
        }
        recyclerView.setAdapter(mAdapter);
        bakingClient client =  retrofit.requestclient();
        Call<ArrayList<RecipeList>> call = client.getRecipe();
        //noinspection VisibleForTests
        mIdlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        call.enqueue(new Callback<ArrayList<RecipeList>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeList>> call, Response<ArrayList<RecipeList>> response) {
                Integer code = response.code();
                ArrayList<RecipeList> recipeLists = response.body();
                Log.v("MainActivityY::",String.valueOf(recipeLists));
                mAdapter.setData(getContext(),recipeLists);
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeList>> call, Throwable t) {
                Toast.makeText(getContext(),"GALBAAT HAI NI",Toast.LENGTH_SHORT).show();
            }
        });
        return rootview;
    }
    public boolean isInLandscapeMode(Context contex) {
        return (contex.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

}
