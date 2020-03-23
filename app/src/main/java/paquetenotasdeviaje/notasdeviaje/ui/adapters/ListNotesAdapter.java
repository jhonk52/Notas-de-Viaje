package paquetenotasdeviaje.notasdeviaje.ui.adapters;

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
import paquetenotasdeviaje.notasdeviaje.data.Note;

public class ListNotesAdapter extends ArrayAdapter<Note> {

    private int layout;

    public ListNotesAdapter(@NonNull Context context, int layout, @NonNull List<Note> notes) {
        super(context, layout, notes);
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView title = convertView.findViewById(R.id.txt_title_listitem_principalactivity);
        TextView description = convertView.findViewById(R.id.txt_description_listitem_principalactivity);

        Note note = getItem(position);

        title.setText(note.getTitle());
        description.setText(note.getDescription());

        return convertView;
    }

}
