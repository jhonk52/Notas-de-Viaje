package paquetenotasdeviaje.notasdeviaje.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import paquetenotasdeviaje.notasdeviaje.modelos.Nota;
import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.basededatos.BasedeDatos;

public class EscribirNota extends AppCompatActivity{

    EditText txt_titulo,txt_descripcion;
    Boolean esNotaNueva;
    Nota notaAEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escribir_nota);

        notaAEditar = new Nota("","");

        txt_titulo = findViewById(R.id.txt_titulo_crearnota);
        txt_descripcion = findViewById(R.id.txt_descripcion_crearnota);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Si al llamar este activity le mandaron informacion
        if(getIntent().getExtras()!=null) {

            String titulo = getIntent().getExtras().getString("Titulo");
            getSupportActionBar().setTitle(R.string.editar_nota);
            esNotaNueva = false;

            BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);
            notaAEditar = basedeDatos.consultarNota(titulo);

            txt_titulo.setText(notaAEditar.getTitulo());
            txt_descripcion.setText(notaAEditar.getDescripcion());

            ocultarTecladoHastaDarClick();

        }else{ // Si al llamar este activity NO le mandaron informacion
            getSupportActionBar().setTitle(R.string.escribir_nota);
            txt_titulo.requestFocus();
            esNotaNueva = true;
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

                if ( validacion1() ) {
                    Toast.makeText(EscribirNota.this, "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                    txt_titulo.requestFocus();
                }

                if ( validacion2() ){
                    guardarYSalir();
                }

                if ( validacion3() ){
                    actualizarYSalir();
                }

                break;

            case R.id.item_eliminar_menuescribirnota:

                AlertDialog.Builder msjAlerta = new AlertDialog.Builder(EscribirNota.this);

                if(!esNotaNueva){// si es nota a editar...

                    msjAlerta.setTitle("Eliminar Nota")
                            .setMessage("Esta seguro que desea eliminar esta nota?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new BasedeDatos(EscribirNota.this).eliminarNota(notaAEditar);
                                    Toast.makeText(EscribirNota.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                                    irAPrincipal();

                                }
                            }).setNegativeButton("Cancelar",null)
                            .show();

                }else{// si es una nueva nota

                    msjAlerta.setTitle("Eliminar Nota")
                            .setMessage("Esta seguro que desea eliminar esta nota?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    txt_titulo.getText().clear();
                                    txt_descripcion.getText().clear();
                                    Toast.makeText(EscribirNota.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("Cancelar",null)
                            .show();
                }
                break;

            case android.R.id.home:

                validacionesAntesDeSalir();

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        validacionesAntesDeSalir();
        //super.onBackPressed();
    }




    private void validacionesAntesDeSalir(){
        AlertDialog.Builder msjAlerta = new AlertDialog.Builder(EscribirNota.this);

        if ( validacion4() ) {
            irAPrincipal();
        }

        if ( validacion1() ) {

            msjAlerta.setTitle("Nota sin Nombre")
                    .setMessage("Debe asignarle un nombre a esta nota")
                    .setPositiveButton("Cancelar", null).
                    setNegativeButton("Salir sin guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    irAPrincipal();
                }
            }).show();

        }


        if ( validacion2() ){

            msjAlerta.setTitle("Salir")
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


        if ( validacion3() ){

            msjAlerta.setTitle("Salir")
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

        Nota nota = new Nota(txt_titulo.getText().toString(),txt_descripcion.getText().toString());
        BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);

        if(basedeDatos.actualizarNota(nota)){

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



    @SuppressLint("ClickableViewAccessibility")
    private void ocultarTecladoHastaDarClick(){

        txt_titulo.setFocusableInTouchMode(false);
        txt_descripcion.setFocusableInTouchMode(false);

        txt_descripcion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txt_descripcion.setFocusableInTouchMode(true);
                txt_descripcion.requestFocus();
                return false;
            }
        });

        txt_titulo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txt_titulo.setFocusableInTouchMode(true);
                txt_descripcion.requestFocus();
                return false;
            }
        });

    }



    // ver tabla de validaciones al final
    private boolean validacion1(){
        return  (esNotaNueva && !txt_descripcion.getText().toString().isEmpty() && txt_titulo.getText().toString().isEmpty());
    }
    private boolean validacion2(){
        return ( (esNotaNueva && !txt_titulo.getText().toString().isEmpty() && txt_descripcion.getText().toString().isEmpty()) ||
                 (esNotaNueva && !txt_titulo.getText().toString().isEmpty() && !txt_descripcion.getText().toString().isEmpty()) ||
                 (!esNotaNueva && !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))  &&  notaAEditar.getDescripcion().equals(txt_descripcion.getText().toString())) ||
                 (!esNotaNueva && !(notaAEditar.getTitulo().equals(txt_titulo.getText().toString()))  &&  !(notaAEditar.getDescripcion().equals(txt_descripcion.getText().toString())))
               );
        }
    private boolean validacion3(){
        return ( !esNotaNueva && notaAEditar.getTitulo().equals(txt_titulo.getText().toString()) && !(notaAEditar.getDescripcion().equals(txt_descripcion.getText().toString())) );
        }
    private boolean validacion4(){
        return notaAEditar.equals(new Nota(txt_titulo.getText().toString(),txt_descripcion.getText().toString()));
    }


}










/* /////////////// TABLA DE VALIDACIONES/////////////////////


_______________________________________________________________________________________________________________________________
Condicion:          |   acciones al presionar back:          |   acciones al presionar guardar:      |   metodo de validacion  |
____________________|________________________________________|_______________________________________|_________________________|
                    |                                        |                                       |                         |
- nota nueva		    SALIR						                    ninguna accion
titulo vacio
descripcion vacio

- nota nueva            alerta salir sin guardar - guardar		        GUARDAR                         validacion2();
titulo lleno
descripcion vacio

- nota nueva		    alerta nota sin nombre, salir sin guardar	    Toast: nota sin nombre          validacion1();
titulo vacio
descripcion llena

- nota nueva		    alerta salir sin guardar - guardar		        GUARDAR                         validacion2();
titulo llena
descripcion llena


- nota editar		    SALIR						                    ninguna accion                  validacion4();
titulo igual
descripcion igual

- nota editar		    alerta salir sin guardar - guardar		        GUARDAR                         validacion2();
titulo cambio
descripcion igual

nota editar		        alerta salir sin guardar - actualizar		    ACTUALIZAR                      validacion3();
titulo igual
descripcion cambio

- nota editar		    alerta salir sin guardar - guardar		        GUARDAR                         validacion2();
titulo cambio
descripcion cambio

*/
