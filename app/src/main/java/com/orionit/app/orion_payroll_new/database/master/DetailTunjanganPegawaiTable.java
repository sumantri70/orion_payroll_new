package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.DetailTunjanganPegawaiModel;
import com.orionit.app.orion_payroll_new.models.PotonganModel;

import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.ID_TJ_INSENTIF;

public class DetailTunjanganPegawaiTable {
    private SQLiteDatabase db;
    private ArrayList<DetailTunjanganPegawaiModel> records;
    private String SQL;

    public DetailTunjanganPegawaiTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<DetailTunjanganPegawaiModel>();
    }

    public void Insert(DetailTunjanganPegawaiModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("detail_tunjangan_pegawai", null, cv);
    }

    public void Update(DetailTunjanganPegawaiModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("detail_tunjangan_pegawai", cv, "id_pegawai = " + Data.getId_pegawai(), null);
    }


    private ContentValues SetValue(DetailTunjanganPegawaiModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("id_pegawai", Data.getId_pegawai());
        cv.put("id_tunjangan", Data.getId_tunjangan());
        cv.put("jumlah", Data.getJumlah());
        return cv;
    }

    public void delete(long id_pegawai) {
        this.db.delete("detail_tunjangan_pegawai", "id_pegawai = " + id_pegawai, null);
    }

    public List<DetailTunjanganPegawaiModel> GetListData(int id_pegawai){
        Cursor cr = this.db.rawQuery("SELECT _id, id_pegawai, id_tunjangan, jumlah FROM detail_tunjangan_pegawai WHERE id_pegawai = "+Integer.toString(id_pegawai) , null);
        List<DetailTunjanganPegawaiModel> Datas = new ArrayList<DetailTunjanganPegawaiModel>();

        DetailTunjanganPegawaiModel Data = new DetailTunjanganPegawaiModel();
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new DetailTunjanganPegawaiModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                        cr.getInt(cr.getColumnIndexOrThrow("id_tunjangan")),
                        cr.getDouble(cr.getColumnIndexOrThrow("jumlah"))
                );
                Datas.add(Data);
            } while (cr.moveToNext());
        }
        return Datas;
    }

    public Double GetInsentifPegawai(int id_pegawai){
        Cursor cr = this.db.rawQuery("SELECT jumlah FROM detail_tunjangan_pegawai WHERE id_pegawai = "+Integer.toString(id_pegawai)+" AND id_tunjangan = "+String.valueOf(ID_TJ_INSENTIF), null);
        Double insentif = 0.0;
        if (cr != null && cr.moveToFirst()) {
            insentif = cr.getDouble(cr.getColumnIndexOrThrow("jumlah"));
        }
        cr.close();
        return insentif;
    }

    public ArrayList<DetailTunjanganPegawaiModel> GetRecords() {
        return records;
    }

    public void SetRecords(ArrayList<DetailTunjanganPegawaiModel> records) {
        this.records = records;
    }

    public DetailTunjanganPegawaiModel GetDataByIndex(int index) {
        return this.records.get(index);
    }


}
