package com.example.SanJose.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.SanJose.R;
import com.example.SanJose.VideoView_Fragment;
import com.example.SanJose.dialog.DialogoFragment;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.MyViewHolder> {
    private List<Serializable> galeria = new ArrayList<>();
    private Context context;
    private DialogoFragment dialog;
    private FragmentManager fManager;
    private Bundle bundle = new Bundle();

    private String type;

    public GaleriaAdapter(List<Serializable> galeria, FragmentManager fm, String type) {
        this.galeria = galeria;
        fManager = fm;
        this.type = type;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagenGaleria;

        public MyViewHolder(final View view) {
            super(view);
            imagenGaleria = view.findViewById(R.id.imagenGaleria);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_galeria, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(galeria.get(position)).into(holder.imagenGaleria);
        holder.imagenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == "imagenes"){
                    bundle.putString("imagenes", galeria.get(position).toString());
                    dialog = new DialogoFragment(bundle);
                    dialog.show(fManager,"a");
                }else {
                    Bundle result = new Bundle();
                    File video = (File) galeria.get(position);
                    result.putString("video", video.getPath());
                    VideoView_Fragment videoView_fragment = VideoView_Fragment.newInstance();
                    videoView_fragment.setArguments(result);
                    fManager.beginTransaction().replace(R.id.nav_container, videoView_fragment).addToBackStack("galeria").commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return galeria.size();
    }
}
