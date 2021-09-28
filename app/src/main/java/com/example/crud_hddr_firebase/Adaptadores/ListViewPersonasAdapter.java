package com.example.crud_hddr_firebase.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.crud_hddr_firebase.Models.Persona;
import com.example.crud_hddr_firebase.R;


import java.util.ArrayList;

public class ListViewPersonasAdapter extends BaseAdapter {
    Context context;
    ArrayList<Persona> personaData;
    LayoutInflater layoutInflater;
    Persona personaModel;

    public ListViewPersonasAdapter(Context context, ArrayList<Persona> personaDate){
        this.context = context;
        this.personaData = personaDate;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() { return personaData.size();  }

    @Override
    public Object getItem(int position) {
        return personaData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;  //rowView permite imprimir los valores del objeto
        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.lista_personas, null, true); // layoutinflater, esta clase permite instanciar los archivos xml

            }
        //enlazar vistas
        TextView nombres = rowView.findViewById(R.id.nombres);
        TextView dni = rowView.findViewById(R.id.dni);
        TextView correo = rowView.findViewById(R.id.correo);
        TextView estado_civil = rowView.findViewById(R.id.estado_civil);
        TextView telefono = rowView.findViewById(R.id.telefonos);
        TextView fecharegistro = rowView.findViewById(R.id.fecharegistro);

        personaModel = personaData.get(position);
        nombres.setText(personaModel.getNombres());//trae datos de la base
        dni.setText(personaModel.getDni());
        correo.setText(personaModel.getCorreo());
        estado_civil.setText(personaModel.getEstado_civil());
        telefono.setText(personaModel.getTelefono());
        fecharegistro.setText(personaModel.getFecharegistro());
        return rowView;

    }
}