package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.KasbonPegawaiModel;

import java.util.ArrayList;

public class PasswordAplikasiTable {
    private SQLiteDatabase db;
    private ArrayList<KasbonPegawaiModel> records;
    private String SQL;

    public PasswordAplikasiTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
    }

    private ContentValues SetValue(String password) {
        ContentValues cv = new ContentValues();
        cv.put("password", password);
        return cv;
    }

//    public void Insert(String password) {
//        ContentValues cv;
//        cv = SetValue(password);
//        this.db.insert("password_aplikasi", null, cv);
//    }

    public void Update(String password) {
//        ContentValues cv;
        String sql = "REPLACE INTO password_aplikasi(_id, password) VALUES((SELECT min(_id) from password_aplikasi), '"  + password + "')";
        this.db.execSQL(sql);
        //this.db.update("password_aplikasi", cv, "_id <> 0", null);
    }

    public boolean CekPassword(String password){
        Cursor cr = this.db.rawQuery("SELECT _id, password FROM password_aplikasi WHERE password = '"+password+"'" , null);
        return cr.getCount() > 0;
    }

//    public boolean CekPasswordAwal(){
//        Cursor cr = this.db.rawQuery("SELECT _id, password FROM password_aplikasi WHERE password = ''" , null);
//        return cr.getCount() > 0;
//    }

    public boolean IsAdaPassword(){
        Cursor cr = this.db.rawQuery("SELECT COUNT(*) as hasil FROM password_aplikasi", null);
        cr.moveToFirst();
        int haha = cr.getColumnIndexOrThrow("hasil");
        return cr.getInt(cr.getColumnIndexOrThrow("hasil")) > 0;
    }
}
