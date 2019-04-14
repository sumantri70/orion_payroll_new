package com.orionit.app.orion_payroll_new.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;

import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.FmtSqlStr;

public class EngineGeneral {
    private SQLiteDatabase db;
    private static String SQL = "";

    public EngineGeneral(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
    }


    public boolean KodeExist(String NamaTabel, String NamaField, String IsiField, int Id){
        String Filter = "";

        if (Id != 0){
            Filter = " AND id <> "+Integer.toString(Id);
        };

        SQL = "SELECT COUNT(*) FROM " + NamaTabel + " WHERE " + NamaField + "=" + FmtSqlStr(IsiField) + Filter;
        Cursor cr = db.rawQuery(SQL,null);
        return cr != null;
    }


}
