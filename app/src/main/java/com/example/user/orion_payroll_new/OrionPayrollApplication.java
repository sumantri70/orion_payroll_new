package com.example.user.orion_payroll_new;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.orion_payroll_new.database.DBConection;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.database.master.PotonganTable;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.form.adapter.PegawaiAdapter;
import com.example.user.orion_payroll_new.form.adapter.TunjanganAdapter;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganRekap;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.PotonganModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.EngineGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;

public class OrionPayrollApplication extends Application {
    public static final String TAG = OrionPayrollApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static OrionPayrollApplication mInstance;

    public HashMap<String, PegawaiModel> ListHashPegawaiGlobal;
    public HashMap<String, TunjanganModel> ListHashTunjanganGlobal;
    public HashMap<String, PotonganModel> ListHashPotonganGlobal;

    public String USER_LOGIN = "Sumantri";

    public DBConection DbConn;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        GetHashMaster();
        this.DbConn = new DBConection(getApplicationContext());
    }

    protected void GetHashMaster(){
        ListHashPegawaiGlobal   = new HashMap<>();
        ListHashTunjanganGlobal = new HashMap<>();
        ListHashPotonganGlobal  = new HashMap<>();

        GetHashPegawai();
        GetHashTunjangan();
        GetHashPotongan();
    }

    //public void GetHashPegawai(){
//        String filter;
//        filter = "?status=&order_by=";
//        String url = route.URL_SELECT_PEGAWAI + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PegawaiModel Data;
//                OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new PegawaiModel(
//                                obj.getInt("id"),
//                                obj.getString("nik"),
//                                obj.getString("nama"),
//                                obj.getString("alamat"),
//                                obj.getString("no_telpon_1"),
//                                obj.getString("no_telpon_2"),
//                                obj.getString("email"),
//                                obj.getDouble("gaji_pokok"),
//                                obj.getString("status"),
//                                getMillisDate(obj.getString("tgl_lahir")),
//                                getMillisDate(obj.getString("tgl_mulai_kerja")),
//                                obj.getString("keterangan")
//                        );
//
//                        Data.setUang_ikatan(obj.getDouble("uang_ikatan"));
//                        Data.setUang_kehadiran(obj.getDouble("uang_kehadiran"));
//                        Data.setPremi_harian(obj.getDouble("Premi_harian"));
//                        Data.setPremi_perjam(obj.getDouble("Premi_perjam"));
//                        ListHashPegawaiGlobal.put(Integer.toString(Data.getId()), Data);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(OrionPayrollApplication.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.clear();
//                Toast.makeText(OrionPayrollApplication.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

//    public void GetHashTunjangan(){
//        String filter;
//        filter = "?status=&order_by=";
//        String url = route.URL_SELECT_TUNJANGAN + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                TunjanganModel Data;
//                ListHashTunjanganGlobal.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new TunjanganModel(
//                                obj.getInt("id"),
//                                obj.getString("kode"),
//                                obj.getString("nama"),
//                                obj.getString("keterangan"),
//                                obj.getString("status")
//                        );
//                        ListHashTunjanganGlobal.put(Integer.toString(Data.getId()), Data);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(OrionPayrollApplication.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(OrionPayrollApplication.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

//    public void GetHashPotongan(){
//        String filter;
//        filter = "?status=&order_by=";
//        String url = route.URL_SELECT_POTONGAN + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PotonganModel Data;
//                ListHashPotonganGlobal.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new PotonganModel(
//                                obj.getInt("id"),
//                                obj.getString("kode"),
//                                obj.getString("nama"),
//                                obj.getString("keterangan"),
//                                obj.getString("status")
//                        );
//                        ListHashPotonganGlobal.put(Integer.toString(Data.getId()), Data);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(OrionPayrollApplication.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(OrionPayrollApplication.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

    public void GetHashPotongan() {
        PotonganTable TPotongan = new PotonganTable(getApplicationContext());
        ListHashPotonganGlobal = TPotongan.GetHash();
    }

    public void GetHashTunjangan() {
        TunjanganTable TTunjangan = new TunjanganTable(getApplicationContext());
        ListHashTunjanganGlobal = TTunjangan.GetHash();
    }

    public void GetHashPegawai() {
        PegawaiTable TPegawai = new PegawaiTable(getApplicationContext());
        ListHashPegawaiGlobal = TPegawai.GetHash();
    }

    public static synchronized OrionPayrollApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}