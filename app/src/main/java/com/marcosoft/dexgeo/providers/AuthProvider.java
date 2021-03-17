package com.marcosoft.dexgeo.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    FirebaseAuth mAuth;

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
                }

public Task<AuthResult> registrar(String correo, String clave){
        return mAuth.createUserWithEmailAndPassword(correo,clave);
        }
public Task<AuthResult> autenticar(String correo, String clave){
        return mAuth.signInWithEmailAndPassword(correo,clave);
        }
    public void cerrarSesion(){
         mAuth.signOut();
    }
public String getId(){
        return mAuth.getCurrentUser().getUid();
}
public Boolean existeciaSesion (){
        boolean existencia = false;
    if (mAuth.getCurrentUser() != null){
        existencia = true;
    }
    return existencia;
//return (mAuth.getCurrentUser() != null);
    }
}