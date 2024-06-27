package com.example.SanJose;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadAmazonS3 extends AsyncTask {
    private String ACCESS_ID = "AKIA55CI3RZ6URIWIJET";
    private String SECRET_KEY = "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";
    private String BUCKET_NAME = "mediafiles-csj";

    private String type;

    private String formato;

    private AWSCredentials credentials;
    private AmazonS3 conn;

    private int codigo;

    private int numaudio, numvideo, numfoto;

    @Override
    protected Object doInBackground(Object[] objects) {
        credentials = new BasicAWSCredentials(ACCESS_ID, SECRET_KEY);
        conn = new AmazonS3Client(credentials);
        if(type == "audio"){
            List<File> archivos = (List<File>) objects[0];
            if (archivos.size() > 0){
                if (numaudio > 0){
                    for (int i = 0; i < archivos.size(); i++){
                        int num = i+1+numaudio;
                        if (formato == "inspeccion"){
                            conn.putObject(BUCKET_NAME, "audio-INS"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }else {
                            conn.putObject(BUCKET_NAME, "audio"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i < archivos.size(); i++){
                        int num = i+1;
                        if (formato == "inspeccion"){
                            conn.putObject(BUCKET_NAME, "audio-INS"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }else {
                            conn.putObject(BUCKET_NAME, "audio"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }
                    }
                }
            }
        } else if (type == "foto") {
            List<File> archivos = (List<File>) objects[0];
            if (archivos.size() > 0){
                if (numfoto > 0){
                    for (int i = 0; i < archivos.size(); i++){
                        int num = i+1+numfoto;
                        Log.v("fotosubida", codigo+"-"+num);
                        if(formato == "inspeccion"){
                            Log.v("entra", "entra");
                            conn.putObject(BUCKET_NAME, "foto-INS"+codigo+"-"+num+".jpg", (File) archivos.get(i));
                        }else {
                            conn.putObject(BUCKET_NAME, "foto"+codigo+"-"+num+".jpg", (File) archivos.get(i));
                        }
                    }
                }else {
                    for (int i = 0; i < archivos.size(); i++){
                        int num = i+1;
                        if(formato == "inspeccion"){
                            Log.v("entra", "entra");
                            conn.putObject(BUCKET_NAME, "foto-INS"+codigo+"-"+num+".jpg", (File) archivos.get(i));
                        }else {
                            conn.putObject(BUCKET_NAME, "foto"+codigo+"-"+num+".jpg", (File) archivos.get(i));
                        }
                    }
                }
            }
        } else if (type == "video") {
            List<File> archivos = (List<File>) objects[0];
            if (archivos.size() > 0){
                if (numvideo > 0){
                    for (int i = 0; i < archivos.size(); i++){
                        int num = i+1+numvideo;
                        if (formato == "inspeccion"){
                            conn.putObject(BUCKET_NAME, "video-INS"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }
                        conn.putObject(BUCKET_NAME, "video"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                    }
                }else {
                    for (int i = 0; i < archivos.size(); i++){
                        int num = i+1;
                        if (formato == "inspeccion"){
                            conn.putObject(BUCKET_NAME, "video-INS"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }else{
                            conn.putObject(BUCKET_NAME, "video"+codigo+"-"+num+".mp4", (File) archivos.get(i));
                        }
                    }
                }
            }
        }
        return null;
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

    public void setFormato(String formato) {
        this.formato = formato;
    }

    protected void onPostExecute(String feed) {

        return ;
    }


}
