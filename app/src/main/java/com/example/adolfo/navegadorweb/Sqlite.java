package com.example.adolfo.navegadorweb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.adolfo.navegadorweb.R.id.texto;

/**
 * Created by adolf on 18/10/2016.
 */

public class Sqlite extends SQLiteOpenHelper{

    private static final int VERSION_BASEDATOS = 2;
    private static final String NOMBRE_BASEDATOS = "BD_Navegador.db";

    private static final String ins = "CREATE TABLE paginas (codigo INTEGER PRIMARY KEY AUTOINCREMENT, titulo VARCHAR(100))";



    public Sqlite(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ins);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS paginas");
        onCreate(sqLiteDatabase);
    }

}
