package paquetenotasdeviaje.notasdeviaje.basededatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import paquetenotasdeviaje.notasdeviaje.modelos.Nota;

public class BasedeDatos extends SQLiteOpenHelper {



    public BasedeDatos(@Nullable Context context) {
        super(context, "notasdeviaje.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CamposBasedeDatos.TABLA + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CamposBasedeDatos.CAMPO_TITULO + " TEXT UNIQUE, " +
                CamposBasedeDatos.CAMPO_DESCRIPCION + " TEXT "+
                ")");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CamposBasedeDatos.TABLA);

        onCreate(db);
    }




    public boolean registrarNota(Nota nota){

        ContentValues valores = new ContentValues();

        valores.put(CamposBasedeDatos.CAMPO_TITULO, nota.getTitulo());
        valores.put(CamposBasedeDatos.CAMPO_DESCRIPCION, nota.getDescripcion());

        SQLiteDatabase bd = getWritableDatabase();

        long num = bd.insert(CamposBasedeDatos.TABLA, null, valores);

        if(num>0){
            return true;
        }
        else {
            return false;
        }
    }




    public Nota consultarNota(String titulo) {

        String[] columns = {CamposBasedeDatos.CAMPO_TITULO, CamposBasedeDatos.CAMPO_DESCRIPCION};
        String selection = CamposBasedeDatos.CAMPO_TITULO + " = ? ";
        String[] selectionArgs = {titulo};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        SQLiteDatabase bd = getReadableDatabase();

        Cursor datos = bd.query(CamposBasedeDatos.TABLA, columns, selection, selectionArgs, groupBy, having, orderBy, limit);


        if (datos.moveToFirst()) {
            Nota nota = new Nota();

            nota.setTitulo(datos.getString(datos.getColumnIndex(CamposBasedeDatos.CAMPO_TITULO)));
            nota.setDescripcion(datos.getString(datos.getColumnIndex(CamposBasedeDatos.CAMPO_DESCRIPCION)));

            datos.close();

            return nota;
        } else {
            return null;
        }
    }




    public void eliminarNota (Nota nota) {

        SQLiteDatabase bd = getWritableDatabase();

        String[] argumento = {nota.getTitulo()};

        bd.delete(CamposBasedeDatos.TABLA,CamposBasedeDatos.CAMPO_TITULO+"=?",argumento);

    }




    public List<Nota> listarNotas(String queBuscar){

        SQLiteDatabase bd = getReadableDatabase();
        Cursor datos;
        List<Nota> lista = new ArrayList<>();

        if (queBuscar.isEmpty()) {
            datos = bd.rawQuery("SELECT * FROM " + CamposBasedeDatos.TABLA, null);

        }else {
            datos = bd.rawQuery("SELECT * FROM " + CamposBasedeDatos.TABLA+" where "+CamposBasedeDatos.CAMPO_TITULO + " like '%" + queBuscar + "%'", null);
        }

        if (datos.moveToLast()) {
            for (int i = datos.getPosition(); i >= 0 ; i--) {
                lista.add(new Nota(datos.getString(datos.getColumnIndex(CamposBasedeDatos.CAMPO_TITULO)),
                                   datos.getString(datos.getColumnIndex(CamposBasedeDatos.CAMPO_DESCRIPCION))));
              datos.moveToPrevious();
            }
        }
            datos.close();

        return lista;
    }




    public boolean actualizarNota (Nota nota){

        ContentValues valores = new ContentValues();

        valores.put(CamposBasedeDatos.CAMPO_TITULO, nota.getTitulo());
        valores.put(CamposBasedeDatos.CAMPO_DESCRIPCION, nota.getDescripcion());

        String[] selectionArgs = {nota.getTitulo()};

        SQLiteDatabase bd = getWritableDatabase();

        int var = bd.update(CamposBasedeDatos.TABLA,valores,CamposBasedeDatos.CAMPO_TITULO+"=?", selectionArgs);

        if(var > 0){
            return true;
        }else {
            return false;
        }

    }




}
