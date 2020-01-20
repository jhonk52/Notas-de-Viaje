package paquetenotasdeviaje.notasdeviaje.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

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
import notasdeviaje.notasdeviaje.modelos.Nota;
import paquetenotasdeviaje.notasdeviaje.basededatos.BasedeDatos;
import paquetenotasdeviaje.notasdeviaje.basededatos.CamposBasedeDatos;

public class Principal extends AppCompatActivity {

    ListView listaDeNotas;
    FloatingActionButton btn_agregarNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_app2);


        btn_agregarNota = findViewById(R.id.btn_agregarnota_principal);
        listaDeNotas = findViewById(R.id.listaDeNotas);
        listarDatos("");

        listaDeNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) listaDeNotas.getItemAtPosition(position);
                Intent intent = new Intent(Principal.this, EscribirNota.class);

                intent.putExtra("Titulo",cursor.getString(1));

                startActivity(intent);
                finish();

            }
        });
        registerForContextMenu(listaDeNotas);

        btn_agregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, EscribirNota.class);
                startActivity(intent);
                finish();
            }
        });


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
            public boolean onQueryTextChange(String newText) {
                listarDatos(newText.trim());

                //Toast.makeText(Principal.this, newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){



        }



        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_lista_principal,menu);
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo informacionItemSeleccionado = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.item_eliminar_principal:

                BasedeDatos basedeDatos = new BasedeDatos(Principal.this);
                Cursor cursor = (Cursor) listaDeNotas.getItemAtPosition(informacionItemSeleccionado.position);

                String tituloNotaAEliminar = cursor.getString(cursor.getColumnIndex(CamposBasedeDatos.CAMPO_TITULO));
                basedeDatos.eliminarNota(new Nota(tituloNotaAEliminar,""));

                listarDatos("");
                break;

        }


        return super.onContextItemSelected(item);
    }




    private void listarDatos(String queBuscar) {

        int ids[] = {
                R.id.txt_titulo_plantillalistadenotas,
                R.id.txt_descripcion_plantillalistadenotas
        };

        String campos[] = {
                CamposBasedeDatos.CAMPO_TITULO,
                CamposBasedeDatos.CAMPO_DESCRIPCION
        };

        BasedeDatos bd = new BasedeDatos(getApplicationContext());
        Cursor datos = bd.listarNotas(queBuscar);

        //si encuentra datos
        //if(datos.moveToFirst()) {

            SimpleCursorAdapter adaptador = new SimpleCursorAdapter(Principal.this,R.layout.plantilla_listadenotas,datos,campos,ids,0);
            listaDeNotas.setAdapter(adaptador);

        //}


    }


}
