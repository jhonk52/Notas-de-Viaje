package paquetenotasdeviaje.notasdeviaje.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import notasdeviaje.notasdeviaje.modelos.Nota;
import paquetenotasdeviaje.notasdeviaje.R;


public class AdaptadorListaDeNotas extends ArrayAdapter<Nota> {

    public AdaptadorListaDeNotas(@NonNull Context context, int resource, @NonNull List<Nota> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.plantilla_listadenotas,
                    parent,
                    false);
        }

        TextView titulo = convertView.findViewById(R.id.txt_titulo_plantillalistadenotas);
        TextView descripcion = convertView.findViewById(R.id.txt_descripcion_plantillalistadenotas);

        Nota nota = getItem(position);

        titulo.setText(nota.getTitulo());
        descripcion.setText(nota.getDescripcion());

        return convertView;

    }


}