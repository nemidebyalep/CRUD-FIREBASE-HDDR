package com.example.crud_hddr_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.crud_hddr_firebase.Adaptadores.ListViewPersonasAdapter;
import com.example.crud_hddr_firebase.Models.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Persona> listPersonas = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;
    ListViewPersonasAdapter listViewPersonasAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewPersonas;

    EditText inputNombre, inputTelefono, inputDni, inputCorreo, inputEstado_civil;
    Button btnCancelar;

    Persona personaSeleccionada;


    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputNombre = findViewById(R.id.inputNombre);
        inputDni = findViewById(R.id.inputDni);
        inputCorreo = findViewById(R.id.inputCorreo);
        inputEstado_civil = findViewById(R.id.inputEstado_civil);
        inputTelefono = findViewById(R.id.inputTelefono);
        btnCancelar = findViewById(R.id.btnCANCELAR);

        listViewPersonas = findViewById(R.id.listViewPersonas);
        linearLayoutEditar = findViewById(R.id.linerLayoudtEditar);


        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                personaSeleccionada = (Persona) parent.getItemAtPosition(position);
                inputNombre.setText(personaSeleccionada.getNombres());
                inputDni.setText(personaSeleccionada.getDni());
                inputCorreo.setText(personaSeleccionada.getCorreo());
                inputEstado_civil.setText(personaSeleccionada.getEstado_civil());
                inputTelefono.setText(personaSeleccionada.getTelefono());
                //visibilidad de los layout
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutEditar.setVisibility(View.GONE);
                personaSeleccionada = null;
            }
        });

        listarPersonas();
    }

    private void listarPersonas() {
        databaseReference.child("Personas").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPersonas.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Persona p = objSnapshot.getValue(Persona.class);
                    listPersonas.add(p);
                }
                //iniciar adaptador
                listViewPersonasAdapter = new ListViewPersonasAdapter( MainActivity.this, listPersonas);
                listViewPersonas.setAdapter(listViewPersonasAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //este metodo permite que las opciones del menu sean visibles
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // este metodo permite la funcionalidad del crud
        String nombres = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String dni = inputDni.getText().toString();
        String correo = inputCorreo.getText().toString();
        String estado_civil = inputEstado_civil.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_guardarr:
                if (personaSeleccionada != null) {
                    if (validarInputs() == false) {
                        Persona p = new Persona();
                        p.setIdpersona(personaSeleccionada.getIdpersona());
                        p.setNombres(nombres);
                        p.setDni(dni);
                        p.setCorreo(correo);
                        p.setEstado_civil(estado_civil);
                        p.setTelefono(telefono);
                        p.setFecharegistro(personaSeleccionada.getFecharegistro());
                        p.setTimestamp(personaSeleccionada.getTimestamp());
                        databaseReference.child("Personas").child(p.getIdpersona()).setValue(p);
                        Toast.makeText(this, "Actualizado Correctamente", Toast.LENGTH_LONG).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                         personaSeleccionada = null;
                    }
                } else {
                    Toast.makeText(this, "Seleccione una Persona para actualizar", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_eliminar:
                if (personaSeleccionada != null) {
                    Persona p2 = new Persona();
                    p2.setIdpersona(personaSeleccionada.getIdpersona());
                    databaseReference.child("Personas").child(p2.getIdpersona()).removeValue();
                    linearLayoutEditar.setVisibility(View.GONE);
                    personaSeleccionada = null;

                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Selecione Persona a eliminar", Toast.LENGTH_LONG).show();

        }
        }
        return super.onOptionsItemSelected(item);
    }


    public void insertar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder( //con este dialog se puede acceder al formulario de registrar una persona
                MainActivity.this
        );
        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsertar = (Button) mView.findViewById(R.id.btnInsertar);
        final EditText mInputNombres = (EditText) mView.findViewById(R.id.inputNombre);
        final EditText mInputDni = (EditText) mView.findViewById(R.id.inputDni);
        final EditText mInputCorreo = (EditText) mView.findViewById(R.id.inputCorreo);
        final EditText mInputEstado_civil = (EditText) mView.findViewById(R.id.inputEstado_civil);
        final EditText mInputTelefono = (EditText) mView.findViewById(R.id.inputTelefono);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //metodo para la funcionalidad del boton con ciertas validaciones
                String nombres = mInputNombres.getText().toString();
                String dni = mInputDni.getText().toString();
                String correo = mInputCorreo.getText().toString();
                String estado_civil = mInputEstado_civil.getText().toString();
                String telefono = mInputTelefono.getText().toString();
                if (nombres.isEmpty() || nombres.length() <3) {
                    showError(mInputNombres, "Nombre invalido (Min. 3 letras)");
                } else if (telefono.isEmpty() || telefono.length() < 9) {
                    showError(mInputTelefono, "Telefono invalido (Min. 9 números)");
                } else {
                    Persona p = new Persona();
                    p.setIdpersona(UUID.randomUUID().toString());
                    p.setNombres(nombres);
                    p.setDni(dni);
                    p.setCorreo(correo);
                    p.setEstado_civil(estado_civil);
                    p.setTelefono(telefono);
                    p.setFecharegistro(getFechaNormal(getFechaMilisegundos()));
                    p.setTimestamp(getFechaMilisegundos() * -1);
                    databaseReference.child("Personas").child(p.getIdpersona()).setValue(p);
                    Toast.makeText(MainActivity.this, "Registrado Correctamente",
                            Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
    }
    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();
       return tiempounix;

    }
    public String getFechaNormal(long fechamilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = sdf.format(fechamilisegundos);
        return fecha;
    }

    public void showError(EditText input, String s){ //metodo de registro de un error en las opciones que no cumplen la condicion
        input.requestFocus();
        input.setError(s);
    }

    public boolean validarInputs() {
        String nombre = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        if (nombre.isEmpty() || nombre.length() < 3) {
            showError(inputNombre, "Nombre invalido (Min. 3 letras)");
            return  true;
        }else if (telefono.isEmpty() || telefono.length()<9){
            showError(inputTelefono, "Telefono invalido (Min. 9 numeros)");
            return  true;
        }else {
            return false;
        }
    }
}