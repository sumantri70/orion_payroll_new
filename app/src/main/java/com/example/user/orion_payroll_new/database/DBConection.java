package com.example.user.orion_payroll_new.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConection extends SQLiteOpenHelper{
    public DBConection(Context context) {
        super(context, "db_orion_payroll.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Sql;

        Sql =  "CREATE TABLE IF NOT EXISTS master_pegawai (" +
                "_id integer primary key, " +  //id pakai _ biar auto nambah
                "nik text, " +
                "nama text, " +
                "alamat text, " +
                "telpon1 text, " +
                "telpon2 text, " +
                "email text, " +
                "tgl_lahir integer, " +
                "tgl_mulai_kerja integer, " +
                "gaji_pokok real, " +
                "status text, " +
                "keterangan text, " +
                "uang_ikatan real, " +
                "uang_kehadiran real, " +
                "premi_harian real, " +
                "premi_perjam real)";
        db.execSQL(Sql);


        Sql =  "CREATE TABLE IF NOT EXISTS detail_tunjangan_pegawai (" +
                "_id integer primary key, " +
                "id_pegawai integer, " +
                "id_tunjangan integer, " +
                "jumlah real)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS histori_gaji_pegawai (" +
                "_id integer primary key, " +
                "id_pegawai integer, " +
                "tanggal integer, " +
                "gaji_pokok real, " +
                "uang_ikatan real, " +
                "uang_kehadiran real, " +
                "premi_harian real, " +
                "premi_perjam real)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS master_tunjangan (" +
                "_id integer primary key, " +
                "kode text, " +
                "nama text, " +
                "keterangan text, " +
                "status text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS master_potongan (" +
                "_id integer primary key, " +
                "kode text, " +
                "nama text, " +
                "keterangan text, " +
                "status text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS kasbon_pegawai (" +
                "_id integer primary key, " +
                "nomor text, " +
                "tanggal integer, " +
                "id_pegawai integer, " +
                "jumlah integer, " +
                "cicilan integer, " +
                "sisa integer, " +
                "keterangan text, " +
                "user_id text, " +
                "tgl_input integer, " +
                "user_edit text, " +
                "tgl_edit integer)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS penggajian_master (" +
                "_id integer primary key, " +
                "nomor text, " +
                "tanggal integer, " +
                "periode integer, " +
                "id_pegawai integer, " +
                "gaji_pokok real, "+
                "uang_ikatan real, "+
                "uang_kehadiran real, "+
                "premi_harian real, "+
                "premi_perjam real, "+
                "telat_satu integer, " +
                "telat_dua integer, " +
                "dokter integer, " +
                "izin_stgh_hari integer, " +
                "izin_non_cuti integer, " +
                "izin_cuti integer, " +
                "jam_lembur real, " +
                "total_tunjangan real, "+
                "total_potongan real, "+
                "total_lembur real, "+
                "total_kasbon real, "+
                "total real, "+
                "keterangan text, " +
                "user_id text, " +
                "tgl_input integer, " +
                "user_edit text, " +
                "tgl_edit integer)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE penggajian_detail (" +
                "_id integer primary key, " +
                "id_master integer, " +
                "id_tjg_pot_kas integer, " +
                "tipe text, " +
                "jumlah real)";
        db.execSQL(Sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {  //untuk update database untuk penambahan baru

    }
}
