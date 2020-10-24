package com.example.prueba1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "prueba1";
    private EditText nEmailField, nPassField;
    private Button nLoginbtn;
    private Button nRegisterUser;
    private TextView nFBTextView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener nAuthListener;

    //Conexion a BDFireBase
    DatabaseReference nDatabaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference nRootChild = nDatabaseReference.child("texto");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //conexion al auth de firebase
        mAuth = FirebaseAuth.getInstance();
        nEmailField = (EditText) findViewById(R.id.IDmail);
        nPassField = (EditText) findViewById(R.id.IDcontraseña);
        nLoginbtn = (Button) findViewById(R.id.btnLogin);
        nRegisterUser = (Button) findViewById(R.id.btnRegisterUser);

        //llama el edit text para el mensaje
        nFBTextView = (TextView) findViewById(R.id.fbTextView);



        nAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                }else{
                   // Toast.makeText(MainActivity.this" Datos Incorrectos", Toast.LENGTH_SHORT).show();
                     }
            }
        };

        //EVENTOS ONCLICK EN LA PANTALLA DE LOGEO
        nRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registroUsuario();

            }
        });

        nLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logearUsuario();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(nAuthListener);

        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);


        nRootChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String texto = dataSnapshot.getValue().toString();
                nFBTextView.setText(texto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //metodo de logeo de usuario
    private void logearUsuario() {

        String email = nEmailField.getText().toString();
        String password = nPassField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Iniciar Sesion " + task.isSuccessful());
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                         if(!task.isSuccessful()){
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Datos incorrectos",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    //metodo de registro de usuarios
    private void registroUsuario() {

        String email = nEmailField.getText().toString();
        String password = nPassField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "Registrar Usuario " + task.isSuccessful());
                        //FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                        if(!task.isSuccessful()){
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Datos incorrectos",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

}