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

import paquetenotasdeviaje.notasdeviaje.modelos.Nota;
import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.basededatos.BasedeDatos;

public class EscribirNota extends AppCompatActivity{

    EditText txt_title, txt_description;
    Boolean isNoteNew;
    Nota noteToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escribir_nota);


        txt_title = findViewById(R.id.txt_titulo_crearnota);
        txt_description = findViewById(R.id.txt_descripcion_crearnota);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteToEdit = new Nota();

        if( getIntent().getExtras() != null ) { // Si al llamar este activity le mandaron informacion


            noteToEdit.setNoteFromArrayString( getIntent().getExtras().getStringArray("Titulo") );
            getSupportActionBar().setTitle(R.string.editar_nota);
            isNoteNew = false;

            txt_title.setText(noteToEdit.getTitulo());
            txt_description.setText(noteToEdit.getDescripcion());

            hideKeypad();


        }else{ // Si al llamar este activity NO le mandaron informacion

            getSupportActionBar().setTitle(R.string.escribir_nota);
            txt_title.requestFocus();
            isNoteNew = true;

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


                if ( check1() ) {

                    Toast.makeText(EscribirNota.this, "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                    txt_title.requestFocus();

                } else if ( check2() ){

                    saveAndExit();

                } else if ( check3() ){

                    updateAndExit();
                }


                break;

            case R.id.item_eliminar_menuescribirnota:


                 new AlertDialog.Builder(EscribirNota.this)
                    .setTitle("Eliminar Nota")
                            .setMessage("Esta seguro que desea eliminar esta nota?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (isNoteNew) {

                                        txt_title.getText().clear();
                                        txt_description.getText().clear();
                                        Toast.makeText(EscribirNota.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();

                                    }else{ // es nota a editar

                                        new BasedeDatos(EscribirNota.this).eliminarNota(noteToEdit);
                                        Toast.makeText(EscribirNota.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                                        exit();
                                    }
                                }
                            }).setNegativeButton("Cancelar",null)
                            .show();


            case android.R.id.home:

                checkBeforeExit();

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        checkBeforeExit();
        //super.onBackPressed();
    }




    private void checkBeforeExit(){

        AlertDialog.Builder msjAlerta = new AlertDialog.Builder(EscribirNota.this);

        if ( check4() ) {

            exit();

        } else if ( check1() ) {

            msjAlerta.setTitle("Nota sin Nombre")
                    .setMessage("Debe asignarle un nombre a esta nota")
                    .setPositiveButton("Cancelar", null).
                    setNegativeButton("Salir sin guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exit();
                }
            }).show();

        } else if ( check2() ){

            msjAlerta.setTitle("Salir")
                    .setMessage("Desea Salir sin guardar?")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exit();
                        }
                    }).setNegativeButton("Guardar y salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveAndExit();
                }
            }).show();

        } else if ( check3() ){

            msjAlerta.setTitle("Salir")
                    .setMessage("Desea Salir sin guardar?")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exit();
                        }
                    }).setNegativeButton("Guardar y salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateAndExit();
                }
            }).show();

        }

    }




    private void saveAndExit(){

        Nota nota = new Nota(txt_title.getText().toString(), txt_description.getText().toString());
        BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);

        if(basedeDatos.registrarNota(nota)){

            Toast.makeText(EscribirNota.this, "Nota Guardada!", Toast.LENGTH_SHORT).show();
            exit();
        }else{
            Toast.makeText(EscribirNota.this,"Ha ocurrido un error,\nIntente Nuevamente",Toast.LENGTH_SHORT).show();
        }
    }




    private void updateAndExit(){

        Nota nota = new Nota(txt_title.getText().toString(), txt_description.getText().toString());
        BasedeDatos basedeDatos = new BasedeDatos(EscribirNota.this);

        if(basedeDatos.actualizarNota(nota)){

            Toast.makeText(EscribirNota.this, "Nota Actualizada!", Toast.LENGTH_SHORT).show();
            exit();
        }else{
            Toast.makeText(EscribirNota.this,"Ha ocurrido un error,\nIntente Nuevamente",Toast.LENGTH_SHORT).show();
        }
    }




    private void exit(){

        Intent intent = new Intent(EscribirNota.this, Principal.class);
        startActivity(intent);
        finish();
    }



    @SuppressLint("ClickableViewAccessibility")
    private void hideKeypad(){

        txt_title.setFocusableInTouchMode(false);
        txt_description.setFocusableInTouchMode(false);

        txt_description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txt_description.setFocusableInTouchMode(true);
                txt_description.requestFocus();
                return false;
            }
        });

        txt_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txt_title.setFocusableInTouchMode(true);
                txt_description.requestFocus();
                return false;
            }
        });

    }



    // ver tabla de validaciones al final
    private boolean check1(){
        return  (isNoteNew && !txt_description.getText().toString().isEmpty() && txt_title.getText().toString().isEmpty());
    }
    private boolean check2(){
        return ( (isNoteNew && !txt_title.getText().toString().isEmpty() && txt_description.getText().toString().isEmpty()) ||
                 (isNoteNew && !txt_title.getText().toString().isEmpty() && !txt_description.getText().toString().isEmpty()) ||
                 (!isNoteNew && !(noteToEdit.getTitulo().equals(txt_title.getText().toString()))  &&  noteToEdit.getDescripcion().equals(txt_description.getText().toString())) ||
                 (!isNoteNew && !(noteToEdit.getTitulo().equals(txt_title.getText().toString()))  &&  !(noteToEdit.getDescripcion().equals(txt_description.getText().toString())))
               );
        }
    private boolean check3(){
        return ( !isNoteNew && noteToEdit.getTitulo().equals(txt_title.getText().toString()) && !(noteToEdit.getDescripcion().equals(txt_description.getText().toString())) );
        }
    private boolean check4(){
        return noteToEdit.equals(new Nota(txt_title.getText().toString(), txt_description.getText().toString()));
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
