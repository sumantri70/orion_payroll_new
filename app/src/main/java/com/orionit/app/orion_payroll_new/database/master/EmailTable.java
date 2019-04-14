package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.EmailModel;
import com.orionit.app.orion_payroll_new.models.KasbonPegawaiModel;

import java.util.ArrayList;

public class EmailTable {
    private SQLiteDatabase db;
    private ArrayList<KasbonPegawaiModel> records;
    private String SQL;

    public EmailTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
    }

    private ContentValues SetValue(EmailModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("email_pengirim", Data.getAlamat_email());
        cv.put("password", Data.getPassword());
        return cv;
    }

    public void Insert(EmailModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("email", null, cv);
    }

    public void Update(EmailModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("email", cv, "_id = " + Data.getId(), null);
    }

    public EmailModel GetData(int Id){
        Cursor cr = this.db.rawQuery("SELECT _id, email_pengirim, password FROM email WHERE _id = "+Integer.toString(Id) , null);
        EmailModel Data = new EmailModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new EmailModel(
                    cr.getInt(cr.getColumnIndexOrThrow("_id")),
                    cr.getString(cr.getColumnIndexOrThrow("email_pengirim")),
                    cr.getString(cr.getColumnIndexOrThrow("password"))
            );
        }
        cr.close();
        return Data;
    }

}
