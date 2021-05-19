package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.KasbonPegawaiModel;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;

import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_KASBON;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.EndOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.NumberToRomawi;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrToIntDef;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getBulan;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTahun;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.padLeft;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

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

    public void ReloadList(Long tgl_dari, Long tgl_Sampai, int id_pegawai, int Status, String OrderBY) {
        String filter = "";
        filter += " AND k.tanggal BETWEEN "+Long.toString(tgl_dari)+" AND "+Long.toString(tgl_Sampai);

        if (id_pegawai > 0){
            filter += " AND k.id_pegawai = "+String.valueOf(id_pegawai);
        }

        if (Status > 0){
            if (Status == 1){
                filter += " AND k.sisa = 0 ";
            }else if (Status == 2){
                filter += " AND k.sisa > 0 ";
            }
        }

        if (OrderBY != "") {
            filter += " order by "+OrderBY+" ASC";
        }

        this.records.clear();

        Cursor cr = this.db.rawQuery("SELECT k._id AS _id, k.nomor AS nomor, k.tanggal AS tanggal, k.id_pegawai AS id_pegawai, k.jumlah AS jumlah, " +
                                         "k.cicilan AS cicilan, k.sisa AS sisa, k.keterangan AS keterangan, k.user_id AS user_id, k.tgl_input AS tgl_input, " +
                                         "k.user_edit AS user_edit, k.tgl_edit AS tgl_edit, p.nama AS nama_pegawai " +
                                         "FROM kasbon_pegawai k, master_pegawai p " +
                                         "WHERE k.id_pegawai = p._id "+
                                          filter, null);
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
                Data.setNama_pegawai(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
                this.records.add(Data);
            } while (cr.moveToNext());
        }
        Data = new KasbonPegawaiModel(0,0,"",0,0.0,0.0,0,"","HIDE",0,"",0);
        this.records.add(Data);
    }

    public List<PenggajianDetailModel> Load4Penggajian(Long tanggal, int id_pegawai, int status, int id_penggajian) {
        List<PenggajianDetailModel> hasil = new ArrayList<>();
        String filter = "";
        if (tanggal != 0){
            filter += " AND tanggal <= "+Long.toString(tanggal);
        }

        if (id_pegawai != 0) {
            filter += " AND id_pegawai = "+ String.valueOf(id_pegawai);
        }

        if (status != 0) {
            if (status == 1){
                filter += " AND sisa > 0 ";
            }else if (status == 2){
                filter += " AND sisa <= 0 ";
            }
        }

        if (id_penggajian != 0){
            filter += " OR _id IN (SELECT id_tjg_pot_kas "+
                                  " FROM penggajian_detail "+
                                  " WHERE tipe = '" + TIPE_DET_KASBON + "' AND id_pegawai = "+String.valueOf(id_pegawai)+ ")";
        }

        if (filter.length() > 0) {
            filter = " WHERE " + filter.substring(4,filter.length());
        }

        String sql = "SELECT _id, nomor, tanggal, id_pegawai, jumlah, cicilan, sisa, keterangan, user_id, tgl_input, user_edit, tgl_edit "+
                     "FROM kasbon_pegawai "+ filter+ " ORDER BY nomor, tanggal";
        Cursor cr = this.db.rawQuery(sql, null);

        PenggajianDetailModel Data;
        hasil.clear();

        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(cr.getInt(cr.getColumnIndexOrThrow("_id")));
                Data.setNomor(cr.getString(cr.getColumnIndexOrThrow("nomor")));
                Data.setTanggal(cr.getLong(cr.getColumnIndexOrThrow("tanggal")));
                Data.setSisa(cr.getDouble(cr.getColumnIndexOrThrow("sisa")));
                Data.setLama_cicilan(cr.getInt(cr.getColumnIndexOrThrow("cicilan")));
                Data.setTotal(cr.getDouble(cr.getColumnIndexOrThrow("jumlah")));

                hasil.add(Data);
            } while (cr.moveToNext());
        }
        return hasil;
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

    public void UpdateSisa(int id, double jumlah, boolean AMenambah) {
        String operator = "";
        if (AMenambah == true){
            operator = "+";
        }else {
            operator = "-";
        }
        String sql = "UPDATE kasbon_pegawai SET sisa = sisa "+operator+String.valueOf(jumlah)+" WHERE _id = "+String.valueOf(id);
        this.db.execSQL(sql);
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

    public boolean IsSudahAdaPelunasan(int ID){
        String sql = "SELECT COUNT(*) AS jml FROM kasbon_pegawai WHERE jumlah <> sisa AND _id = "+String.valueOf(ID);
        Cursor cr = this.db.rawQuery(sql, null);

        int jml = 0;
        if (cr != null && cr.moveToFirst()) {
            jml = cr.getInt(cr.getColumnIndexOrThrow("jml"));
        }
        return jml > 0;
    }

    public  double GetSisaKasbon(int id_pegawai){
        String sql = "SELECT SUM(sisa) AS sisa FROM kasbon_pegawai WHERE id_pegawai = "+id_pegawai;
        Cursor cr = this.db.rawQuery(sql, null);

        double sisa = 0;
        if (cr != null && cr.moveToFirst()) {
            sisa = cr.getDouble(cr.getColumnIndexOrThrow("sisa"));
        }
        return sisa;
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
