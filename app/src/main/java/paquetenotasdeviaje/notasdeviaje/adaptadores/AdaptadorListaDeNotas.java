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

import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.modelos.Nota;

public class AdaptadorListaDeNotas extends ArrayAdapter<Nota> {

    private int layout;

    public AdaptadorListaDeNotas(@NonNull Context context, int layout, @NonNull List<Nota> notas) {
        super(context, layout, notas);

        this.layout = layout;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView titulo = convertView.findViewById(R.id.txt_titulo_plantillalistadenotas);
        TextView descripcion = convertView.findViewById(R.id.txt_descripcion_plantillalistadenotas);

        Nota nota = getItem(position);

        titulo.setText(nota.getTitulo());
        descripcion.setText(nota.getDescripcion());

        return convertView;
    }

}
