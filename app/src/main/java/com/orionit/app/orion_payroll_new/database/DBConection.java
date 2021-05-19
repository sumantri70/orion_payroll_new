package com.orionit.app.orion_payroll_new.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.PotonganModel;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;

import static com.orionit.app.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;

public class DBConection extends SQLiteOpenHelper{
    private Context ctx;
    public DBConection(Context context) {
        super(context, "db_orion_payroll.db", null, 1);
        ctx = context;
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
                "premi_perjam real, " +
                "no_telpon_darurat text)";
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
                "status text, " +
                "can_delete text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS master_potongan (" +
                "_id integer primary key, " +
                "kode text, " +
                "nama text, " +
                "keterangan text, " +
                "status text, " +
                "can_delete text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS kasbon_pegawai (" +
                "_id integer primary key, " +
                "nomor text, " +
                "tanggal integer, " +
                "id_pegawai integer, " +
                "jumlah real, " +
                "cicilan integer, " +
                "sisa real, " +
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

        Sql =  "CREATE TABLE IF NOT EXISTS password_aplikasi (" +
                "_id integer primary key, " +
                "password text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS email (" +
                "_id integer primary key, " +
                "email_pengirim text, " +
                "password text)";
        db.execSQL(Sql);

        Sql =  "CREATE TABLE IF NOT EXISTS setting_db (" +
                "accessKey text," +
                "accessSecret text, " +
                "password text)";
        db.execSQL(Sql);

        insert_tunjangan_system(db);
        insert_potongan_system(db);
//        if (CekTabelPassword(db) == false){
//            insert_passwor_default(db);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {  //untuk update database untuk penambahan baru

    }

    private void insert_tunjangan_system(SQLiteDatabase db){
        try {
            ContentValues cv;

            TunjanganModel Data = new TunjanganModel(
                    1,"TJ001",
                    "Insentif",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValueTunjangan(Data);
            db.insert("master_tunjangan", null, cv);

            TunjanganModel Data2 = new TunjanganModel(
                    2,"TJ002",
                    "Upah Lembur",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValueTunjangan(Data2);
            db.insert("master_tunjangan", null, cv);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void insert_potongan_system(SQLiteDatabase db){
        try {
            ContentValues cv;

            PotonganModel Data = new PotonganModel(
                    1,"PT001",
                    "Telat <= 15",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValuePotongan(Data);
            db.insert("master_potongan", null, cv);

            PotonganModel Data2 = new PotonganModel(
                    2,"PT002",
                    "Telat > 15",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValuePotongan(Data2);
            db.insert("master_potongan", null, cv);

            PotonganModel Data3 = new PotonganModel(
                    3,"PT003",
                    "Izin 1/2 Hari",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValuePotongan(Data3);
            db.insert("master_potongan", null, cv);

            PotonganModel Data4 = new PotonganModel(
                    4,"PT004",
                    "Izin Non Cuti",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValuePotongan(Data4);
            db.insert("master_potongan", null, cv);

            PotonganModel Data5 = new PotonganModel(
                    5,"PT005",
                    "Dokter",
                    "",
                    TRUE_STRING,
                    FALSE_STRING);
            cv = SetValuePotongan(Data5);
            db.insert("master_potongan", null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void insert_passwor_default(SQLiteDatabase db){
//        try {
//            ContentValues cv;
//            cv = SetValuePassword("");
//            db.insert("password_aplikasi", null, cv);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public boolean CekTabelPassword(SQLiteDatabase db){
//        Cursor cr = db.rawQuery("SELECT COUNT(*) as hasil FROM password_aplikasi", null);
//        cr.moveToFirst();
//        return cr.getInt(cr.getColumnIndexOrThrow("hasil")) > 0;
//    }

    private ContentValues SetValueTunjangan(TunjanganModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("kode", Data.getKode());
        cv.put("nama", Data.getNama());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("status", Data.getStatus());
        cv.put("can_delete", Data.getCan_delete());
        return cv;
    }

    private ContentValues SetValuePotongan(PotonganModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("kode", Data.getKode());
        cv.put("nama", Data.getNama());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("status", Data.getStatus());
        cv.put("can_delete", Data.getCan_delete());
        return cv;
    }

    private ContentValues SetValuePassword(String password) {
        ContentValues cv = new ContentValues();
        cv.put("password", password);
        return cv;
    }

    public String getDatabaseLocation() {
        String hasil = ctx.getDatabasePath("db_orion_payroll.db").toString();
        return hasil;
    }
}


