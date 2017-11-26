package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    @Nullable
    SimpleIdlingResource mIdlingResource;
    ArrayList<RecipeList> recipeLists;
    String recipeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        recipeName = getIntent().getExtras().getString("key");
        recipeLists= new ArrayList<>();
        recipeLists = getIntent().getExtras().getParcelableArrayList("Selected_Recipes");
        Toolbar mytoolbar =(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(recipeName);
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
           Snackbar snackbar = Snackbar.make(findViewById(R.id.detail_linear_layout),"LandscapeMode",Snackbar.LENGTH_SHORT);
            View snackview = snackbar.getView();
            snackview.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }

        getIdlingResource();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

@VisibleForTesting
@NonNull
public IdlingResource getIdlingResource() {
    if (mIdlingResource == null) {
        mIdlingResource = new SimpleIdlingResource();
    }
    return mIdlingResource;
}
}
