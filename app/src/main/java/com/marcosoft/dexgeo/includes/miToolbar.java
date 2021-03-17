package com.marcosoft.dexgeo.includes;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import com.marcosoft.dexgeo.R;

public class miToolbar {

    public static void show(AppCompatActivity activity,String titulo,boolean mostrarBoton){
        Toolbar toolbar= activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(titulo);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(mostrarBoton);
    }
}
