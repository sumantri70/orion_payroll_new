package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.EmailModel;
import com.orionit.app.orion_payroll_new.models.KasbonPegawaiModel;
import com.orionit.app.orion_payroll_new.models.SettingDBModel;

import java.util.ArrayList;

public class SettingDBTable {
    private SQLiteDatabase db;
    private String SQL;

    public SettingDBTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
    }

    private ContentValues SetValue(SettingDBModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("accessKey", Data.getAccessKey());
        cv.put("accessSecret", Data.getAccessSecret());
        cv.put("password", Data.getPassword());
        return cv;
    }

    public void Insert(SettingDBModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        deleteAll();
        this.db.insert("setting_db", null, cv);
    }

    public void deleteAll(){
        this.db.delete("setting_db", null, null);
    }

//    public void Update(SettingDBModel Data) {
//        ContentValues cv;
//        cv = SetValue(Data);
//        this.db.update("setting_db", cv, "_id = " + Data.getId(), null);
//    }

    public SettingDBModel GetData(){
        Cursor cr = this.db.rawQuery("SELECT accessKey, accessSecret, password FROM setting_db", null);
        SettingDBModel Data = new SettingDBModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new SettingDBModel(
                    cr.getString(cr.getColumnIndexOrThrow("accessKey")),
                    cr.getString(cr.getColumnIndexOrThrow("accessSecret")),
                    cr.getString(cr.getColumnIndexOrThrow("password"))
            );
        }
        cr.close();
        return Data;
    }



}
