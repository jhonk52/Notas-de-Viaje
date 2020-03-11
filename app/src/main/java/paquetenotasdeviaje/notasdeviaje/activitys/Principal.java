package paquetenotasdeviaje.notasdeviaje.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.adaptadores.AdaptadorListaDeNotas;
import paquetenotasdeviaje.notasdeviaje.modelos.Nota;
import paquetenotasdeviaje.notasdeviaje.basededatos.BasedeDatos;

public class Principal extends AppCompatActivity {

    ListView listaDeNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_app);


        FloatingActionButton btn_agregarNota = findViewById(R.id.btn_agregarnota_principal);
        listaDeNotas = findViewById(R.id.listaDeNotas);
        registerForContextMenu(listaDeNotas); //se establece que el listview listaDeNotas va a tener un ContextMenu


        listaDeNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Nota nota = (Nota) listaDeNotas.getItemAtPosition(position);
                pasarAActivity_EscribirNota("Titulo",nota);
            }
        });


        btn_agregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarAActivity_EscribirNota("",null);
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        listarNotas("");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.optionsmenu_principal,menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                listarNotas(searchText.trim());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

/*
    //Menu de opciones aun no se está utilizando, solo el searchView
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){}
        return super.onOptionsItemSelected(item);}
*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_lista_principal,menu);
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        final AdapterView.AdapterContextMenuInfo informacionItemSeleccionado = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.item_eliminar_principal:

                new AlertDialog.Builder(Principal.this)
                        .setTitle("Eliminar Nota")
                        .setMessage("Esta seguro que desea eliminar esta nota?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                BasedeDatos basedeDatos = new BasedeDatos(Principal.this);
                                basedeDatos.eliminarNota( (Nota)listaDeNotas.getItemAtPosition(informacionItemSeleccionado.position) );
                                basedeDatos.close();

                                listarNotas("");

                            }
                        }).setNegativeButton("Cancelar",null)
                        .show();

                break;
        }

        return super.onContextItemSelected(item);
    }




    private void listarNotas(String buscar) {

        BasedeDatos bd = new BasedeDatos(Principal.this);

        AdaptadorListaDeNotas adaptador = new AdaptadorListaDeNotas(Principal.this,R.layout.plantilla_listadenotas,bd.listarNotas(buscar));

        bd.close();

        listaDeNotas.setAdapter(adaptador);
    }




    private void pasarAActivity_EscribirNota( String name, Nota noteToIntent ){

        Intent intent = new Intent(Principal.this, EscribirNota.class);

        if( !name.isEmpty() || noteToIntent != null ){
            intent.putExtra( name, noteToIntent.convertToArrayString() );
        }

        startActivity(intent);
        finish();
    }




}
