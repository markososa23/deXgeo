package com.marcosoft.dexgeo.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.actividades.cliente.MapaDeCliente;
import com.marcosoft.dexgeo.actividades.cliente.OpcionesDeVecino;
import com.marcosoft.dexgeo.actividades.cliente.Registrar;
import com.marcosoft.dexgeo.actividades.conductor.MapaDeConductor;
import com.marcosoft.dexgeo.includes.miToolbar;
import com.marcosoft.dexgeo.providers.AuthProvider;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText txtEmail,txtClave;
    Button btnIngresar;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    AlertDialog mDialog;
    SharedPreferences mPref;
    AuthProvider mAuthProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.txtCorreo);
        txtClave = findViewById(R.id.txtClave);
        btnIngresar = findViewById(R.id.btnLgin);
        miToolbar.show(this,"Login",true);
        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mAuthProvider = new AuthProvider();
        //FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
         mDatabase = FirebaseDatabase.getInstance().getReference();
        mDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).setMessage("Espere un momento").build();
    }


////METODO PARA AUTENTICACION////
    public void login(View v){
        String email = txtEmail.getText().toString();
        String clave = txtClave.getText().toString();

        if (!email.isEmpty() && !clave.isEmpty()){
            if (clave.length() >=6 ){
                mDialog.show();
              //  mAuthProvider.autenticar(email,clave).a
                mAuthProvider.autenticar(email,clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String tipousuario = mPref.getString("usuario","");
                            if (tipousuario.equals("Cliente")){
                                Intent intent = new Intent(LoginActivity.this, MapaDeCliente.class);
                                //no podra volver atras
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else
                            {
                                Intent intent = new Intent(LoginActivity.this, MapaDeConductor.class);
                                //no podra volver atras
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(LoginActivity.this,"EMAIL O CLAVE INCORRECTOS",Toast.LENGTH_SHORT).show();
                        } mDialog.dismiss();
                    }
                });
            }else{
                Toast.makeText(LoginActivity.this,"LA CONTRASEÑA DEBE TENER MAS DE 6 CARACTERES",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this,"EMAIL Y CLAVE OBLIGATORIOS",Toast.LENGTH_SHORT).show();
        }
    }
}