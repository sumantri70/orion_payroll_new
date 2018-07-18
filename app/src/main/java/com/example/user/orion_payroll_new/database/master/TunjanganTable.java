package com.example.user.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;

import java.util.ArrayList;

public class TunjanganTable {
    private SQLiteDatabase db;
    private ArrayList<TunjanganModel> records;
    private String Fstatus = "";

    public TunjanganTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<TunjanganModel>();
        this.ReloadList(Fstatus);
    }

    public void ReloadList(String status){
        String filter = "";
        if (status != ""){
            filter += " where status = '" + status + "'";
        };

        this.records.clear();

        Cursor cr = this.db.rawQuery("SELECT * FROM master_tunjangan "+ filter ,null);
        TunjanganModel Data;
        if (cr != null && cr.moveToFirst()){
            do {
                Data = new TunjanganModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("kode")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("status"))
                );
                this.records.add(Data);
            }while (cr.moveToNext());
        }
        Data = new TunjanganModel(0,"","","","");
        this.records.add(Data);
    }

    private ContentValues SetValue (TunjanganModel Data ){
        ContentValues cv = new ContentValues();
        cv.put("kode", Data.getKode());
        cv.put("nama", Data.getNama());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("status", Data.getStatus());
        return cv;
    }

    public void Insert(TunjanganModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("master_Tunjangan",null,cv);
        this.ReloadList(Fstatus);
    }

    public void Update(TunjanganModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("master_Tunjangan",cv,"_id = "+Data.getId(),null);
        this.ReloadList(Fstatus);
    }

    public void delete(long ID){
        this.db.delete("master_Tunjangan", "_id = " + ID, null);
        this.ReloadList(Fstatus);
    }

    public void aktivasi(long ID, String status){
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update("master_Tunjangan", cv,"_id = "+ID,null);
        this.ReloadList(Fstatus);
    }

    public ArrayList<TunjanganModel> GetRecords(){
        return records;
    }

    public void setStatus(String Fstatus) {
        this.Fstatus = Fstatus;
    }

    public void SetRecords(ArrayList<TunjanganModel> records) {
        this.records = records;
    }

    public TunjanganModel GetDataKaryawanByIndex(int index) {
        return this.records.get(index);
    }

}
