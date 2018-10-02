package com.example.user.orion_payroll_new.form.master;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_PEGAWAI;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_TUNJANGAN;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_TUNJANGAN;

public class TunjanganInput extends AppCompatActivity {
    private TextInputEditText txtKode, txtNama, txtKeterangan;
    private Button btnSimpan;
    private String Mode;
    private int IdMst;
    private ProgressDialog Loading;

    protected void CreateView(){
        txtKode       = (TextInputEditText) findViewById(R.id.txtKode);
        txtNama       = (TextInputEditText) findViewById(R.id.txtNama);
        txtKeterangan = (TextInputEditText) findViewById(R.id.txtKeterangan);
        btnSimpan     = (Button) findViewById(R.id.btnSimpan);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.IdMst = extra.getInt("ID");
        Loading = new ProgressDialog(TunjanganInput.this);

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Tunjangan");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Tunjangan");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Tunjangan");
        };

        boolean Enabled = !Mode.equals(DETAIL_MODE);
        this.txtKode.setEnabled(Enabled);
        this.txtNama.setEnabled(Enabled);
        this.txtKeterangan.setEnabled(Enabled);
        this.btnSimpan.setEnabled(Enabled);
        txtKode.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); //untuk uppercase
    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (IsValid() == true){
                if(Mode.equals(EDIT_MODE)){
                    IsSavedEdit();
                }else{
                    IsSaved();
                }
            }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunjangan_input);
        CreateView();
        InitClass();
        EventClass();
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
        }
    }

    protected void onStart() {
        super.onStart();
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void LoadData(){
        Loading.setMessage("Loading...");
        Loading.setCancelable(false);
        Loading.show();
        String filter;
        filter = "?id="+IdMst;
        String url = route.URL_GET_TUNJANGAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TunjanganModel Data;
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject obj = jsonArray.getJSONObject(0);
                    txtKode.setText(obj.getString("kode"));
                    txtNama.setText(obj.getString("nama"));
                    txtKeterangan.setText(obj.getString("keterangan"));
                    Loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TunjanganInput.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    Loading.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Loading.dismiss();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    protected void IsSaved(){
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_TUNJANGAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(TunjanganInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TunjanganInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TunjanganInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", String.valueOf(txtKode.getText().toString()));
                params.put("nama", String.valueOf(txtNama.getText().toString()));
                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
                params.put("status", String.valueOf(TRUE_STRING));
                return params;


//                Map<String, String> params = new HashMap<String, String>();
//                JSONArray ArTunjangan = new JSONArray();
//                for(int i=0; i < 2;i++){
//                    JSONObject obj= new JSONObject();
//                    try {
//                        obj.put("kode", String.valueOf(txtKode.getText().toString()));
//                        obj.put("nama", String.valueOf(txtNama.getText().toString()));
//                        obj.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                        obj.put("status", String.valueOf(TRUE_STRING));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ArTunjangan.put(obj);
//                }
//
//                //Log.d("tahhhhhhhhhhhhhh",ArTunjangan.toString());
//                params.put("data", ArTunjangan.toString());
//                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
    }

    protected void IsSavedEdit(){
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_TUNJANGAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(TunjanganInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TunjanganInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TunjanganInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(Integer.toString(IdMst)));
                params.put("kode", String.valueOf(txtKode.getText().toString()));
                params.put("nama", String.valueOf(txtNama.getText().toString()));
                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
                params.put("status", String.valueOf(TRUE_STRING));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
    }

    protected boolean IsValid(){
        if (this.txtKode.getText().toString().trim().equals("")) {
            txtKode.requestFocus();
            txtKode.setError("Kode belum diisi");
            return false;
        }

//        if (TPegawai.KodeExist(this.txtNik.getText().toString().trim(),IdMst)) {
//            txtNik.requestFocus();
//            txtNik.setError("NIK sudah pernah digunakan");
//            return false;
//        }

        if (this.txtNama.getText().toString().equals("")) {
            txtNama.requestFocus();
            txtNama.setError("Nama belum diisi");
            return false;
        }
        return true;
    }
}


//    protected boolean IsSaved(){
//        TunjanganModel Data = new TunjanganModel(
//                            0,txtKode.getText().toString().trim(),
//                            txtNama.getText().toString().trim(),
//                            txtKeterangan.getText().toString().trim(),
//                            TRUE_STRING);
//        TData.Insert(Data);
//        return true;
//    }

//    protected boolean IsSavedEdit(){
//        TunjanganModel Data = new TunjanganModel(
//                this.IdMst,
//                txtKode.getText().toString().trim(),
//                txtNama.getText().toString().trim(),
//                txtKeterangan.getText().toString().trim(),
//                TRUE_STRING);
//        TData.Update(Data);
//        return true;
//    }