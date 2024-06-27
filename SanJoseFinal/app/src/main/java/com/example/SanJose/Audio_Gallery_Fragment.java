package com.example.SanJose;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.SanJose.Adapters.AudioGalleryAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Audio_Gallery_Fragment extends Fragment {

    // InteriorViewModel mViewModel;
    private AudioGalleryAdapter agAdapter;
    private RecyclerView audioRecycle;

    private List<File> audios = new ArrayList<File>();

    private List<String> audioPaths = new ArrayList<String>();
    private Context context;

    private Button anteriorBtn;

    public static Audio_Gallery_Fragment newInstance() {
        return new Audio_Gallery_Fragment();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inc__audio__gallery_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_Galeria_Audio);

        audios = (List<File>) getArguments().getSerializable("audios");
        audioRecycle = getView().findViewById(R.id.inc_audio_gallery);

        for (File audio: audios) {
            audioPaths.add(audio.getPath());
        }

        agAdapter = new AudioGalleryAdapter(audioPaths, getChildFragmentManager());
        RecyclerView.LayoutManager lManager =  new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        audioRecycle.setLayoutManager(lManager);
        audioRecycle.setItemAnimator(new DefaultItemAnimator());
        audioRecycle.setAdapter(agAdapter);

        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }
}