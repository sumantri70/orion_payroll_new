package com.example.user.orion_payroll_new.form.master;

import android.app.DatePickerDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

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
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_PEGAWAI;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_TUNJANGAN;

public class TunjanganInput extends AppCompatActivity {
    private TextInputEditText txtKode, txtNama, txtKeterangan;
    private Button btnSimpan;

    public Boolean EditMode;
    private String Mode;
    private int SelectedData;
    private int IdMst;

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
        this.SelectedData = extra.getInt("POSITION");

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Tinjangan");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Tinjangan");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Tinjangan");
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
                        Toast.makeText(TunjanganInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                    }else{
                        IsSaved();
                        Toast.makeText(TunjanganInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
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
    }

    protected void onStart() {
        super.onStart();
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
        }
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void LoadData(){
        //PegawaiModel Data = this.TPegawai.GetDataKaryawanByIndex(this.SelectedData);
//        this.IdMst = Data.getId();
//        this.txtNik.setText(Data.getNik());
//        this.txtNama.setText(Data.getNama());
//        this.txtTelpon1.setText(Data.getTelpon1());
//        this.txtTelpon2.setText(Data.getTelpon2());
//        this.txtEmail.setText(Data.getEmail());
//        this.txtGajiPokok.setText(fmt.format(Data.getgaji_pokok()));
//        this.txtTglLahir.setText(FungsiGeneral.getTglFormat(Data.getTgl_lahir()));
    }

    protected boolean IsSaved(){
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
                params.put("kode", String.valueOf(txtKode.getText().toString()));
                params.put("nama", String.valueOf(txtNama.getText().toString()));
                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
                params.put("status", String.valueOf(TRUE_STRING));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
        return true;
    }

    protected boolean IsSavedEdit(){
//        PegawaiModel Data = new PegawaiModel(IdMst,txtNik.getText().toString().trim(),
//                txtNama.getText().toString().trim(),
//                txtAlamat.getText().toString().trim(),
//                txtTelpon1.getText().toString().trim(),
//                txtTelpon2.getText().toString().trim(),
//                txtEmail.getText().toString().trim(),
//                StrFmtToDouble(txtGajiPokok.getText().toString()),
//                TRUE_STRING,
//                getSimpleDate(txtTglLahir.getText().toString())
//        );
//        TPegawai.Update(Data);
//        PegawaiInput.this.onBackPressed();
        return true;
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
