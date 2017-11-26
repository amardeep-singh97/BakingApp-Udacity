package com.example.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amardeep on 11/4/2017.
 */

public class ExoPlayerActivityFragment extends Fragment {
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ImageView imageView;
    private TextView textView;
    private Button PrevButton;
    private Button NextButton;
    private String url;
    private Long currentPosition;
    private String description;
    private ArrayList<RecipeStep> recipeSteps;
    private int id;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_exo_player_fragment,container,false);
        url = getActivity().getIntent().getExtras().getString("videourl");
        description = getActivity().getIntent().getExtras().getString("description");
        recipeSteps =  getActivity().getIntent().getExtras().getParcelableArrayList("select_step");
        id = getActivity().getIntent().getExtras().getInt("id");
        String thumbnailUrl = getActivity().getIntent().getExtras().getString("thumbnail");
        textView=(TextView)rootview.findViewById(R.id.video_description);
        imageView=(ImageView)rootview.findViewById(R.id.error);
        PrevButton = (Button)rootview.findViewById(R.id.previousStep);
        NextButton = (Button)rootview.findViewById(R.id.nextStep);
        if(savedInstanceState!=null){
            url=savedInstanceState.getString("URL");
            description=savedInstanceState.getString("DESCRIPTION");
            id=savedInstanceState.getInt("ID");
            currentPosition=savedInstanceState.getLong("PLAYER");
        }
        if(TextUtils.isEmpty(url)){
            imageView.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(thumbnailUrl)){
                Uri builturi = Uri.parse(thumbnailUrl).buildUpon().build();
                Picasso.with(getContext())
                        .load(builturi)
                        .error(R.drawable.error)
                        .into(imageView);
            }
            textView.setText(description);
        }else {
            simpleExoPlayerView = (SimpleExoPlayerView)rootview.findViewById(R.id.playerView);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            exoplayer(url,description);
        }

        PrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id>0){
                    id=id-1;
                    url = recipeSteps.get(id).getVideoURL();
                    description = recipeSteps.get(id).getDescription();
                    exoplayer(url,description);
                }else {
                    Toast.makeText(getContext(),"You are already on the First step of the recipe",Toast.LENGTH_SHORT).show();
                }
            }
        });
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id<recipeSteps.size()-1){
                    id=id+1;
                    url = recipeSteps.get(id).getVideoURL();
                    description = recipeSteps.get(id).getDescription();
                    exoplayer(url,description);
                }else {
                    Toast.makeText(getContext(),"You are already on the last step",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootview;
    }
    public void exoplayer(String VideoUrl,String des){
        textView.setText(des);
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
//            if(onSaveInstanceState();)
            if(currentPosition!=null){
                player.seekTo(currentPosition);
            }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("URL",url);
        outState.putString("DESCRIPTION",description);
        outState.putInt("ID",id);
        outState.putLong("PLAYER",player.getCurrentPosition());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player!=null) {
            player.stop();
            player.release();
            player=null;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (player!=null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(player!=null){
            player.setPlayWhenReady(true);
        }
    }
}
