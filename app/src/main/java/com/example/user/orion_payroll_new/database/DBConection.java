package com.example.user.orion_payroll_new.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConection extends SQLiteOpenHelper{
    public DBConection(Context context) {
        super(context, "db_orion_payroll.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Sql;

        Sql =  "CREATE TABLE master_pegawai (" +
               "_id integer primary key, " +  //id pakai _ biar auto nambah
               "nik text, " +
               "nama text, " +
               "alamat text, " +
               "telpon1 text, " +
               "telpon2 text, " +
               "email text, " +
               "gaji_pokok real, " +
               "tgl_lahir integer, " +
               "status text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE master_tunjangan (" +
                "_id integer primary key, " +
                "kode text, " +
                "nama text, " +
                "keterangan text, " +
                "status text)";
        db.execSQL(Sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {  //untuk update database untuk penambahan baru

    }
}
