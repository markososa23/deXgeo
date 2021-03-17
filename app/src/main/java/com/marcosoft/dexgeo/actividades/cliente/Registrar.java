package com.marcosoft.dexgeo.actividades.cliente;
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
import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.includes.miToolbar;
import com.marcosoft.dexgeo.modelos.Cliente;
import com.marcosoft.dexgeo.providers.AuthProvider;
import com.marcosoft.dexgeo.providers.ClienteProvider;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class Registrar extends AppCompatActivity {
    AuthProvider mAuthProvider;
    ClienteProvider mClienteProvider;

 //   SharedPreferences mPref;
    Button btnRegistrar;
    TextInputEditText txtNombreCompleto,txtCorreoR;
    AlertDialog mDialog;
    private TextInputEditText txtClaveR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        ///TOOLBAR DE CLASE miToolbar
        miToolbar.show(this,"Registrar Cliente",true);

     //   mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtClaveR = findViewById(R.id.txtClaveRegistro);
        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtCorreoR = findViewById(R.id.txtCorreoR);
        mDialog = new SpotsDialog.Builder().setContext(Registrar.this).setMessage("Espere un momento").build();
        mAuthProvider = new AuthProvider();
        mClienteProvider= new ClienteProvider();
    }



    public void clickRegistrar(View v){
        final String nombre = Objects.requireNonNull(txtNombreCompleto.getText()).toString();
        final String correo = Objects.requireNonNull(txtCorreoR.getText()).toString();
        String clave = Objects.requireNonNull(txtClaveR.getText()).toString();


        if (!nombre.isEmpty()&& !correo.isEmpty() && !clave.isEmpty()){
            if (clave.length()>= 6){
                mDialog.show();
                registrar(nombre,correo,clave);
            }else{
                Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            }
        }else{Toast.makeText(this,"Complete todos los campos ",Toast.LENGTH_SHORT).show();}
    }

    private void registrar(final String nombre,final String correo,String clave) {
        mAuthProvider.registrar(correo,clave).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.hide();
                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Cliente cliente = new Cliente(id,nombre,correo);
                            crearUsuario(cliente);
                        } else {
                            Toast.makeText(Registrar.this, "ERROR DE INSERCION ", Toast.LENGTH_SHORT).show();
                        }
                    }
                                                                                 }
        );
    }

    public void crearUsuario(Cliente cliente){
        mClienteProvider.crear(cliente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(Registrar.this,"El registro se realizo correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registrar.this, MapaDeCliente.class);
                    //no podra volver atras
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Registrar.this,"No se puede crear",Toast.LENGTH_SHORT).show();
                }

            }
        });
    } 

   /* private void guardarUsuario(String id,String nombre,String email) {
        String selectedUser = mPref.getString("usuario","");
        Usuario usuario = new Usuario();
        usuario.setCorreo(email);
        usuario.setNombre(nombre);
       // Toast.makeText(this,"El valor que selecciono fue "+ selectedUser,Toast.LENGTH_SHORT).show();
        if (selectedUser.equals("Conductor")){
            mDatabase.child("Usuario").child("Conductor").child(id).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Registrar.this,"Carga exitosa ",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Registrar.this,"ERROR EN LA CARGA ",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if (selectedUser.equals("Cliente")){
            mDatabase.child("Usuario").child("Cliente").child(id).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Registrar.this,"Carga exitosa ",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Registrar.this,"ERROR EN LA CARGA ",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/
}