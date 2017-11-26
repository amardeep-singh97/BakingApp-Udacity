package com.example.android.bakingapp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Amardeep on 10/22/2017.
 */

public interface bakingClient{
    @GET("baking.json")
    Call<ArrayList<RecipeList>> getRecipe();
}