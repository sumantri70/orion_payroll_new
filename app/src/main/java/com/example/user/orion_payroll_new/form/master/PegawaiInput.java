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
import com.example.user.orion_payroll_new.utility.EngineGeneral;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
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

public class PegawaiInput extends AppCompatActivity {
    private TextInputEditText txtNik, txtNama, txtAlamat, txtTelpon1, txtTelpon2, txtEmail, txtGajiPokok, txtTglLahir;
    private Button btnSimpan;

    private PegawaiTable TPegawai;
    public Boolean EditMode;
    private String Mode;
    private int SelectedData;
    private int IdMst;

    protected void CreateView(){
        txtNik       = (TextInputEditText) findViewById(R.id.txtNik);
        txtNama      = (TextInputEditText) findViewById(R.id.txtNama);
        txtAlamat    = (TextInputEditText) findViewById(R.id.txtAlamat);
        txtTelpon1   = (TextInputEditText) findViewById(R.id.txtTelpon1);
        txtTelpon2   = (TextInputEditText) findViewById(R.id.txtTelpon2);
        txtEmail     = (TextInputEditText) findViewById(R.id.txtEmail);
        txtGajiPokok = (TextInputEditText) findViewById(R.id.txtGajiPokok);
        txtTglLahir  = (TextInputEditText) findViewById(R.id.txtTglLahir);
        btnSimpan    = (Button) findViewById(R.id.btnSimpan);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.SelectedData = extra.getInt("POSITION");
        this.TPegawai = ((OrionPayrollApplication)getApplicationContext()).TPegawai;

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Pegawai");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Pegawai");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Pegawai");
        };

        boolean Enabled = !Mode.equals(DETAIL_MODE);
        this.txtNik.setEnabled(Enabled);
        this.txtNama.setEnabled(Enabled);
        this.txtTelpon1.setEnabled(Enabled);
        this.txtTelpon2.setEnabled(Enabled);
        this.txtEmail.setEnabled(Enabled);
        this.txtGajiPokok.setEnabled(Enabled);
        this.btnSimpan.setEnabled(Enabled);
        this.txtTglLahir.setEnabled(Enabled);

        txtGajiPokok.addTextChangedListener(new FormatNumber(txtGajiPokok));
        txtNik.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); //untuk uppercase
    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsValid() == true){
                    if(Mode.equals(EDIT_MODE)){
                        IsSavedEdit();
                        Toast.makeText(PegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                    }else{
                        IsSaved();
                        Toast.makeText(PegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DialogFragment newFragment = new DatePickerDialogFragment();
                //newFragment.show(getFragmentManager(), "datePicker");
                hideSoftKeyboard(PegawaiInput.this);
                if (txtTglLahir.getText().toString().equals("")){
                    txtTglLahir.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTglLahir.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(PegawaiInput.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("dd-MM-yyyy");
                                txtTglLahir.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                txtTglLahir.setError(null);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai_input);
        CreateView();
        InitClass();
        EventClass();
    }

    @Override

    protected void onStart() {
        super.onStart();
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void LoadData(){
        PegawaiModel Data = this.TPegawai.GetDataKaryawanByIndex(this.SelectedData);
        this.IdMst = Data.getId();
        this.txtNik.setText(Data.getNik());
        this.txtNama.setText(Data.getNama());
        this.txtTelpon1.setText(Data.getTelpon1());
        this.txtTelpon2.setText(Data.getTelpon2());
        this.txtEmail.setText(Data.getEmail());
        this.txtGajiPokok.setText(fmt.format(Data.getgaji_pokok()));
        this.txtTglLahir.setText(FungsiGeneral.getTglFormat(Data.getTgl_lahir()));
    }

//    protected boolean IsSaved(){
//        PegawaiModel Data = new PegawaiModel(0,txtNik.getText().toString().trim(),
//                                             txtNama.getText().toString().trim(),
//                                             txtAlamat.getText().toString().trim(),
//                                             txtTelpon1.getText().toString().trim(),
//                                             txtTelpon2.getText().toString().trim(),
//                                             txtEmail.getText().toString().trim(),
//                                             StrFmtToDouble(txtGajiPokok.getText().toString()),
//                                             TRUE_STRING,
//                                             FungsiGeneral.getSimpleDate(txtTglLahir.getText().toString())
//                                             );
//        TPegawai.Insert(Data);
//        PegawaiInput.this.onBackPressed();
//        return true;
//    }

    protected boolean IsSaved(){
        Log.d("iiiiii",URL_INSERT_PEGAWAI);

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PEGAWAI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //int isGagal;
                    Log.d("java error","responnnnn");
                    JSONObject jObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("java error","gagalllllll");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("java error",error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", String.valueOf(txtNik.getText().toString()));
                params.put("nama", String.valueOf(txtNama.getText().toString()));
                params.put("alamat", String.valueOf(txtAlamat.getText().toString()));
                params.put("no_telpon_1", String.valueOf(txtTelpon1.getText().toString()));
                params.put("no_telpon_2", String.valueOf(txtTelpon2.getText().toString()));
                params.put("email", String.valueOf(txtEmail.getText().toString()));
                params.put("tgl_lahir", String.valueOf(getSimpleDate(txtTglLahir.getText().toString())));
                params.put("tgl_mulai_kerja", String.valueOf(getSimpleDate(txtTglLahir.getText().toString())));
                params.put("gaji_pokok", String.valueOf(txtGajiPokok.getText().toString()));
                params.put("status", String.valueOf(TRUE_STRING));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
        return true;
    }

    protected boolean IsSavedEdit(){
        PegawaiModel Data = new PegawaiModel(IdMst,txtNik.getText().toString().trim(),
                                             txtNama.getText().toString().trim(),
                                             txtAlamat.getText().toString().trim(),
                                             txtTelpon1.getText().toString().trim(),
                                             txtTelpon2.getText().toString().trim(),
                                             txtEmail.getText().toString().trim(),
                                             StrFmtToDouble(txtGajiPokok.getText().toString()),
                                             TRUE_STRING,
                                             getSimpleDate(txtTglLahir.getText().toString())
        );
        TPegawai.Update(Data);
        PegawaiInput.this.onBackPressed();
        return true;
    }

    protected boolean IsValid(){
        if (this.txtNik.getText().toString().trim().equals("")) {
            txtNik.requestFocus();
            txtNik.setError("NIK belum diisi");
            return false;
        }

        if (TPegawai.KodeExist(this.txtNik.getText().toString().trim(),IdMst)) {
            txtNik.requestFocus();
            txtNik.setError("NIK sudah pernah digunakan");
            return false;
        }

        if (this.txtNama.getText().toString().equals("")) {
            txtNama.requestFocus();
            txtNama.setError("Nama belum diisi");
            return false;
        }

        if (this.txtTglLahir.getText().toString().equals("")) {
            txtTglLahir.setFocusableInTouchMode(true);
            txtTglLahir.requestFocus();
            txtTglLahir.setError("Tgl. Lahir belum diisi");
            txtTglLahir.setFocusableInTouchMode(false);
            hideSoftKeyboard(this);
            return false;
        }

        if (StrFmtToDouble(txtGajiPokok.getText().toString()) == 0) {
            txtGajiPokok.requestFocus();
            txtGajiPokok.setError("Gaji pokok belum diisi");
            return false;
        }
        return true;
    }
}
