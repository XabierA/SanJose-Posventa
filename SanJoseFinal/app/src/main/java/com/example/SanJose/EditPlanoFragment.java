package com.example.SanJose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.SanJose.databinding.FragmentEditPlanoBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;


public class EditPlanoFragment extends Fragment {
    private Context context;

    private Button señalarPlanoBtn, guardarPlanoBtn, anterior, siguiente;

    public EditPlanoFragment() {
        // Required empty public constructor
    }

    public static EditPlanoFragment newInstance() {
        EditPlanoFragment fragment = new EditPlanoFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context = context;
        //control = (IControlFragmentos) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_plano, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        señalarPlanoBtn = getView().findViewById(R.id.btnSeñalarPlano);
        guardarPlanoBtn = getView().findViewById(R.id.btnGuardarPlano);
        anterior = getView().findViewById(R.id.btnAnterior_EditP);
        siguiente = getView().findViewById(R.id.btnSiguiente_EditP);

        guardarPlanoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        señalarPlanoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }

    public static void request (SurfaceView source,
                                Bitmap dest,
                                PixelCopy.OnPixelCopyFinishedListener listener,
                                Handler listenerThread){
        Toast.makeText(source.getContext(), "llega request", Toast.LENGTH_LONG).show();
    }

    private void getScreen(){
        View v = getView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        String extr = Environment.getExternalStorageDirectory().toString();
        File myPath = new File(extr, 1+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage( getActivity().getContentResolver(), b, "Screen", "screen");
            Toast.makeText(context, "Funciona", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        Toast.makeText(view.getContext(), "screenshot", Toast.LENGTH_LONG).show();
        return bitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Toast.makeText(context, storageDir+"", Toast.LENGTH_LONG).show();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public void store(Bitmap bm, String fileName){
        String dirPath = getRecordedFilePath();
        Toast.makeText(context, dirPath, Toast.LENGTH_LONG).show();
        File dir = new File(dirPath);
        /*if(!dir.exists())
            dir.mkdirs();*/
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        File file = new File(dirPath, fileName+n+".png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(context, "entra try", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private String getRecordedFilePath(){;
        ContextWrapper contextWrapper = new ContextWrapper(context);
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        File screenshotDirecotry = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(screenshotDirecotry, n+".jpeg");
        return screenshotDirecotry.toString();
        //return  file.getPath();
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(getRecordedFilePath(), "test1.png");

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "llega", Toast.LENGTH_LONG).show();
            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void getScreenBitmap() {
        try {
            File imageFile = new File(getRecordedFilePath(), "test1.jpg");
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(imageFile);
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.write((imageFile.toString()).getBytes("ASCII"));
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "try", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}