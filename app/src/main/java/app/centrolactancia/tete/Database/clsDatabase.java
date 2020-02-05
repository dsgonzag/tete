package app.centrolactancia.tete.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class clsDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="dblocal";
    private static final String TABLE_NAME ="tbListaLocales";


    public clsDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql="";
        sql="CREATE TABLE "+ TABLE_NAME +" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre_establecimiento text, direccion text, latitud REAL, longitud REAL) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
