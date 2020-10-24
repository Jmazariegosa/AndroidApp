package com.example.prueba1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.prueba1.Models.Persona;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText nombreP, apellidoP, correoP;
    ListView listV_personas;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Persona personaSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nombreP = findViewById(R.id.nombrePersona);
        apellidoP = findViewById(R.id.apellidoPersona);
        correoP = findViewById(R.id.correoPersona);

        listV_personas = findViewById(R.id.listPersona);

       iniciarDBfireBase();

       listarDatos();

       //funcion que al clickear un registro sus datos se colocan en los EditText
       listV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               personaSelected = (Persona) parent.getItemAtPosition(position);
               nombreP.setText(personaSelected.getNombre());
               apellidoP.setText(personaSelected.getApellido());
               correoP.setText(personaSelected.getCorreo());

           }
       });

       mAuth = FirebaseAuth.getInstance();



    }

    private void listarDatos() {

        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listPerson.clear();
                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){

                    Persona p = objSnapshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity2.this, android.R.layout.simple_list_item_1, listPerson);
                    listV_personas.setAdapter(arrayAdapterPersona);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void iniciarDBfireBase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombre = nombreP.getText().toString();
        String apellido = apellidoP.getText().toString();
        String correo = correoP.getText().toString();

        switch (item.getItemId()){

            case R.id.icon_add:
                if(nombre.equals("")||apellido.equals("")||correo.equals("")){
                    validacion();

                }else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(apellido);
                    p.setCorreo(correo);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this,"Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCajas();
                    }break;

            case R.id.icon_save:
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nombreP.getText().toString().trim());
                p.setApellido(apellidoP.getText().toString().trim());
                p.setCorreo(correoP.getText().toString().trim());
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this,"Guardado", Toast.LENGTH_SHORT).show();
                limpiarCajas();
                break;

            case R.id.icon_delete:
                Persona d = new Persona();
                d.setUid(personaSelected.getUid());
                databaseReference.child("Persona").child(d.getUid()).removeValue();
                Toast.makeText(this,"Eliminado", Toast.LENGTH_SHORT).show();
                limpiarCajas();
                break;

            case R.id.icon_signout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                finish();
                Toast.makeText(this,"Vuelva pronto", Toast.LENGTH_SHORT).show();
                break;

            default:break;
        }





        return true;
    }

    private void limpiarCajas() {

        nombreP.setText("");
        apellidoP.setText("");
        correoP.setText("");

    }

    private void validacion() {

        String nombre = nombreP.getText().toString();
        String apellido = apellidoP.getText().toString();
        String correo = correoP.getText().toString();

        if (nombre.equals("")){

            nombreP.setError("Obligatorio");

        } else if(apellido.equals("")){

            apellidoP.setError("Obligatorio");

        } else if(correo.equals("")){

            correoP.setError("Obligatorio");

        }

    }
}