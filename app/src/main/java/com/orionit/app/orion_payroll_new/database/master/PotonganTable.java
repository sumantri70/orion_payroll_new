package com.orionit.app.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orionit.app.orion_payroll_new.database.DBConection;
import com.orionit.app.orion_payroll_new.models.PotonganModel;
import java.util.ArrayList;
import java.util.HashMap;

import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_DOKTER;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_IZIN_NON_CUTI;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_IZIN_STGH_HARI;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_TELAT_15;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_TELAT_LBH_15;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_TJ_INSENTIF;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_TJ_LEMBUR;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.FmtSqlStr;

public class PotonganTable {
    private SQLiteDatabase db;
    private ArrayList<PotonganModel> records;
    private String SQL;

    public PotonganTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<PotonganModel>();
    }

    public ArrayList<PotonganModel> GetRecords() {
        return records;
    }

    public void SetRecords(ArrayList<PotonganModel> records) {
        this.records = records;
    }

    public PotonganModel GetDataByIndex(int index) {
        return this.records.get(index);
    }

    private ContentValues SetValue(PotonganModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("kode", Data.getKode());
        cv.put("nama", Data.getNama());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("status", Data.getStatus());
        cv.put("can_delete", Data.getCan_delete());
        return cv;
    }

    public void ReloadList(String status, String OrderBY, boolean Is4Lov) {
        String filter = "";
        if (status != "") {
            filter += " AND status = '" + status + "'";
        }

        if (Is4Lov == true){
            filter += " AND _id NOT IN ("+ID_PT_TELAT_15 +","+ID_PT_TELAT_LBH_15+","+ID_PT_IZIN_STGH_HARI+","+ID_PT_IZIN_NON_CUTI+","+ID_PT_DOKTER+")";
        }

        if (OrderBY != "") {
            OrderBY = " order by "+OrderBY+" ASC";
        }

        if (filter.length() > 0) {
            filter = " where " + filter.substring(4, filter.length());
        }

        this.records.clear();

        Cursor cr = this.db.rawQuery("SELECT * FROM master_potongan " + filter + OrderBY, null);
        PotonganModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new PotonganModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("kode")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("status")),
                        cr.getString(cr.getColumnIndexOrThrow("can_delete"))
                );
                this.records.add(Data);
            } while (cr.moveToNext());
        }
        Data = new PotonganModel(0, "", "", "", "HIDE", "");
        this.records.add(Data);
    }


    public HashMap<String, PotonganModel> GetHash() {
        HashMap<String, PotonganModel> HashData = new HashMap<>();
        Cursor cr = this.db.rawQuery("SELECT * FROM master_potongan ", null);
        PotonganModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new PotonganModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("kode")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("status")),
                        cr.getString(cr.getColumnIndexOrThrow("can_delete"))
                );
                HashData.put(Integer.toString(Data.getId()), Data);
            } while (cr.moveToNext());
        }
        return HashData;
    }

    public void Insert(PotonganModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("master_potongan", null, cv);
    }

    public void Update(PotonganModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("master_Potongan", cv, "_id = " + Data.getId(), null);
    }

    public void aktivasi(long ID, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update("master_potongan", cv, "_id = " + ID, null);
    }

    public PotonganModel GetData(int Id){
        Cursor cr = this.db.rawQuery("SELECT _id, kode, nama, keterangan, status, can_delete FROM master_potongan WHERE _id = "+Integer.toString(Id) , null);
        PotonganModel Data = new PotonganModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new PotonganModel(
                    cr.getInt(cr.getColumnIndexOrThrow("_id")),
                    cr.getString(cr.getColumnIndexOrThrow("kode")),
                    cr.getString(cr.getColumnIndexOrThrow("nama")),
                    cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                    cr.getString(cr.getColumnIndexOrThrow("status")),
                    cr.getString(cr.getColumnIndexOrThrow("can_delete"))
            );
        }
        cr.close();
        return Data;
    }

    public boolean KodeExist(String kode, int Id){
        String Filter = "";

        if (Id != 0){
            Filter = " AND _id <> "+Integer.toString(Id);
        };

        SQL = "SELECT * FROM master_potongan WHERE kode = " + FmtSqlStr(kode) + Filter;
        Cursor cr = db.rawQuery(SQL,null);
        return cr.getCount() > 0;
    }

    public void delete(long ID) {
        this.db.delete("master_Potongan", "_id = " + ID, null);
    }
}