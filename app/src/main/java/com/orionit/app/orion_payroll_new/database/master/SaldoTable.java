package com.orionit.app.orion_payroll_new.database.master;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModelChild;
import com.orionit.app.orion_payroll_new.models.SaldoKasbonModel;

import java.util.ArrayList;

public class SaldoTable {
    private SQLiteDatabase db;
    private String SQL;

    public SaldoTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
    }

    public ArrayList<SaldoKasbonModel> get_list_saldo_kasbon(long tanggal, int id_pegawai, boolean IsSaldoAwal){
        ArrayList <SaldoKasbonModel> Hasil;
        Hasil = new ArrayList <SaldoKasbonModel>();

        String Filter = "";
        if (IsSaldoAwal){
            Filter += " WHERE tanggal < "+Long.toString(tanggal);
        }else{
            Filter += " WHERE tanggal <= "+Long.toString(tanggal);
        }

        if (id_pegawai > 0){
            Filter += " AND id_pegawai = "+String.valueOf(id_pegawai);
        }

        SQL = "SELECT id_pegawai, SUM(jumlah) as jumlah FROM ("+
                "SELECT id_pegawai as id_pegawai, jumlah as jumlah "+
                "FROM kasbon_pegawai " + Filter +
                " UNION ALL " +
                "SELECT id_pegawai as id_pegawai, -total_kasbon as jumlah "+
                "FROM penggajian_master " + Filter +
                " AND total_kasbon > 0 " +
              ") GROUP by id_pegawai";

        Cursor cr = this.db.rawQuery(SQL, null);
        SaldoKasbonModel Data;

        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new SaldoKasbonModel(
                        cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                        cr.getDouble(cr.getColumnIndexOrThrow("jumlah"))
                );
                Hasil.add(Data);
            } while (cr.moveToNext());
        }

        return Hasil;
    }

    public ArrayList<LaporanKasbonModelChild> get_laporan_kasbon(long tgl_dari, long tgl_Sampai, int id_pegawai){
        ArrayList <LaporanKasbonModelChild> Hasil;
        Hasil = new ArrayList <LaporanKasbonModelChild>();

        String Filter = "";

        if ((tgl_dari > 0) || (tgl_Sampai > 0)){
            Filter += " WHERE tanggal BETWEEN "+Long.toString(tgl_dari)+" AND "+Long.toString(tgl_Sampai);
        }

        if (id_pegawai > 0){
            Filter += " AND id_pegawai = "+String.valueOf(id_pegawai);
        }

        SQL = "SELECT id_pegawai, nomor, tipe, tanggal, SUM(jumlah) as jumlah, urutan FROM ("+
                "SELECT id_pegawai as id_pegawai, nomor, 'Pinjam' as tipe, tanggal, jumlah as jumlah, 1 AS urutan "+
                "FROM kasbon_pegawai " + Filter +
                " UNION ALL " +
                "SELECT id_pegawai as id_pegawai, nomor, 'Bayar' as tipe, tanggal, total_kasbon as jumlah, 2 AS urutan "+
                "FROM penggajian_master " + Filter +
                " AND total_kasbon > 0 " +
                ") GROUP by id_pegawai, nomor, tanggal, tipe ORDER BY id_pegawai, tanggal, urutan, nomor";

        Cursor cr = this.db.rawQuery(SQL, null);
        LaporanKasbonModelChild Data;

        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new LaporanKasbonModelChild(
                        cr.getInt(cr.getColumnIndexOrThrow("id_pegawai")),
                        cr.getString(cr.getColumnIndexOrThrow("nomor")),
                        cr.getDouble(cr.getColumnIndexOrThrow("jumlah")),
                        cr.getString(cr.getColumnIndexOrThrow("tipe")),
                        cr.getLong(cr.getColumnIndexOrThrow("tanggal"))
                );
                Hasil.add(Data);
            } while (cr.moveToNext());
        }
        return Hasil;
    }
}
