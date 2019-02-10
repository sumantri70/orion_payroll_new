package com.example.user.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.models.PenggajianModel;

import java.util.ArrayList;

public class PenggajianTable {
    private SQLiteDatabase db;
    private ArrayList<PenggajianModel> records;
    private String SQL;

    public PenggajianTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<PenggajianModel>();
    }

    private ContentValues SetValue(PenggajianModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("nomor", Data.getNomor());
        cv.put("tanggal", Data.getTanggal());
        cv.put("periode", Data.getPeriode());
        cv.put("id_pegawai", Data.getId_pegawai());
        cv.put("gaji_pokok", Data.getGaji_pokok());
        cv.put("uang_ikatan", Data.getUang_ikatan());
        cv.put("uang_kehadiran", Data.getUang_kehadiran());
        cv.put("premi_harian", Data.getPremi_harian());
        cv.put("premi_perjam", Data.getPremi_perjam());
        cv.put("telat_satu", Data.getTelat_satu());
        cv.put("telat_dua", Data.getTelat_dua());
        cv.put("dokter", Data.getDokter());
        cv.put("izin_stgh_hari", Data.getIzin_stgh_hari());
        cv.put("izin_non_cuti", Data.getIzin_non_cuti());
        cv.put("izin_cuti", Data.getIzin_cuti());
        cv.put("jam_lembur", Data.getJam_lembur());
        cv.put("total_tunjangan", Data.getTotal_tunjangan());
        cv.put("total_potongan", Data.getTotal_potongan());
        cv.put("total_lembur", Data.getTotal_lembur());
        cv.put("total_kasbon", Data.getTotal_kasbon());
        cv.put("total", Data.getTotal());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("user_id", Data.getUser_id());
        cv.put("tgl_input", Data.getTgl_input());
        cv.put("user_edit", Data.getUser_edit());
        cv.put("tgl_edit", Data.getTgl_edit());
        return cv;
    }

    public void ReloadList(String status, String OrderBY) {
        String filter = "";
        if (status != "") {
            filter += " where status = '" + status + "'";
        }

        if (OrderBY != "") {
            filter += " order by "+OrderBY+" ASC";
        }

        if (filter.length() > 0) {
            filter = " where " + filter.substring(4,filter.length());
        }

        this.records.clear();

        SQL = "SELECT _id, nomor, tanggal, periode, id_pegawai, gaji_pokok, uang_ikatan, uang_kehadiran, premi_harian, premi_perjam, telat_satu, telat_dua," +
              "dokter, izin_stgh_hari, izin_non_cuti, izin_cuti, jam_lembur, total_tunjangan, total_potongan, total_lembur, total_kasbon, total," +
              "keterangan, user_id, tgl_input, user_edit, tgl_edit "+
              "FROM penggajian_master " + filter;
        Cursor cr = this.db.rawQuery(SQL, null);

        PenggajianModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new PenggajianModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                        cr.getInt(cr.getColumnIndexOrThrow("telat_satu")),
                        cr.getInt(cr.getColumnIndexOrThrow("telat_dua")),
                        cr.getInt(cr.getColumnIndexOrThrow("dokter")),
                        cr.getInt(cr.getColumnIndexOrThrow("izin_stgh_hari")),
                        cr.getInt(cr.getColumnIndexOrThrow("izin_cuti")),
                        cr.getInt(cr.getColumnIndexOrThrow("izin_non_cuti")),
                        cr.getString(cr.getColumnIndexOrThrow("nomor")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("user_id")),
                        cr.getString(cr.getColumnIndexOrThrow("user_edit")),
                        cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokok")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_ikatan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("uang_kehadiran")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_harian")),
                        cr.getDouble(cr.getColumnIndexOrThrow("premi_perjam")),
                        cr.getDouble(cr.getColumnIndexOrThrow("jam_lembur")),
                        cr.getDouble(cr.getColumnIndexOrThrow("total_tunjangan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("total_potongan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("total_lembur")),
                        cr.getDouble(cr.getColumnIndexOrThrow("total_kasbon")),
                        cr.getDouble(cr.getColumnIndexOrThrow("total")),
                        cr.getLong(cr.getColumnIndexOrThrow("tanggal")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_input")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_edit")),
                        cr.getLong(cr.getColumnIndexOrThrow("periode"))
                );
                this.records.add(Data);
            } while (cr.moveToNext());
        }
        Data = new PenggajianModel(0, 0,0,0,0,0,0,0,"","","HIDE","",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0);
        this.records.add(Data);
    }


    public void Insert(PenggajianModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("penggajian_master", null, cv);
    }

    public void Update(PenggajianModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("penggajian_master", cv, "_id = " + Data.getId(), null);
    }

    public PenggajianModel GetData(int Id){
        Cursor cr = this.db.rawQuery("SELECT _id, kode, nama, keterangan, status FROM penggajian_master WHERE _id = "+Integer.toString(Id) , null);
        PenggajianModel Data = new PenggajianModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new PenggajianModel(
                    cr.getInt(cr.getColumnIndexOrThrow("_id")),
                    cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                    cr.getInt(cr.getColumnIndexOrThrow("telat_satu")),
                    cr.getInt(cr.getColumnIndexOrThrow("telat_dua")),
                    cr.getInt(cr.getColumnIndexOrThrow("dokter")),
                    cr.getInt(cr.getColumnIndexOrThrow("izin_stgh_hari")),
                    cr.getInt(cr.getColumnIndexOrThrow("izin_cuti")),
                    cr.getInt(cr.getColumnIndexOrThrow("izin_non_cuti")),
                    cr.getString(cr.getColumnIndexOrThrow("nomor")),
                    cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                    cr.getString(cr.getColumnIndexOrThrow("user_id")),
                    cr.getString(cr.getColumnIndexOrThrow("user_edit")),
                    cr.getDouble(cr.getColumnIndexOrThrow("gaji_pokok")),
                    cr.getDouble(cr.getColumnIndexOrThrow("uang_ikatan")),
                    cr.getDouble(cr.getColumnIndexOrThrow("uang_kehadiran")),
                    cr.getDouble(cr.getColumnIndexOrThrow("premi_harian")),
                    cr.getDouble(cr.getColumnIndexOrThrow("premi_perjam")),
                    cr.getDouble(cr.getColumnIndexOrThrow("jam_lembur")),
                    cr.getDouble(cr.getColumnIndexOrThrow("total_tunjangan")),
                    cr.getDouble(cr.getColumnIndexOrThrow("total_potongan")),
                    cr.getDouble(cr.getColumnIndexOrThrow("total_lembur")),
                    cr.getDouble(cr.getColumnIndexOrThrow("total_kasbon")),
                    cr.getDouble(cr.getColumnIndexOrThrow("total")),
                    cr.getLong(cr.getColumnIndexOrThrow("tanggal")),
                    cr.getLong(cr.getColumnIndexOrThrow("tgl_input")),
                    cr.getLong(cr.getColumnIndexOrThrow("tgl_edit")),
                    cr.getLong(cr.getColumnIndexOrThrow("periode"))
            );
        }
        cr.close();
        return Data;
    }

    public void delete(long ID) {
        this.db.delete("penggajian_master", "_id = " + ID, null);
    }
    
    

    public ArrayList<PenggajianModel> GetRecords() {
        return records;
    }

    public void SetRecords(ArrayList<PenggajianModel> records) {
        this.records = records;
    }

    public PenggajianModel GetDataByIndex(int index) {
        return this.records.get(index);
    }
}
