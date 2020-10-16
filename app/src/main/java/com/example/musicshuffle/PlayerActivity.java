package com.example.musicshuffle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next,btn_previeous,btn_pause;
    TextView songtextlabel;
    SeekBar songSeekbar;
    static MediaPlayer mediaPlayer;
    int positon;
    ArrayList<File> mysongs;
   Thread updateseekbar;
   String sname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next=findViewById(R.id.nextbtn);
        btn_pause=findViewById(R.id.pause_btn);
        btn_previeous=findViewById(R.id.previeuosbtn);
        songtextlabel=findViewById(R.id.songlabel);
        songSeekbar=findViewById(R.id.seekbar);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        updateseekbar=new Thread(){
            @Override
            public void run() {
                int totalduretaion=mediaPlayer.getDuration();
                int currentposition=0;
                while(currentposition<totalduretaion){
                    try {
                          sleep(500);
                          songSeekbar.setProgress(currentposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        updateseekbar.start();
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysongs=(ArrayList)bundle.getParcelableArrayList("songs");
        sname=mysongs.get(positon).getName().toString();
        String songname=i.getStringExtra("songname");
        songtextlabel.setText(songname);
        songtextlabel.setSelected(true);
        positon=bundle.getInt("pos",0);
        Uri uri=Uri.parse(mysongs.get(positon).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        songSeekbar.setMax(mediaPlayer.getDuration());
        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songSeekbar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.ic_baseline_play_button);
                }else{
                    btn_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                positon=(positon+1)%mysongs.size();
                Uri uri=Uri.parse(mysongs.get(positon).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                sname=mysongs.get(positon).getName().toString();
                songtextlabel.setText(sname);
                mediaPlayer.start();
            }
        });
        btn_previeous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                positon=((positon-1)<0)?(mysongs.size()-1):(positon-1);
                Uri uri=Uri.parse(mysongs.get(positon).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                sname=mysongs.get(positon).getName().toString();
                songtextlabel.setText(sname);
                mediaPlayer.start();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}