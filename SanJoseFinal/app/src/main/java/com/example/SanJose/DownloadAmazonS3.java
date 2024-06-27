package com.example.SanJose;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadAmazonS3 extends AsyncTask {
    private String ACCESS_ID = "";
    private String SECRET_KEY = "";
    private String BUCKET_NAME = "";
    private Context context;
    private String type;

    private AWSCredentials credentials;
    private AmazonS3 conn;

    private int codigo;

    private int numaudio, numvideo, numfoto;

    private List<File> audios = new ArrayList<>();
    private List<File> fotos = new ArrayList<>();
    private List<File> videos = new ArrayList<>();

    private String[] audioNames, fotoNames, videoNames;

    @Override
    protected Object doInBackground(Object[] objects) {
        ///////////////////////////////////////
        credentials = new BasicAWSCredentials(ACCESS_ID, SECRET_KEY);
        conn = new AmazonS3Client(credentials);
        ///////////////////////////////////////////////////////////////////////

        ContextWrapper contextWrapper = new ContextWrapper(context);
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File videoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DCIM);


        if(type == "audio"){
            if(audioNames.length > 0){
                for (int i = 0; i < audioNames.length; i++){
                    try {
                        File file = new File(musicDirectory, audioNames[i]);
                        S3Object s3Object = conn.getObject(new GetObjectRequest(BUCKET_NAME, audioNames[i]));
                        S3ObjectInputStream s3is = s3Object.getObjectContent();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] read_buf = new byte[1024];
                        int read_len = 0;
                        while ((read_len = s3is.read(read_buf)) > 0) {
                            fos.write(read_buf, 0, read_len);
                        }
                        s3is.close();
                        fos.close();
                        audios.add(file);
                    } catch (AmazonServiceException e) {
                        System.err.println(e.getErrorMessage());
                        System.exit(1);
                    } catch (FileNotFoundException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                }
            }
        } if (type == "foto"){
            if(fotoNames.length > 0){
                for (int i = 0; i < fotoNames.length; i++){
                    try {
                        File file = new File(fotoDirectory, fotoNames[i]);
                        S3Object s3Object = conn.getObject(new GetObjectRequest(BUCKET_NAME, fotoNames[i]));
                        S3ObjectInputStream s3is = s3Object.getObjectContent();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] read_buf = new byte[1024];
                        int read_len = 0;
                        while ((read_len = s3is.read(read_buf)) > 0) {
                            fos.write(read_buf, 0, read_len);
                        }
                        s3is.close();
                        fos.close();
                        fotos.add(file);
                    } catch (AmazonServiceException e) {
                        System.err.println(e.getErrorMessage());
                        System.exit(1);
                    } catch (FileNotFoundException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                }
            }
        } if(type == "video"){
            if(videoNames.length > 0){
                for (int i = 0; i < videoNames.length; i++){
                    try {
                        File file = new File(videoDirectory, videoNames[i]);
                        S3Object s3Object = conn.getObject(new GetObjectRequest(BUCKET_NAME, videoNames[i]));
                        S3ObjectInputStream s3is = s3Object.getObjectContent();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] read_buf = new byte[1024];
                        int read_len = 0;
                        while ((read_len = s3is.read(read_buf)) > 0) {
                            fos.write(read_buf, 0, read_len);
                        }
                        s3is.close();
                        fos.close();
                        videos.add(file);
                    } catch (AmazonServiceException e) {
                        System.err.println(e.getErrorMessage());
                        System.exit(1);
                    } catch (FileNotFoundException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                }
            }
        }
        return null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected void setName(int codigo ){
        this.codigo = codigo;
    }

    protected void setType(String type){
        this.type = type;
    }

    public void setNumaudio(int numaudio) {
        this.numaudio = numaudio;
    }

    public void setNumvideo(int numvideo) {
        this.numvideo = numvideo;
    }

    public void setNumfoto(int numfoto) {
        this.numfoto = numfoto;
    }

    public String[] getAudioNames() {
        return audioNames;
    }

    public void setAudioNames(String[] audioNames) {
        this.audioNames = audioNames;
    }

    public String[] getFotoNames() {
        return fotoNames;
    }

    public void setFotoNames(String[] fotoNames) {
        this.fotoNames = fotoNames;
    }

    public String[] getVideoNames() {
        return videoNames;
    }

    public void setVideoNames(String[] videoNames) {
        this.videoNames = videoNames;
    }

    public List<File> getAudios() {
        return audios;
    }

    public void setAudios(List<File> audios) {
        this.audios = audios;
    }

    public List<File> getFotos() {
        return fotos;
    }

    public void setFotos(List<File> fotos) {
        this.fotos = fotos;
    }

    public List<File> getVideos() {
        return videos;
    }

    public void setVideos(List<File> videos) {
        this.videos = videos;
    }

    protected List<File> onPostExecute(String feed) {
        if (feed == "audios"){
            return audios;
        } else if(feed == "fotos"){
            return fotos;
        } else if (feed == "videos"){
            return videos;
        }
        return null;
    }


}
