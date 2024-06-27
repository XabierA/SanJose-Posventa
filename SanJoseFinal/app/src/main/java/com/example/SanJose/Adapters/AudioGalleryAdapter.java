package com.example.SanJose.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SanJose.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioGalleryAdapter extends RecyclerView.Adapter<AudioGalleryAdapter.MyViewHolder> {

    private List<String> audios = new ArrayList<>();
    private Context context;
    private MediaPlayer mediaPlayer;

    private FragmentManager fManager;
    private Bundle bundle = new Bundle();

    private final Handler handler = new Handler();

    public AudioGalleryAdapter(List<String> audios, FragmentManager fm) {
        this.audios = audios;
        fManager = fm;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton audioBtn;
        private final SeekBar seekBar;

        public MyViewHolder(final View view) {
            super(view);
            audioBtn = view.findViewById(R.id.audio_galleryBtn);
            seekBar = view.findViewById(R.id.audioSeekBar);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.audio_gallery_item, parent, false);
        return new MyViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("TTTTT", getItemCount()+"");

        holder.audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("imagenes", audios.get(position).toString());
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(audios.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    holder.seekBar.setProgress(0);
                    holder.seekBar.setMax(mediaPlayer.getDuration());
                    startPlayProgressUpdater(holder.seekBar,holder.audioBtn);
                    Toast.makeText(context, "Reproduciondo audio", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        holder.seekBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    public void startPlayProgressUpdater(final SeekBar seek, final ImageButton start) {
        seek.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater(seek,start);
                }
            };
            handler.postDelayed(notification, 1000);
        } else {
            mediaPlayer.pause();
            seek.setProgress(0);
        }
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }
}
