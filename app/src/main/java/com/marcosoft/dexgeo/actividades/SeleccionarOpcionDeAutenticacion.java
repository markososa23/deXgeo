package com.marcosoft.dexgeo.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.actividades.cliente.Registrar;
import com.marcosoft.dexgeo.actividades.conductor.RegistrarConductorActividad;
import com.marcosoft.dexgeo.includes.miToolbar;


public class SeleccionarOpcionDeAutenticacion extends AppCompatActivity {

    Toolbar mTolbar;
    Button btnIrALogin,btnIrARegistro;
    SharedPreferences mPref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_opcion_de_autenticacion);
        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        ///TOOLBAR DE CLASE miToolbar
        miToolbar.show(this,"Seleccionar opcion de Autenticacion",true);
         btnIrALogin = findViewById(R.id.btnIrALogin);
         btnIrARegistro = findViewById(R.id.btnIrARegistro);
    }

    public void irALogin (View view){
        Intent intent = new Intent (SeleccionarOpcionDeAutenticacion.this,LoginActivity.class);
        startActivity(intent);
    }
    public void irARegistro (View view){
        String typeUser = mPref.getString("usuario","");
        if (typeUser.equals("Cliente")){
            Intent intent = new Intent(SeleccionarOpcionDeAutenticacion.this,Registrar.class);
            startActivity(intent);
        }
        if (typeUser.equals("Conductor")){
            Intent intent = new Intent(SeleccionarOpcionDeAutenticacion.this, RegistrarConductorActividad.class);
            startActivity(intent);
        }
       // Intent intent = new Intent (SeleccionarOpcionDeAutenticacion.this, Registrar.class);
       // startActivity(intent);
    }
}