package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;

import java.util.ArrayList;
import java.util.List;

public class PenggajianDetailTable {
    private SQLiteDatabase db;
    private String SQL;

    public PenggajianDetailTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
    }

    private ContentValues SetValue(PenggajianDetailModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("id_master", Data.getId_master());
        cv.put("id_tjg_pot_kas", Data.getId_tjg_pot_kas());
        cv.put("tipe", Data.getTipe());
        cv.put("jumlah", Data.getJumlah());
        return cv;
    }

    public void Insert(PenggajianDetailModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("penggajian_detail", null, cv);
    }

    public void Update(PenggajianDetailModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("penggajian_detail", cv, "_id = " + Data.getId(), null);
    }

    public void delete(long id_master) {
        this.db.delete("penggajian_detail", "id_master = " + id_master, null);
    }

    public List <PenggajianDetailModel> GetArrList(int id_master){
        List <PenggajianDetailModel> Arr = new ArrayList<>();
        String sql = "SELECT _id, id_master, id_tjg_pot_kas, tipe, jumlah "+
                     "FROM penggajian_detail WHERE id_master = "+String.valueOf(id_master);

        Cursor cr = this.db.rawQuery(sql, null);
        PenggajianDetailModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new PenggajianDetailModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getInt(cr.getColumnIndexOrThrow("id_master")),
                        cr.getInt(cr.getColumnIndexOrThrow("id_tjg_pot_kas")),
                        cr.getString(cr.getColumnIndexOrThrow("tipe")),
                        cr.getDouble(cr.getColumnIndexOrThrow("jumlah")));
                Arr.add(Data);
            } while (cr.moveToNext());
        }
        return Arr;
    };
}


