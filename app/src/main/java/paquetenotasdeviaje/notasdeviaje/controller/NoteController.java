package paquetenotasdeviaje.notasdeviaje.controller;

import android.content.Context;

import java.util.List;

import paquetenotasdeviaje.notasdeviaje.model.Note;
import paquetenotasdeviaje.notasdeviaje.model.DaoNote;

public class NoteController {

    private DaoNote _daoNote;

    public NoteController(Context context) {
        _daoNote = new DaoNote(context);
    }

    public List<Note> getNoteList(String find) {
        return _daoNote.listNotes(find);
    }

    public boolean deleteNote(Note note){
       return _daoNote.delete(note);
    }

    public boolean saveNote(Note noteToSave){

        boolean isNewNote = noteToSave.getId().isEmpty();

        if (!noteToSave.isEmpty()) {
            if (isNewNote)
                return _daoNote.save(noteToSave);
             else
                return _daoNote.update(noteToSave);
        }
        else
            return false;

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

- nota nueva		    alerta salir sin guardar - guardar		        GUARDAR                         validacion1();
titulo vacio
descripcion llena

- nota nueva		    alerta salir sin guardar - guardar		        GUARDAR                         validacion2();
titulo llena
descripcion llena

_______________________________________________________________________________________________________________________________

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
