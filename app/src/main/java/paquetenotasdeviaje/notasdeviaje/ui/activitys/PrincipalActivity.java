package paquetenotasdeviaje.notasdeviaje.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.data.Note;
import paquetenotasdeviaje.notasdeviaje.control.NoteController;
import paquetenotasdeviaje.notasdeviaje.ui.adapters.ListNotesAdapter;

public class PrincipalActivity extends AppCompatActivity {

    NoteController _noteController;
    ListView lsv_noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        _noteController = new NoteController(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_app);


        lsv_noteList = findViewById(R.id.lst_notelist);
        lsv_noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToNoteActivity("select_note", ((Note) lsv_noteList.getItemAtPosition(position)) );
            }
        });
        registerForContextMenu(lsv_noteList); //se establece que el listview listaDeNotas va a tener un ContextMenu


        FloatingActionButton btn_agregarNota = findViewById(R.id.btn_newnote_principalactivity);
        btn_agregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNoteActivity("",null);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Note> notes = _noteController.getNoteList("");
        listNotes(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_principal_action,menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                listNotes( _noteController.getNoteList(searchText.trim()) );
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_principal_context_action,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        final AdapterView.AdapterContextMenuInfo informacionItemSeleccionado = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.item_delete_principalactivity:

                new AlertDialog.Builder(PrincipalActivity.this)
                        .setTitle( R.string.eliminar_nota )
                        .setMessage( R.string.alerta_eliminar_nota )
                        .setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                _noteController.deleteNote( (Note) lsv_noteList.getItemAtPosition(informacionItemSeleccionado.position) );
                                listNotes(_noteController.getNoteList(""));

                            }
                        }).setNegativeButton( R.string.cancelar,null)
                        .show();

                break;
        }

        return super.onContextItemSelected(item);
    }


    public void listNotes (List<Note> notes){
        ListNotesAdapter adapter = new ListNotesAdapter(this, R.layout.listitem_principal, notes);
        lsv_noteList.setAdapter(adapter);
    }


    private void goToNoteActivity(String name, Note note){

        Intent intent = new Intent(PrincipalActivity.this, NoteActivity.class);
        if( !name.isEmpty() || note != null ){
            intent.putExtra( name, note );
        }
        startActivity(intent);
        finish();
    }

}
