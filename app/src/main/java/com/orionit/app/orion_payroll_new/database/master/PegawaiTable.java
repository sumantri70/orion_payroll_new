package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.PegawaiModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.FmtSqlStr;

public class PegawaiTable {
    private SQLiteDatabase db;
    private ArrayList<PegawaiModel> records;
    private String SQL = "";

    public PegawaiTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<PegawaiModel>();
    }

    public void ReloadList(String status, String  OrderBy){
        String filter = "";
        if (status != ""){
            filter = " AND status = '" + status + "'";
        }

        if (OrderBy != "") {
            OrderBy = " order by "+OrderBy+" ASC";
        }

        if (filter.length() > 0) {
            filter = " where " + filter.substring(4,filter.length());
        }

        SQL = "SELECT * FROM master_pegawai "+ filter + OrderBy;
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
                        cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokok")),
                        cr.getString(cr.getColumnIndexOrThrow("status")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_lahir")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_mulai_kerja")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_ikatan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_kehadiran")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_harian")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_perjam")),
                        cr.getString(cr.getColumnIndexOrThrow("no_telpon_darurat")));

                this.records.add(Data);
            }while (cr.moveToNext());
        }
        Data = new PegawaiModel(0,"","","","","","",0.0,"HIDE", 0,0,"",0.0,0.0,0.0,0.0, "");
        this.records.add(Data);
    }

    public PegawaiModel GetData(int Id){
        Cursor cr = this.db.rawQuery("SELECT _id, nik, nama, alamat, telpon1, telpon2, email, gaji_pokok, status, tgl_lahir, tgl_mulai_kerja, keterangan, uang_ikatan, uang_kehadiran, premi_harian, premi_perjam, no_telpon_darurat "+
                                     "FROM master_pegawai WHERE _id = "+Integer.toString(Id) , null);
        PegawaiModel Data = new PegawaiModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new PegawaiModel(
                    cr.getInt(cr.getColumnIndexOrThrow("_id")),
                    cr.getString(cr.getColumnIndexOrThrow("nik")),
                    cr.getString(cr.getColumnIndexOrThrow("nama")),
                    cr.getString(cr.getColumnIndexOrThrow("alamat")),
                    cr.getString(cr.getColumnIndexOrThrow("telpon1")),
                    cr.getString(cr.getColumnIndexOrThrow("telpon2")),
                    cr.getString(cr.getColumnIndexOrThrow("email")),
                    cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokok")),
                    cr.getString(cr.getColumnIndexOrThrow("status")),
                    cr.getLong(cr.getColumnIndexOrThrow("tgl_lahir")),
                    cr.getLong(cr.getColumnIndexOrThrow("tgl_mulai_kerja")),
                    cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                    cr.getDouble(cr.getColumnIndexOrThrow("uang_ikatan")),
                    cr.getDouble(cr.getColumnIndexOrThrow("uang_kehadiran")),
                    cr.getDouble(cr.getColumnIndexOrThrow("premi_harian")),
                    cr.getDouble(cr.getColumnIndexOrThrow("premi_perjam")),
                    cr.getString(cr.getColumnIndexOrThrow("no_telpon_darurat"))
            );
        }
        cr.close();
        return Data;
    }


    private ContentValues SetValue (PegawaiModel Data ){
        ContentValues cv = new ContentValues();
        cv.put("nik", Data.getNik());
        cv.put("nama", Data.getNama());
        cv.put("alamat", Data.getAlamat());
        cv.put("telpon1", Data.getTelpon1());
        cv.put("telpon2", Data.getTelpon2());
        cv.put("email", Data.getEmail());
        cv.put("gaji_pokok", Data.getGaji_pokok());
        cv.put("status", Data.getStatus());
        cv.put("tgl_lahir", Data.getTgl_lahir());
        cv.put("tgl_mulai_kerja", Data.getTgl_mulai_kerja());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("uang_ikatan", Data.getUang_ikatan());
        cv.put("uang_kehadiran", Data.getUang_kehadiran());
        cv.put("premi_harian", Data.getPremi_harian());
        cv.put("premi_perjam", Data.getPremi_perjam());
        cv.put("no_telpon_darurat", Data.getNo_telpon_darurat());
        return cv;
    }

    public HashMap<String, PegawaiModel> GetHash() {
        HashMap<String, PegawaiModel> HashData = new HashMap<>();
        Cursor cr = this.db.rawQuery("SELECT * FROM master_pegawai ", null);
        PegawaiModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new PegawaiModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("nik")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("alamat")),
                        cr.getString(cr.getColumnIndexOrThrow("telpon1")),
                        cr.getString(cr.getColumnIndexOrThrow("telpon2")),
                        cr.getString(cr.getColumnIndexOrThrow("email")),
                        cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokok")),
                        cr.getString(cr.getColumnIndexOrThrow("status")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_lahir")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_mulai_kerja")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_ikatan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_kehadiran")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_harian")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_perjam")),
                        cr.getString(cr.getColumnIndexOrThrow("no_telpon_darurat"))
                );
                HashData.put(Integer.toString(Data.getId()), Data);
            } while (cr.moveToNext());
        }
        return HashData;
    }

    public Long Insert(PegawaiModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        return this.db.insert("master_pegawai",null,cv);
    }

    public void Update(PegawaiModel Data){
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("master_pegawai",cv,"_id = "+Data.getId(),null);
    }

    public void delete(long ID){
        this.db.delete("master_pegawai", "_id = " + ID, null);
    }

    public void aktivasi(long ID, String status){
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update("master_pegawai", cv,"_id = "+ID,null);
    }

    public boolean KodeExist(String kode, int Id){
        String Filter = "";

        if (Id != 0){
            Filter = " AND _id <> "+Integer.toString(Id);
        };

        SQL = "SELECT * FROM master_pegawai WHERE nik = " + FmtSqlStr(kode) + Filter;
        Cursor cr = db.rawQuery(SQL,null);
        return cr.getCount() > 0;
    }


    public ArrayList<Integer> get_list_id_pegawai(String status, int id_pegawai){
        ArrayList<Integer> hasil = new ArrayList<Integer>();

        String filter = "";
        if (status != ""){
            filter = " AND status = '" + status + "'";
        }

        if (id_pegawai > 0){
            filter = " AND _id = "+String.valueOf(id_pegawai);
        }

        if (filter.length() > 0) {
            filter = " where " + filter.substring(4,filter.length());
        }

        SQL = "SELECT _id FROM master_pegawai "+ filter;
        this.records.clear();
        Cursor cr = this.db.rawQuery(SQL,null);

        if (cr != null && cr.moveToFirst()){
            do {
                hasil.add(cr.getInt(cr.getColumnIndexOrThrow("_id")));
            }while (cr.moveToNext());
        }

        return hasil;
    }

    public ArrayList<PegawaiModel> GetRecords(){
        return records;
    }

    public void SetRecords(ArrayList<PegawaiModel> records) {
        this.records = records;
    }

    public PegawaiModel GetDataKaryawanByIndex(int index) {
        return this.records.get(index);
    }
}
