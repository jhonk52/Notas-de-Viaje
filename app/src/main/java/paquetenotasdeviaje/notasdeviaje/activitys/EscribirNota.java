package paquetenotasdeviaje.notasdeviaje.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import notasdeviaje.notasdeviaje.modelos.Nota;
import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.basededatos.BasedeDatos;

public class EscribirNota extends AppCompatActivity{

    EditText txt_titulo,txt_descripcion;
    Boolean esNotaNueva;
    Nota notaAEditar;

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escribir_nota);

        notaAEditar = new Nota("","");
        esNotaNueva = true;


        txt_titulo = findViewById(R.id.txt_titulo_crearnota);
        txt_descripcion = findViewById(R.id.txt_descripcion_crearnota);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(getIntent().getExtras()!=null) {
            getSupportActionBar().setTitle(R.string.editar_nota);

            String titulo = getIntent().getExtras().getString("Titulo");
            esNotaNueva = false;

            BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);
            notaAEditar = basedeDatos.consultarNota(titulo);
            txt_titulo.setText(notaAEditar.getTitulo());
            txt_descripcion.setText(notaAEditar.getDescripcion());

            txt_titulo.setFocusableInTouchMode(false);
            txt_descripcion.setFocusableInTouchMode(false);

            txt_descripcion.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    txt_titulo.setFocusableInTouchMode(true);
                    txt_descripcion.setFocusableInTouchMode(true);
                    txt_descripcion.requestFocus();
                    return false;
                }
            });



        }else{
            getSupportActionBar().setTitle(R.string.escribir_nota);
            txt_titulo.requestFocus();
        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_escribirnota,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.item_guardar_menucrearnota:

                if (esNotaNueva == true && !txt_descripcion.getText().toString().isEmpty() &&
                        txt_titulo.getText().toString().isEmpty()) {

                    Toast.makeText(EscribirNota.this, "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                    txt_titulo.requestFocus();

                }


                if ( (esNotaNueva == true && !txt_titulo.getText().toString().isEmpty() && txt_descripcion.getText().toString().isEmpty()) ||
                        (esNotaNueva == true && !txt_titulo.getText().toString().isEmpty() && !txt_descripcion.getText().toString().isEmpty()) ||
                        (esNotaNueva == false && !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))  &&  notaAEditar.getTitulo().equals(txt_titulo.getText().toString())) ||
                        (esNotaNueva == false && !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))  &&  !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))) ){

                            guardarYSalir();

                }


                if ( esNotaNueva == false && notaAEditar.getTitulo().equals(txt_titulo.getText().toString()) && !(notaAEditar.getDescripcion().equals(txt_descripcion.getText().toString())) ){

                    actualizarYSalir();
                }

                break;

            case R.id.item_eliminar_menuescribirnota:

                if(esNotaNueva==false){

                    BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);
                    basedeDatos.eliminarNota(notaAEditar);
                    txt_titulo.getText().clear();
                    txt_descripcion.getText().clear();
                    Toast.makeText(EscribirNota.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                    irAPrincipal();

                }else{
                    txt_titulo.getText().clear();
                    txt_descripcion.getText().clear();
                    Toast.makeText(EscribirNota.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();

                }

                break;

            case android.R.id.home:

                accionesAntesDeSalir();

                break;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {

        accionesAntesDeSalir();

    }




    private void accionesAntesDeSalir(){

        if ( notaAEditar.equals(new Nota(txt_titulo.getText().toString(),txt_descripcion.getText().toString())) ) {

            irAPrincipal();
        }


        if (esNotaNueva == true && !txt_descripcion.getText().toString().isEmpty() &&
                txt_titulo.getText().toString().isEmpty()) {

            new AlertDialog.Builder(EscribirNota.this)
                    .setTitle("Nota sin Nombre")
                    .setMessage("Debe asignarle un nombre a esta nota")
                    .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    }).setNegativeButton("Salir sin guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    irAPrincipal();
                }
            }).show();

        }


        if ( (esNotaNueva == true && !txt_titulo.getText().toString().isEmpty() && txt_descripcion.getText().toString().isEmpty()) ||
                (esNotaNueva == true && !txt_titulo.getText().toString().isEmpty() && !txt_descripcion.getText().toString().isEmpty()) ||
                (esNotaNueva == false && !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))  &&  notaAEditar.getTitulo().equals(txt_titulo.getText().toString())) ||
                (esNotaNueva == false && !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))  &&  !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))) ){

            new AlertDialog.Builder(EscribirNota.this)
                    .setTitle("Salir")
                    .setMessage("Desea Salir sin guardar?")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            irAPrincipal();
                        }
                    }).setNegativeButton("Guardar y salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    guardarYSalir();
                }
            }).show();

        }
        //super.onBackPressed();

        if ( esNotaNueva == false && notaAEditar.getTitulo().equals(txt_titulo.getText().toString()) && !(notaAEditar.getDescripcion().equals(txt_descripcion.getText().toString())) ){

            new AlertDialog.Builder(EscribirNota.this)
                    .setTitle("Salir")
                    .setMessage("Desea Salir sin guardar?")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            irAPrincipal();
                        }
                    }).setNegativeButton("Guardar y salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actualizarYSalir();
                }
            }).show();

        }

    }




    private void guardarYSalir(){

        Nota nota = new Nota(txt_titulo.getText().toString(),txt_descripcion.getText().toString());

        BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);

        if(basedeDatos.registrarNota(nota)){

            Toast.makeText(EscribirNota.this, "Nota Guardada!", Toast.LENGTH_SHORT).show();

            irAPrincipal();

        }else{
            Toast.makeText(EscribirNota.this,"Ha ocurrido un error,\nIntente Nuevamente",Toast.LENGTH_SHORT).show();
        }
    }




    private void actualizarYSalir(){

        BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);

        if(basedeDatos.actualizarNota(new Nota(txt_titulo.getText().toString(),txt_descripcion.getText().toString()))){

            Toast.makeText(EscribirNota.this, "Nota Actualizada!", Toast.LENGTH_SHORT).show();

            irAPrincipal();
        }else{
            Toast.makeText(EscribirNota.this,"Ha ocurrido un error,\nIntente Nuevamente",Toast.LENGTH_SHORT).show();
        }

    }




    private void irAPrincipal(){

        Intent intent = new Intent(EscribirNota.this, Principal.class);
        startActivity(intent);
        finish();
    }




}


