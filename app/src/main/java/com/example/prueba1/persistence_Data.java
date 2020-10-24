package com.example.prueba1;

import com.google.firebase.database.FirebaseDatabase;

public class persistence_Data extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}
