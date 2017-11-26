package com.example.android.bakingapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amardeep on 10/29/2017.
 */

public class WidgetService extends IntentService {
private static final String WidgetIntentAction = "com.example.android.BakingApp.action.UpdateWidgetService";
    public WidgetService() {
        super("WidgetService");
    }
    public static void startWidgetService(Context context, List<RecipeIngredient> recipeIngredients){
        Intent intent = new Intent(context,WidgetService.class);
        intent.setAction(WidgetIntentAction);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("recipe", (ArrayList<? extends Parcelable>) recipeIngredients);
        intent.putExtras(bundle);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       if(intent!=null){
           final String action = intent.getAction();
           ArrayList<RecipeIngredient> recipeIngredientArrayList =  intent.getExtras().getParcelableArrayList("recipe");
           if(WidgetIntentAction.equals(action)){
               handleAction(recipeIngredientArrayList);
           }
       }
    }
    private void handleAction(ArrayList<RecipeIngredient> rcpe){
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        Bundle ingrebundle = new Bundle();
        ingrebundle.putParcelableArrayList("ingre",rcpe);
        intent.putExtras(ingrebundle);
        sendBroadcast(intent);
    }
}
