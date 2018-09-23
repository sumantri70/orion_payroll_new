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
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.VolleyCallBack;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_TUNJANGAN;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_TUNJANGAN;

public class TunjanganTable {
    private SQLiteDatabase db;
    private ArrayList<TunjanganModel> records;
    private TunjanganModel Tunjangan;
    private String Fstatus = "";
    private String order_by = "";

    public TunjanganTable(Context context) {
        this.db = new DBConection(context).getWritableDatabase();
        this.records = new ArrayList<TunjanganModel>();
        this.ReloadList(Fstatus, order_by);
        this.Tunjangan = new TunjanganModel(0,"","","","");
        Log.d("masuk costruktor",Tunjangan.getNama());
    }

    public void Insert(final TunjanganModel Data){
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_TUNJANGAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", String.valueOf(Data.getKode()));
                params.put("nama", String.valueOf(Data.getNama()));
                params.put("keterangan", String.valueOf(Data.getKeterangan()));
                params.put("status", String.valueOf(Data.getStatus()));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
    }

    public void Update(final TunjanganModel Data){
        Log.d("urlll",URL_UPDATE_TUNJANGAN);
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_TUNJANGAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("data",String.valueOf(Data.getId()));
                Log.d("data",String.valueOf(Data.getKode()));
                Log.d("data",String.valueOf(Data.getNama()));
                Log.d("data",String.valueOf(Data.getKeterangan()));
                Log.d("data",String.valueOf(Data.getStatus()));

                params.put("id", String.valueOf(Data.getId()));
                params.put("kode", String.valueOf(Data.getKode()));
                params.put("nama", String.valueOf(Data.getNama()));
                params.put("keterangan", String.valueOf(Data.getKeterangan()));
                params.put("status", String.valueOf(Data.getStatus()));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
        this.ReloadList(Fstatus, order_by);
    }

    public void ReloadList(final String status, final String order_by){
        records.clear();
        String filter;
        filter = "?status="+status+"&order_by="+order_by;
        String url = route.URL_SELECT_TUNJANGAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TunjanganModel Data;
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new TunjanganModel(
                                obj.getInt("id"),
                                obj.getString("kode"),
                                obj.getString("nama"),
                                obj.getString("keterangan"),
                                obj.getString("status")
                        );
                        records.add(Data);
                    }
                    //Satu baris kosong di akhir
                    Data = new TunjanganModel(0,"","","","HIDE");
                    records.add(Data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
            }
        });
        // menambah request ke request queue
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    public void Get_master_tunjangan(int id){
        String filter;
        filter = "?id="+id;
        String url = route.URL_SELECT_GET + filter;
        Log.d("masuk urllll",url);
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    TunjanganModel Data;
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new TunjanganModel(
                                obj.getInt("id"),
                                obj.getString("kode"),
                                obj.getString("nama"),
                                obj.getString("keterangan"),
                                obj.getString("status")
                        );
                        Tunjangan = Data;
                        Log.d("ini datanya","kampretttt");
                        Log.d("ini datanya",Tunjangan.getNama());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
            }
        });
        // menambah request ke request queue
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

//    public void Get_master_tunjangan(int id){
//        String filter;
//        filter = "?id="+id;
//        String url = route.URL_SELECT_GET + filter;
//        Log.d("masuk urllll",url);
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                TunjanganModel Data;
//                Log.d("masuk respon","masuk respon");
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    JSONObject obj = jsonArray.getJSONObject(0);
//                    Tunjangan.setId(obj.getInt("id"));
//                    Tunjangan.setKode(obj.getString("kode"));
//                    Tunjangan.setNama(obj.getString("nama"));
//                    Tunjangan.setKeterangan(obj.getString("keterangan"));
//                    Tunjangan.setStatus(obj.getString("status"));
//                    Log.d("masuk get",Tunjangan.getNama());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("error", "Error: " + error.getMessage());
//            }
//        });
//        // menambah request ke request queue
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

    public void request_master_tunjangan(int id){
    }


    public void aktivasi(long ID, String status){
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update("master_Tunjangan", cv,"_id = "+ID,null);
        this.ReloadList(Fstatus, order_by);
    }

    public ArrayList<TunjanganModel> GetRecords(){
        Log.d("masuk get","masuk record");
        return records;
    }

    public TunjanganModel GetDataTunjangan(){
        Log.d("masuk get","masuk get data");
        Log.d("masuk get",Tunjangan.getNama()+"pangke");
        Log.d("masuk get","mana si nama");
        return this.Tunjangan;
    }

    public void setStatus(String Fstatus) {
        this.Fstatus = Fstatus;
    }

    public void SetRecords(ArrayList<TunjanganModel> records) {
        this.records = records;
    }

    public TunjanganModel GetDataByIndex(int index) {
        return this.records.get(index);
    }

}



//    public void ReloadList(String status){
//        String filter = "";
//        if (status != ""){
//            filter += " where status = '" + status + "'";
//        };
//
//        this.records.clear();
//
//        Cursor cr = this.db.rawQuery("SELECT * FROM master_tunjangan "+ filter ,null);
//        TunjanganModel Data;
//        if (cr != null && cr.moveToFirst()){
//            do {
//                Data = new TunjanganModel(
//                        cr.getInt(cr.getColumnIndexOrThrow("_id")),
//                        cr.getString(cr.getColumnIndexOrThrow("kode")),
//                        cr.getString(cr.getColumnIndexOrThrow("nama")),
//                        cr.getString(cr.getColumnIndexOrThrow("keterangan")),
//                        cr.getString(cr.getColumnIndexOrThrow("status"))
//                );
//                this.records.add(Data);
//            }while (cr.moveToNext());
//        }
//        Data = new TunjanganModel(0,"","","","");
//        this.records.add(Data);
//    }


//    public void Insert(TunjanganModel Data){
//        ContentValues cv;
//        cv = SetValue(Data);
//        this.db.insert("master_Tunjangan",null,cv);
//        this.ReloadList(Fstatus, OrderBY);
//    }


//    public void Update(TunjanganModel Data){
//        ContentValues cv;
//        cv = SetValue(Data);
//        this.db.update("master_Tunjangan",cv,"_id = "+Data.getId(),null);
//        this.ReloadList(Fstatus, OrderBY);
//    }


//    private ContentValues SetValue (TunjanganModel Data ){
//        ContentValues cv = new ContentValues();
//        cv.put("kode", Data.getKode());
//        cv.put("nama", Data.getNama());
//        cv.put("keterangan", Data.getKeterangan());
//        cv.put("status", Data.getStatus());
//        return cv;
//    }

//    public void delete(long ID){
//        this.db.delete("master_Tunjangan", "_id = " + ID, null);
//        this.ReloadList(Fstatus, OrderBY);
//    }