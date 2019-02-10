package com.example.user.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.models.HistoriGajiPegawaiModel;
import com.example.user.orion_payroll_new.models.PotonganModel;

import java.util.ArrayList;

public class HistoriGajiPegawaiTable {
    private SQLiteDatabase db;
    private ArrayList<HistoriGajiPegawaiModel> records;
    private String SQL;

    public HistoriGajiPegawaiTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<HistoriGajiPegawaiModel>();
    }

    public ArrayList<HistoriGajiPegawaiModel> GetRecords() {
        return records;
    }

    public void SetRecords(ArrayList<HistoriGajiPegawaiModel> records) {
        this.records = records;
    }

    public HistoriGajiPegawaiModel GetDataByIndex(int index) {
        return this.records.get(index);
    }

    private ContentValues SetValue(HistoriGajiPegawaiModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("_id", Data.getId());
        cv.put("id_pegawai", Data.getId_pegawai());
        cv.put("tanggal", Data.getTanggal());
        cv.put("gaji_pokok", Data.getGaji_pokok());
        cv.put("uang_ikatan", Data.getUang_ikatan());
        cv.put("uang_kehadiran", Data.getUang_kehadiran());
        cv.put("premi_harian", Data.getPremi_harian());
        cv.put("premi_perjam", Data.getPremi_perjam());
        return cv;
    }

    public void Insert(HistoriGajiPegawaiModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("histori_gaji_pegawai",null,cv);
    }

    public void Update(HistoriGajiPegawaiModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("histori_gaji_pegawai",cv,"id_pegawai = "+Data.getId(),null);
    }

    public void delete(long ID){
        this.db.delete("histori_gaji_pegawai", "id_pegawai = " + ID, null);
    }


    public void ReloadList(int id_pegawai) {
        String filter = "";

        if (id_pegawai != 0){
            filter = " AND id_pegawai = "+Integer.toString(id_pegawai);
        };


        if (filter.length() > 0) {
            filter = " where " + filter.substring(4,filter.length());
        }

        this.records.clear();
        Cursor cr = this.db.rawQuery("SELECT * FROM histori_gaji_pegawai " + filter, null);
        HistoriGajiPegawaiModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new HistoriGajiPegawaiModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                        cr.getLong(cr.getColumnIndexOrThrow("tanggal")),
                        cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokok")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_ikatan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_kehadiran")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_harian")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_perjam"))
                );
                this.records.add(Data);
            } while (cr.moveToNext());
        }
    }






}
