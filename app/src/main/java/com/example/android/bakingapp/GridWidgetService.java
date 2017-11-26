package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import static com.example.android.bakingapp.BakingAppWidgetProvider.ingredientsList;
/**
 * Created by Amardeep on 10/30/2017.
 */

public class GridWidgetService extends RemoteViewsService {
    ArrayList<RecipeIngredient> ingredients;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(),intent);
    }
    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext = null;

        public GridRemoteViewsFactory(Context context,Intent intent) {
            mContext = context;

        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            ingredients = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
             if(ingredients==null||ingredients.size()==0){
                 return -1;
             }
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_view_item);

            views.setTextViewText(R.id.widget_grid_view_item_ingre, ingredients.get(position).getIngredient());
            views.setTextViewText(R.id.widget_grid_view_item_measure,ingredients.get(position).getMeasure());
            views.setTextViewText(R.id.widget_grid_view_item_quantity,String.valueOf(ingredients.get(position).getQuantity()));
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_grid_view_item, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }




    }
}
