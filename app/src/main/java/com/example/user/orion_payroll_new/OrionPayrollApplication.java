package com.example.user.orion_payroll_new;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.EngineGeneral;

import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;

public class OrionPayrollApplication extends Application {
    public static final String TAG = OrionPayrollApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static OrionPayrollApplication mInstance;


    public PegawaiTable TPegawai;
    public TunjanganTable TTunjangan;

    public TunjanganModel DtTunjangan;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.TPegawai = new PegawaiTable(getApplicationContext(), TRUE_STRING, "NIK");
        this.TTunjangan = new TunjanganTable(getApplicationContext());
        this.DtTunjangan = new TunjanganModel(0,"","","","");
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
        req.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
        Log.d("ADa kesini","ADa kesini");
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
