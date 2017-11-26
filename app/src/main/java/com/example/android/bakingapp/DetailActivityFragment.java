package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amardeep on 11/4/2017.
 */

public class DetailActivityFragment extends Fragment {
    ArrayList<RecipeList> recipeLists;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Paint p = new Paint();
    String recipeName;
    private int ID;
    RecyclerView recyclerView1;
    RecipeStepAdapter mAdapterStep;
    TextView mDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.activity_detail_fragment,container,false);
        recipeName = getActivity().getIntent().getExtras().getString("key");
        recipeLists= new ArrayList<>();
        recipeLists = getActivity().getIntent().getExtras().getParcelableArrayList("Selected_Recipes");
        RecyclerView recyclerView = (RecyclerView)rootview.findViewById(R.id.detail_recyclerview_one);
        final RecipeIngredientAdapter mAdapter = new RecipeIngredientAdapter(getContext());
        LinearLayoutManager mManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setData(recipeLists);
        List<RecipeIngredient> ingre = recipeLists.get(0).getIngredients();
        WidgetService.startWidgetService(getContext(),ingre);
        recyclerView1 = (RecyclerView)rootview.findViewById(R.id.detail_recyclerview_two);
        mAdapterStep = new RecipeStepAdapter(getContext(), new RecipeStepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecipeStep d) {
                if(ID==1){
                    launchExoPlayer(d.getVideoURL(),d.getDescription());
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(rootview.findViewById(R.id.main_content).getTag().equals("Tablet-land")){
                mDescription= (TextView)rootview.findViewById(R.id.video_description);
                simpleExoPlayerView = (SimpleExoPlayerView)rootview.findViewById(R.id.playerView);
                tabletmode();
                ID=1;
            }
        }else {
            initview();
            ID=0;
        }
        return rootview;
    }
    public void tabletmode(){
        mAdapterStep.setData(recipeLists);
        recyclerView1.setAdapter(mAdapterStep);
    }
    public void launchExoPlayer(String VideoUrl,String des){
        mDescription.setText(des);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        if (!VideoUrl.isEmpty()) {
            initializePlayer(Uri.parse(VideoUrl));
        }
    }
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }else {
            player.stop();
            player.release();
            player=null;
            if(String.valueOf(mediaUri)!=""){
                initializePlayer(mediaUri);
            }
        }
    }
    public void initview(){
        mAdapterStep.setData(recipeLists);
        recyclerView1.setAdapter(mAdapterStep);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT){
                    Intent intent = new Intent(getContext(),ExoPlayerActivity.class);
                    ArrayList<RecipeStep> steps = (ArrayList<RecipeStep>) recipeLists.get(0).getSteps();
                    Bundle selectedStepsBundle = new Bundle();
                    selectedStepsBundle.putParcelableArrayList("select_step",steps);
                    String url = steps.get(position).getVideoURL();
                    String description = steps.get(position).getDescription();
                    Log.v("POSITIONandID",String.valueOf(position)+" "+String.valueOf(steps.get(position).getId()));
                    Log.v("URLLLLLL::",String.valueOf(url));
                    intent.putExtra("name",recipeName);
                    intent.putExtra("videourl",url);
                    intent.putExtra("description",description);
                    intent.putExtra("thumbnail",steps.get(position).getThumbnailURL());
                    intent.putExtra("id",steps.get(position).getId());
                    intent.putExtras(selectedStepsBundle);
                    startActivity(intent);
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if(dX < 0) {
                        p.setColor(Color.parseColor("#90CAF9"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_arrow_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView1);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            initview();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(player!=null){
            player.stop();
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player!=null){
            player.stop();
            player.release();
        }
    }
}
