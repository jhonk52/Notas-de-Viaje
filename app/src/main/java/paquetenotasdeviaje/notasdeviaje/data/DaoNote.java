package paquetenotasdeviaje.notasdeviaje.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DaoNote extends SQLiteOpenHelper {


    public DaoNote(@Nullable Context context) {
        super(context, "notasdeviaje.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DaoNoteColumns.TABLE + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DaoNoteColumns.COLUMN_TITLE + " TEXT, " +
                DaoNoteColumns.COLUMN_DESCRIPTION + " TEXT "+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DaoNoteColumns.TABLE);

        onCreate(db);
    }


    public boolean save(Note note){

        ContentValues values = new ContentValues();

        values.put(DaoNoteColumns.COLUMN_TITLE, note.getTitle());
        values.put(DaoNoteColumns.COLUMN_DESCRIPTION, note.getDescription());

        SQLiteDatabase bd = getWritableDatabase();

        long num = bd.insert(DaoNoteColumns.TABLE, null, values);
        bd.close();

        return num > 0;
    }


    public boolean delete(Note note) {

        SQLiteDatabase db = getWritableDatabase();
        String[] argument = {note.getId()};
        int num = db.delete(DaoNoteColumns.TABLE, DaoNoteColumns.COLUMN_ID +"=?",argument);
        db.close();
        return num > 0;
    }


    public List<Note> listNotes(String find){

        SQLiteDatabase db = getReadableDatabase();
        Cursor data;
        List<Note> listResult = new ArrayList<>();

        if (find.isEmpty()) {
            data = db.rawQuery("SELECT * FROM " + DaoNoteColumns.TABLE, null);
        }else {
            data = db.rawQuery("SELECT * FROM " + DaoNoteColumns.TABLE +" where "+ DaoNoteColumns.COLUMN_TITLE + " like '%" + find + "%'", null);
        }

        if (data.moveToLast()) {
            for (int i = data.getPosition(); i >= 0 ; i--) {
                listResult.add(new Note(data.getString(data.getColumnIndex(DaoNoteColumns.COLUMN_ID)),
                                        data.getString(data.getColumnIndex(DaoNoteColumns.COLUMN_TITLE)),
                                        data.getString(data.getColumnIndex(DaoNoteColumns.COLUMN_DESCRIPTION))));
              data.moveToPrevious();
            }
        }
            data.close();
            db.close();

        return listResult;
    }


    public boolean update(Note note){

        ContentValues values = new ContentValues();

        values.put(DaoNoteColumns.COLUMN_TITLE, note.getTitle());
        values.put(DaoNoteColumns.COLUMN_DESCRIPTION, note.getDescription());

        String[] selectionArgs = {note.getId()};

        SQLiteDatabase db = getWritableDatabase();
        int var = db.update(DaoNoteColumns.TABLE, values, DaoNoteColumns.COLUMN_ID +"=?", selectionArgs);
        db.close();

        return var > 0;

    }

    /*    public Note read(String titulo) {

        String[] columns = {DaoNoteColumns.COLUMN_TITLE, DaoNoteColumns.COLUMN_DESCRIPTION};
        String selection = DaoNoteColumns.COLUMN_TITLE + " = ? ";
        String[] selectionArgs = {titulo};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor data = db.query(DaoNoteColumns.TABLE, columns, selection, selectionArgs, groupBy, having, orderBy, limit);


        if (data.moveToFirst()) {
            Note note = new Note(data.getString(data.getColumnIndex(DaoNoteColumns.COLUMN_ID)),
                                 data.getString(data.getColumnIndex(DaoNoteColumns.COLUMN_TITLE)),
                                 data.getString(data.getColumnIndex(DaoNoteColumns.COLUMN_DESCRIPTION)));

            data.close();
            db.close();

            return note;
        } else {
            return null;
        }
    }*/

    /*    @Override
    public synchronized void close() {
        super.close();
    }*/
}
