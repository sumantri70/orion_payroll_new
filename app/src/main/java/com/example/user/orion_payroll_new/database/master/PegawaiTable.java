package com.example.user.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.models.PegawaiModel;

import java.util.ArrayList;
import java.util.Optional;

public class PegawaiTable {
    private SQLiteDatabase db;
    private ArrayList<PegawaiModel> records;
    private String Fstatus = "";
    private String OrderBY = "";
    private String SQL = "";

    public PegawaiTable(Context context, String Status, String OrderBY) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<PegawaiModel>();
        this.Fstatus = Status;
        this.OrderBY = OrderBY;
        this.ReloadList(Fstatus, OrderBY);
    }

    public void ReloadList(String status, String  OrderBy){
        String filter = "";
        if (status != ""){
            filter = " AND status = '" + status + "'";
        };

        if (OrderBy != ""){
            OrderBy = " ORDER BY "+ OrderBy;        };


        if (filter.length() > 0) {
            filter = " where " + filter.substring(4,filter.length());
        }

        SQL = "SELECT * FROM master_pegawai "+ filter + OrderBy;
        Log.w("aaa", SQL);

        this.records.clear();
        Cursor cr = this.db.rawQuery(SQL,null);
        PegawaiModel Data;
        if (cr != null && cr.moveToFirst()){
            do {
                Data = new PegawaiModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("nik")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("alamat")),
                        cr.getString(cr.getColumnIndexOrThrow("telpon1")),
                        cr.getString(cr.getColumnIndexOrThrow("telpon2")),
                        cr.getString(cr.getColumnIndexOrThrow("email")),
                        cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokokl")),
                        cr.getString(cr.getColumnIndexOrThrow("status")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_lahir"))

                );
                this.records.add(Data);
            }while (cr.moveToNext());
        }
        Data = new PegawaiModel(0,"","","","","","",0.0,"", 0);
        this.records.add(Data);
    }

    private ContentValues SetValue (PegawaiModel Data ){
        ContentValues cv = new ContentValues();
        cv.put("nik", Data.getNik());
        cv.put("nama", Data.getNama());
        cv.put("alamat", Data.getAlamat());
        cv.put("telpon1", Data.getTelpon1());
        cv.put("telpon2", Data.getTelpon2());
        cv.put("email", Data.getEmail());
        cv.put("gaji_pokokl", Data.getGaji_pokokl());
        cv.put("status", Data.getStatus());
        cv.put("tgl_lahir", Data.getTgl_lahir());
        return cv;
    }

    public void Insert(PegawaiModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("master_pegawai",null,cv);
        this.ReloadList(Fstatus, OrderBY);
    }

    public void Update(PegawaiModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("master_pegawai",cv,"_id = "+Data.getId(),null);
        this.ReloadList(Fstatus, OrderBY);
    }

    public void delete(long ID){
        this.db.delete("master_pegawai", "_id = " + ID, null);
        this.ReloadList(Fstatus, OrderBY);
    }

    public void aktivasi(long ID, String status){
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update("master_pegawai", cv,"_id = "+ID,null);
        this.ReloadList(Fstatus, OrderBY);
    }

    public ArrayList<PegawaiModel> GetRecords(){
        return records;
    }

    public void setStatus(String Fstatus) {
        this.Fstatus = Fstatus;
    }

    public void SetRecords(ArrayList<PegawaiModel> records) {
        this.records = records;
    }

    public PegawaiModel GetDataKaryawanByIndex(int index) {
        return this.records.get(index);
    }
}
