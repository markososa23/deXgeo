package com.marcosoft.dexgeo.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosoft.dexgeo.modelos.Conductor;

import java.util.HashMap;
import java.util.Map;

public class ConductorProvider {
    DatabaseReference mDatabase;


    public ConductorProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Conductores");
    }
    public Task <Void> crear (Conductor conductor){
        Map<String,Object> map= new HashMap<>();
        map.put("nombre",conductor.getNombre());
        map.put("correo",conductor.getCorreo());
        map.put("dni",conductor.getDni());
        return mDatabase.child(conductor.getId()).setValue(map);
    }

}
