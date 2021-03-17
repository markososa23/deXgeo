package com.marcosoft.dexgeo.actividades.conductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.actividades.cliente.MapaDeCliente;
import com.marcosoft.dexgeo.actividades.cliente.Registrar;
import com.marcosoft.dexgeo.includes.miToolbar;
import com.marcosoft.dexgeo.providers.AuthProvider;
import com.marcosoft.dexgeo.modelos.Conductor;
import com.marcosoft.dexgeo.providers.ConductorProvider;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class RegistrarConductorActividad extends AppCompatActivity {
   AuthProvider mAuthProvider;
   ConductorProvider mconductorProvider;

    //   SharedPreferences mPref;
    Button btnregistrarConductor;
    TextInputEditText txtNombreCompleto,txtCorreoR,txtDni;
    AlertDialog mDialog;
    private TextInputEditText txtClaveR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_registrar_conductor_actividad);

        //TOOLBAR DE CLASE miToolbar
        miToolbar.show(this,"Registrar Conductor",true);

        //   mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        btnregistrarConductor = findViewById(R.id.btnRegistrarConductor);
        txtClaveR = findViewById(R.id.txtClaveRegistro);
        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtCorreoR = findViewById(R.id.txtCorreoR);
       // txtMarca = findViewById(R.id.txtMarca);
        txtDni= findViewById(R.id.txtDni);

          mDialog = new SpotsDialog.Builder().setContext(com.marcosoft.dexgeo.actividades.conductor.RegistrarConductorActividad.this).setMessage("Espere un momento").build();
          mAuthProvider = new AuthProvider();
          mconductorProvider= new ConductorProvider();
    }


            public void clickRegistrarConductor(View v){
                final String nombre = Objects.requireNonNull(txtNombreCompleto.getText()).toString();
                final String correo = Objects.requireNonNull(txtCorreoR.getText()).toString();
                final String dni = Objects.requireNonNull(txtDni.getText()).toString();
                String clave = Objects.requireNonNull(txtClaveR.getText()).toString();


                if (!nombre.isEmpty()&& !correo.isEmpty() && !clave.isEmpty()&& !dni.isEmpty()){
                    if (clave.length()>= 6){
                        mDialog.show();
                        registrarConductor(nombre,correo,dni,clave);
                    }else{
                        Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                    }
                }else{Toast.makeText(this,"Complete todos los campos ",Toast.LENGTH_SHORT).show();}
            }

            private void registrarConductor(final String nombre, final String correo, final String dni, String clave) {
                mAuthProvider.registrar(correo,clave).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mDialog.hide();
                                if (task.isSuccessful()) {
                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Conductor conductor = new Conductor(id,nombre,correo,dni);
                                    crearUsuario(conductor);
                                } else {
                                    Toast.makeText(com.marcosoft.dexgeo.actividades.conductor.RegistrarConductorActividad.this, "ERROR DE INSERCION ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }

            public void crearUsuario(Conductor conductor){
                mconductorProvider.crear(conductor).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //Toast.makeText(com.marcosoft.dexgeo.actividades.conductor.RegistrarConductorActividad.this,"El registro se realizo correctamente",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrarConductorActividad.this, MapaDeConductor.class);
                            //No podrá volver atras;
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(com.marcosoft.dexgeo.actividades.conductor.RegistrarConductorActividad.this,"No se puede crear",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
}