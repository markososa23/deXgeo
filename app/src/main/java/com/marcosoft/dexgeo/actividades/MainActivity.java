package com.marcosoft.dexgeo.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.actividades.cliente.MapaDeCliente;
import com.marcosoft.dexgeo.actividades.conductor.MapaDeConductor;

public class MainActivity extends AppCompatActivity {

    Button btnCliente,btnConductor;
    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCliente = findViewById(R.id.btnCliente);
        btnConductor = findViewById(R.id.btnConductor);
        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        //REVISAR ESTA LINEA
     //  final SharedPreferences.Editor editor = mPref.edit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            String tipoUsuario = mPref.getString("usuario","");
            if (tipoUsuario.equals("Cliente")){
                Intent intent = new Intent(MainActivity.this, MapaDeCliente.class);
                //no podra volver atras
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else
            {
                Intent intent = new Intent(MainActivity.this, MapaDeConductor.class);
                //no podra volver atras
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    public void irASeleccionarOpcionAut(){
        Intent intent = new Intent(MainActivity.this,SeleccionarOpcionDeAutenticacion.class);
        startActivity(intent);
    }

    public void clickCliente (View v){
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putString("usuario","Cliente");
        editor.apply();
        irASeleccionarOpcionAut();
    }

    public void clickConductor (View v){
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putString("usuario","Conductor");
        editor.apply();
        irASeleccionarOpcionAut();
    }

  /*  private void irASeleccionDeAutenticacion() {
        Intent intent = new Intent(MainActivity.this,SeleccionarOpcionDeAutenticacion.class);
        startActivity(intent);
    }*/
}