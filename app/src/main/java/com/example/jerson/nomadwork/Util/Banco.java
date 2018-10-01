package com.example.jerson.nomadwork.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.jerson.nomadwork.BasicClass.Local;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jerson on 05/03/2018.
 */

public class Banco extends SQLiteOpenHelper {


    public Banco(Context context) {
        super(context, DbConfig.NOME_DB, null, DbConfig.VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        String sqlComando = String.format("CREATE TABLE IF NOT EXISTS %s(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s DOUBLE NOT NULL, " +
                        " %s DOUBLE NOT NULL, " +
                        " %s TEXT NOT NULL) ",
                DbConfig.TABELA,
                DbConfig.Coluna.LAT,
                DbConfig.Coluna.LNG,
                DbConfig.Coluna.NAME
        );
        sql.execSQL(sqlComando);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertInto(Local l) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put(DbConfig.Coluna.NAME, l.getLocalName());
        values.put(DbConfig.Coluna.LAT, l.getLatitude());
        values.put(DbConfig.Coluna.LNG, l.getLongitude());
        getWritableDatabase().insert(DbConfig.TABELA, null, values);
    }

    public void lerBanco() {
        Cursor cursor = getWritableDatabase().query(DbConfig.TABELA, new String[]{
                        DbConfig.Coluna.ID, DbConfig.Coluna.LAT, DbConfig.Coluna.LNG,DbConfig.Coluna.NAME},
                null, null, null, null, DbConfig.Coluna.NAME);
        int colunaID = cursor.getColumnIndex(DbConfig.Coluna.ID);
        int colunaNome = cursor.getColumnIndex(DbConfig.Coluna.LAT);
        int colunaLatitude = cursor.getColumnIndex(DbConfig.Coluna.LAT);
        int colunaLongitude = cursor.getColumnIndex(DbConfig.Coluna.LNG);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                cursor.moveToNext();
            }
        }
    }

    public List<Local> getList(String order) {
        List<Local> local = new ArrayList<>();
        String sqlComand = "SELECT * FROM " + DbConfig.TABELA + " ORDER BY " + DbConfig.Coluna.NAME + " " + order + ";";
        Cursor cursor = getReadableDatabase().rawQuery(sqlComand, null);
        while (cursor.moveToNext()) {
            Local l = new Local();
            l.setLocalName(  cursor.getString(cursor.getColumnIndex( DbConfig.Coluna.NAME))  );
            l.setLatitude( Double.valueOf( cursor.getString(cursor.getColumnIndex( DbConfig.Coluna.LAT)) ) );
            l.setLongitude( Double.valueOf( cursor.getString(cursor.getColumnIndex( DbConfig.Coluna.LNG)) ) );
            local.add(l);
        }
        cursor.close();
        return local;
    }
}
