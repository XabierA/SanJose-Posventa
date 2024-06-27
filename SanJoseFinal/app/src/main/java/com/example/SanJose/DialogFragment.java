package com.example.SanJose;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.SanJose.FragmentCallBack.CallBackIncidencia;


public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private Context context;

    private CallBackIncidencia callback;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context = context;
    }

    public static DialogFragment newInstance() {
        DialogFragment fragment = new DialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Esta conforme con esta incidencia?")
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ElegirGruposEmailFragment f = new ElegirGruposEmailFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("plano").commit();

                    }
                })
                .setNegativeButton("Saltar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fm = getParentFragmentManager();
                        for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
                            if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase("main")) {
                                fm.popBackStack();
                            }
                            else
                            {
                                break;
                            }
                        }
                        getParentFragmentManager().popBackStackImmediate();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    protected void sendEmail() {
        String[] TO = {"xabier.angulo@bexreal.com"};
        String[] CC = {"testemailsenderapp@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Se a creado una nueva incidencia, accede al portal web para revisarla");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFragmentCallback(CallBackIncidencia callback) {
        this.callback = callback;
    }
}

