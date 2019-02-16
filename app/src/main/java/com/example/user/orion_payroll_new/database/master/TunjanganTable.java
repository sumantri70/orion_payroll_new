package com.example.user.orion_payroll_new.database.master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.VolleyCallBack;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.ID_TJ_INSENTIF;
import static com.example.user.orion_payroll_new.models.JCons.ID_TJ_LEMBUR;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FmtSqlStr;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_TUNJANGAN;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_TUNJANGAN;

public class TunjanganTable {
    private SQLiteDatabase db;
    private ArrayList<TunjanganModel> records;
    private String SQL;

    public TunjanganTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<TunjanganModel>();
    }

    public ArrayList<TunjanganModel> GetRecords() {
        return records;
    }

    public void SetRecords(ArrayList<TunjanganModel> records) {
        this.records = records;
    }

    public TunjanganModel GetDataByIndex(int index) {
        return this.records.get(index);
    }

    private ContentValues SetValue(TunjanganModel Data) {
        ContentValues cv = new ContentValues();
        cv.put("kode", Data.getKode());
        cv.put("nama", Data.getNama());
        cv.put("keterangan", Data.getKeterangan());
        cv.put("status", Data.getStatus());
        return cv;
    }

    public void ReloadList(String status, String OrderBY) {
        String filter = "";
        if (status != "") {
            filter += " AND status = '" + status + "'";
        }

        if (OrderBY != "") {
            filter += " order by "+OrderBY+" ASC";
        }

        if (filter.length() > 0) {
            filter = " where " + filter.substring(4, filter.length());
        }

        this.records.clear();

        Cursor cr = this.db.rawQuery("SELECT * FROM master_tunjangan " + filter, null);
        TunjanganModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new TunjanganModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("kode")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("status"))
                );
                this.records.add(Data);
            } while (cr.moveToNext());
        }
        Data = new TunjanganModel(0, "", "", "", "HIDE");
        this.records.add(Data);
    }


    public HashMap<String, TunjanganModel> GetHash() {
        HashMap<String, TunjanganModel> HashData = new HashMap<>();
        Cursor cr = this.db.rawQuery("SELECT * FROM master_tunjangan ", null);
        TunjanganModel Data;
        if (cr != null && cr.moveToFirst()) {
            do {
                Data = new TunjanganModel(
                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
                        cr.getString(cr.getColumnIndexOrThrow("kode")),
                        cr.getString(cr.getColumnIndexOrThrow("nama")),
                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                        cr.getString(cr.getColumnIndexOrThrow("status"))
                );
                HashData.put(Integer.toString(Data.getId()), Data);
            } while (cr.moveToNext());
        }
        return HashData;
    }

    public void Insert(TunjanganModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.insert("master_tunjangan", null, cv);
    }

    public void Update(TunjanganModel Data) {
        ContentValues cv;
        cv = SetValue(Data);
        this.db.update("master_tunjangan", cv, "_id = " + Data.getId(), null);
    }

    public void aktivasi(long ID, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update("master_tunjangan", cv, "_id = " + ID, null);
    }

    public TunjanganModel GetData(int Id){
        Cursor cr = this.db.rawQuery("SELECT _id, kode, nama, keterangan, status FROM master_tunjangan WHERE _id = "+Integer.toString(Id) , null);
        TunjanganModel Data = new TunjanganModel();
        if (cr != null && cr.moveToFirst()) {
            Data = new TunjanganModel(
                    cr.getInt(cr.getColumnIndexOrThrow("_id")),
                    cr.getString(cr.getColumnIndexOrThrow("kode")),
                    cr.getString(cr.getColumnIndexOrThrow("nama")),
                    cr.getString(cr.getColumnIndexOrThrow("keterangan")),
                    cr.getString(cr.getColumnIndexOrThrow("status"))
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

        SQL = "SELECT * FROM master_tunjangan WHERE kode = " + FmtSqlStr(kode) + Filter;
        Cursor cr = db.rawQuery(SQL,null);
        return cr.getCount() > 0;
    }

    public void delete(long ID) {
        this.db.delete("master_tunjangan", "_id = " + ID, null);
    }
}