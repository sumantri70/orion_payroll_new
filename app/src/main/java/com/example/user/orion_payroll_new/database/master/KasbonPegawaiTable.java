package com.example.user.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.models.KasbonPegawaiModel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.user.orion_payroll_new.utility.FungsiGeneral.EndOfTheMonthLong;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.NumberToRomawi;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StartOfTheMonthLong;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrToIntDef;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getBulan;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTahun;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.padLeft;

public class KasbonPegawaiTable {

    private SQLiteDatabase db;
    private ArrayList<KasbonPegawaiModel> records;
    private String SQL;

    public KasbonPegawaiTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<KasbonPegawaiModel>();
    }

    private ContentValues SetValue(KasbonPegawaiModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("nomor", Data.getNomor());
        cv.put("tanggal", Data.getTanggal());
        cv.put("id_pegawai", Data.getId_pegawai());
        cv.put("jumlah", Data.getJumlah());
        cv.put("cicilan", Data.getCicilan());
        cv.put("sisa", Data.getSisa());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("user_id", Data.getUser_id());
        cv.put("tgl_input", Data.getTgl_input());
        cv.put("user_edit", Data.getUser_edit());
        cv.put("tgl_edit", Data.getTgl_edit());
        return cv;
    }

    public void ReloadList(Long tgl_dari, Long tgl_Sampai, String OrderBY) {
        String filter = "";
        filter += " AND tanggal BETWEEN "+Long.toString(tgl_dari)+" AND "+Long.toString(tgl_Sampai);

        if (filter.length() > 0) {
            filter = " WHERE " + filter.substring(4,filter.length());
        }

        if (OrderBY != "") {
            filter += " order by "+OrderBY+" ASC";
        }

        this.records.clear();

        Cursor cr = this.db.rawQuery("SELECT _id, nomor, tanggal, id_pegawai, jumlah, cicilan, sisa, keterangan, user_id, tgl_input, user_edit, tgl_edit FROM kasbon_pegawai " + filter, null);
        KasbonPegawaiModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new KasbonPegawaiModel(                        
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getLong(cr.getColumnIndexOrThrow("tanggal")),
                        cr.getString(cr.getColumnIndexOrThrow("nomor")),                        
                        cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                        cr.getDouble(cr.getColumnIndexOrThrow("jumlah")),
                        cr.getDouble(cr.getColumnIndexOrThrow("sisa")),
                        cr.getInt(cr.getColumnIndexOrThrow("cicilan")),                        
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("user_id")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_input")),
                        cr.getString(cr.getColumnIndexOrThrow("user_edit")),
                        cr.getLong(cr.getColumnIndexOrThrow("tgl_edit")));
                this.records.add(Data);
            } while (cr.moveToNext());
        }
        Data = new KasbonPegawaiModel(0,0,"",0,0.0,0.0,0,"","HIDE",0,"",0);
        this.records.add(Data);
    }

    public void Insert(KasbonPegawaiModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("kasbon_pegawai", null, cv);
    }

    public void Update(KasbonPegawaiModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("kasbon_pegawai", cv, "_id = " + Data.getId(), null);
    }

    public KasbonPegawaiModel GetData(int Id){
        Cursor cr = this.db.rawQuery("SELECT _id, nomor, tanggal, id_pegawai, jumlah, cicilan, sisa, keterangan, user_id, tgl_input, user_edit, tgl_edit FROM kasbon_pegawai WHERE _id = "+Integer.toString(Id) , null);
        KasbonPegawaiModel Data = new KasbonPegawaiModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new KasbonPegawaiModel(
                    cr.getInt(cr.getColumnIndexOrThrow("_id")),
                    cr.getLong(cr.getColumnIndexOrThrow("tanggal")),
                    cr.getString(cr.getColumnIndexOrThrow("nomor")),
                    cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                    cr.getDouble(cr.getColumnIndexOrThrow("jumlah")),
                    cr.getDouble(cr.getColumnIndexOrThrow("sisa")),
                    cr.getInt(cr.getColumnIndexOrThrow("cicilan")),
                    cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                    cr.getString(cr.getColumnIndexOrThrow("user_id")),
                    cr.getLong(cr.getColumnIndexOrThrow("tgl_input")),
                    cr.getString(cr.getColumnIndexOrThrow("user_edit")),
                    cr.getLong(cr.getColumnIndexOrThrow("tgl_edit")));
        }
        cr.close();
        return Data;
    }

    public String getNextNumber(Long Tanggal){
        String sql;
        Long tgl_awal = StartOfTheMonthLong(Tanggal);
        Long tgl_akhir = EndOfTheMonthLong(Tanggal);
        sql = "SELECT MAX(CAST(SUBSTR(nomor,4,4) AS INTEGER)) AS nomor FROM kasbon_pegawai WHERE tanggal BETWEEN " + tgl_awal + " AND " + tgl_akhir;
        Cursor cr = this.db.rawQuery(sql, null);

        String LastNumber ="";
        if (cr != null && cr.moveToFirst()) {
            LastNumber = cr.getString(cr.getColumnIndexOrThrow("nomor"));
            if (LastNumber == null){
                LastNumber = "1";
            }else{
                LastNumber = String.valueOf(StrToIntDef(LastNumber,0)+1);
            }
        }
        cr.close();
        String Hasil = padLeft(LastNumber,4,'0');

        Hasil = "KS/"+Hasil+"/"+NumberToRomawi(StrToIntDef(getBulan(Tanggal),0))+"/"+getTahun(Tanggal);
        return Hasil;
    }

    public void delete(long ID) {
        this.db.delete("kasbon_pegawai", "_id = " + ID, null);
    }

    public ArrayList<KasbonPegawaiModel> GetRecords() {
        return records;
    }

    public void SetRecords(ArrayList<KasbonPegawaiModel> records) {
        this.records = records;
    }

    public KasbonPegawaiModel GetDataByIndex(int index) {
        return this.records.get(index);
    }
}
