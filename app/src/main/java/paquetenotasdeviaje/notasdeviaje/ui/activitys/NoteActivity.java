package paquetenotasdeviaje.notasdeviaje.ui.activitys;

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

import paquetenotasdeviaje.notasdeviaje.R;
import paquetenotasdeviaje.notasdeviaje.data.Note;
import paquetenotasdeviaje.notasdeviaje.controller.NoteController;

public class NoteActivity extends AppCompatActivity{

    EditText txt_title, txt_description;
    Boolean isNewNote;
    Note _note;
    NoteController _noteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        _noteController = new NoteController(this);

        txt_title = findViewById(R.id.txt_title_noteactivity);
        txt_description = findViewById(R.id.txt_description_noteactivity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if( getIntent().getExtras() != null ) { // Si al llamar este activity le mandaron informacion

            getSupportActionBar().setTitle( R.string.editar_nota );
            _note = (Note) getIntent().getSerializableExtra("select_note");
            isNewNote = false;

            txt_title.setText(_note.getTitle());
            txt_description.setText(_note.getDescription());

            hideKeypad();

        }else{ // Si al llamar este activity NO le mandaron informacion
            getSupportActionBar().setTitle(R.string.escribir_nota);
            _note = new Note("","");
            isNewNote = true;

            txt_title.requestFocus();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_note_action,menu);
        MenuItem itemDuplicate = menu.findItem(R.id.item_duplicate_noteactivity);

        if(isNewNote)
            itemDuplicate.setVisible(false);
        else
            itemDuplicate.setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.item_save_noteactivity:

                _note.setTitle(txt_title.getText().toString());
                _note.setDescription(txt_description.getText().toString() );

                if( _noteController.saveNote(_note) ) {
                    Toast.makeText(this, R.string.nota_guardada, Toast.LENGTH_SHORT).show();
                    exit();
                }else
                    Toast.makeText(NoteActivity.this, R.string.error_guardar, Toast.LENGTH_SHORT).show();

                break;

            case R.id.item_delete_noteactivity:

                 new AlertDialog.Builder(NoteActivity.this)
                    .setTitle( R.string.eliminar_nota )
                            .setMessage( R.string.alerta_eliminar_nota )
                            .setPositiveButton( R.string.eliminar , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (isNewNote) {
                                        txt_title.getText().clear();
                                        txt_description.getText().clear();
                                        Toast.makeText(NoteActivity.this, R.string.nota_eliminada, Toast.LENGTH_SHORT).show();
                                        exit();
                                    }else{ // es nota a editar
                                        if(_noteController.deleteNote(_note)) {
                                            Toast.makeText(NoteActivity.this, R.string.nota_eliminada, Toast.LENGTH_SHORT).show();
                                            exit();
                                        }else{
                                            Toast.makeText(NoteActivity.this, R.string.error_eliminar, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }).setNegativeButton(R.string.cancelar,null)
                            .show();
                 break;

            case R.id.item_duplicate_noteactivity:

                if( _noteController.saveNote( new Note( txt_title.getText().toString(), txt_description.getText().toString() ) ) ) {
                    Toast.makeText(this, R.string.duplicar_nota , Toast.LENGTH_SHORT).show();
                    exit();
                }else
                    Toast.makeText(NoteActivity.this, R.string.error_duplicar , Toast.LENGTH_SHORT).show();

                break;

            case android.R.id.home:

                checkBeforeExit();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        checkBeforeExit();
    }


    private void checkBeforeExit(){

        if ( _note.equals(new Note(txt_title.getText().toString(), txt_description.getText().toString())) ) {

            exit();

        }else{

            new AlertDialog.Builder(NoteActivity.this)
                    .setTitle( R.string.salir )
                    .setMessage( R.string.salir_sin_guardar )
                    .setPositiveButton( R.string.salir , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exit();
                        }
                    }).setNegativeButton( R.string.guardar_y_salir, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    _note.setTitle(txt_title.getText().toString());
                    _note.setDescription(txt_description.getText().toString() );

                    if( _noteController.saveNote(_note) ) {
                        Toast.makeText(NoteActivity.this, R.string.nota_guardada, Toast.LENGTH_SHORT).show();
                        exit();
                    }else
                        Toast.makeText(NoteActivity.this, R.string.error_guardar, Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
    }


    private void exit(){

        Intent intent = new Intent(NoteActivity.this, PrincipalActivity.class);
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

}
