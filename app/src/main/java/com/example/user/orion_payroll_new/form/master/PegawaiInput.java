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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.ModelsHelper.PenggajianDetailModel;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.adapter.ExpandListAadapterPenggajian;
import com.example.user.orion_payroll_new.form.adapter.ExpandListAdapterPegawai;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.EngineGeneral;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_SEARCH_TUNJANGAN;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatMySqlDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormatMySql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_PEGAWAI;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_PEGAWAI;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_TUNJANGAN;

public class PegawaiInput extends AppCompatActivity {
    private TextInputEditText txtNik, txtNama, txtAlamat, txtTelpon1, txtTelpon2, txtEmail, txtGajiPokok, txtTglLahir, txtTglMulaiBekerja, txtKeterangan;
    private Button btnSimpan;
    private String Mode;
    private int IdMst;
    private ProgressDialog Loading;
    public EditText txtTmp;

    public ExpandableListView ListView;
    public ExpandListAdapterPegawai ListAdapter;
    private List<String> ListDataHeader;
    private HashMap<String, List<TunjanganModel>> ListHash;
    public List<TunjanganModel> ArListTunjangan;

    protected void CreateView(){
        txtNik             = (TextInputEditText) findViewById(R.id.txtNik);
        txtNama            = (TextInputEditText) findViewById(R.id.txtNama);
        txtAlamat          = (TextInputEditText) findViewById(R.id.txtAlamat);
        txtTelpon1         = (TextInputEditText) findViewById(R.id.txtTelpon1);
        txtTelpon2         = (TextInputEditText) findViewById(R.id.txtTelpon2);
        txtEmail           = (TextInputEditText) findViewById(R.id.txtEmail);
        txtGajiPokok       = (TextInputEditText) findViewById(R.id.txtGajiPokok);
        txtTglLahir        = (TextInputEditText) findViewById(R.id.txtTglLahir);
        txtKeterangan      = (TextInputEditText) findViewById(R.id.txtKeterangan);
        txtTglMulaiBekerja = (TextInputEditText) findViewById(R.id.txtTglMulaiBekerja);
        btnSimpan          = (Button) findViewById(R.id.btnSimpan);
        ListView           = (ExpandableListView)findViewById(R.id.ExpLv);

        txtTmp = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.IdMst = extra.getInt("ID");
        Loading = new ProgressDialog(PegawaiInput.this);

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Pegawai");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Pegawai");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Pegawai");
        };

        ListDataHeader = new ArrayList<>();
        ListHash = new HashMap<>();
        ListDataHeader.add("TUNJANGAN");
        ArListTunjangan = new ArrayList<>();
        ListHash.put(ListDataHeader.get(0), ArListTunjangan);
        ListAdapter = new ExpandListAdapterPegawai(this, ListDataHeader, ListHash);
        ListView.setAdapter(ListAdapter);

        boolean Enabled = !Mode.equals(DETAIL_MODE);
        this.txtNik.setEnabled(Enabled);
        this.txtNama.setEnabled(Enabled);
        this.txtTelpon1.setEnabled(Enabled);
        this.txtTelpon2.setEnabled(Enabled);
        this.txtEmail.setEnabled(Enabled);
        this.txtGajiPokok.setEnabled(Enabled);
        this.btnSimpan.setEnabled(Enabled);
        this.txtTglLahir.setEnabled(Enabled);
        this.txtTglMulaiBekerja.setEnabled(Enabled);
        this.txtKeterangan.setEnabled(Enabled);

        txtTmp.setVisibility(View.INVISIBLE);

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
                    }else{
                        IsSaved();
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

        txtTglMulaiBekerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(PegawaiInput.this);
                if (txtTglMulaiBekerja.getText().toString().equals("")){
                    txtTglMulaiBekerja.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTglMulaiBekerja.getText().toString());
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
                                txtTglMulaiBekerja.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                txtTglMulaiBekerja.setError(null);
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
        Loading.setMessage("Loading...");
        Loading.setCancelable(false);
        Loading.show();
        String filter;
        filter = "?id="+IdMst;
        String url = route.URL_GET_PEGAWAI  + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TunjanganModel Data;
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject obj = jsonArray.getJSONObject(0);
                    txtNik.setText(obj.getString("nik"));
                    txtNama.setText(obj.getString("nama"));
                    txtAlamat.setText(obj.getString("alamat"));
                    txtTelpon1.setText(obj.getString("no_telpon_1"));
                    txtTelpon2.setText(obj.getString("no_telpon_2"));
                    txtEmail.setText(obj.getString("email"));
                    txtTglLahir.setText(FormatDateFromSql(obj.getString("tgl_lahir")));
                    txtTglMulaiBekerja.setText(FormatDateFromSql(obj.getString("tgl_mulai_kerja")));
                    txtGajiPokok.setText(obj.getString("gaji_pokok"));
                    txtKeterangan.setText(obj.getString("keterangan"));
                    Loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
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
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PEGAWAI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(PegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
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
                params.put("tgl_lahir", String.valueOf(FormatMySqlDate(txtTglLahir.getText().toString())));
                params.put("tgl_mulai_kerja", String.valueOf(FormatMySqlDate(txtTglMulaiBekerja.getText().toString())));
                params.put("gaji_pokok", String.valueOf(StrFmtToDouble(txtGajiPokok.getText().toString())));
                params.put("status", String.valueOf(TRUE_STRING));
                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
    }

    protected void IsSavedEdit(){
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_PEGAWAI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(PegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("iddd",Integer.toString(IdMst));
                params.put("id", String.valueOf(Integer.toString(IdMst)));
                params.put("nik", String.valueOf(txtNik.getText().toString()));
                params.put("nama", String.valueOf(txtNama.getText().toString()));
                params.put("alamat", String.valueOf(txtAlamat.getText().toString()));
                params.put("no_telpon_1", String.valueOf(txtTelpon1.getText().toString()));
                params.put("no_telpon_2", String.valueOf(txtTelpon2.getText().toString()));
                params.put("email", String.valueOf(txtEmail.getText().toString()));
                params.put("tgl_lahir", String.valueOf(FormatMySqlDate(txtTglLahir.getText().toString())));
                params.put("tgl_mulai_kerja", String.valueOf(FormatMySqlDate(txtTglMulaiBekerja.getText().toString())));
                params.put("gaji_pokok", String.valueOf(StrFmtToDouble(txtGajiPokok.getText().toString())));
                params.put("status", String.valueOf(TRUE_STRING));
                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
    }


    protected boolean IsValid(){
        if (this.txtNik.getText().toString().trim().equals("")) {
            txtNik.requestFocus();
            txtNik.setError("NIK belum diisi");
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

        if (this.txtTglLahir.getText().toString().equals("")) {
            txtTglLahir.setFocusableInTouchMode(true);
            txtTglLahir.requestFocus();
            txtTglLahir.setError("Tgl. Lahir belum diisi");
            txtTglLahir.setFocusableInTouchMode(false);
            hideSoftKeyboard(this);
            return false;
        }

        if (this.txtTglMulaiBekerja.getText().toString().equals("")) {
            txtTglMulaiBekerja.setFocusableInTouchMode(true);
            txtTglMulaiBekerja.requestFocus();
            txtTglMulaiBekerja.setError("Tgl. Mulai bekerja belum diisi");
            txtTglMulaiBekerja.setFocusableInTouchMode(false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_SEARCH_TUNJANGAN) {
                Bundle extra = data.getExtras();
                TunjanganModel Tunjangan = new TunjanganModel(
                        extra.getInt("id"),
                        extra.getString("kode"),
                        extra.getString("nama"),
                        extra.getString("keterangan"),
                        extra.getString("status")
                );
                ArListTunjangan.add(Tunjangan);
                ListAdapter.notifyDataSetChanged();
            }else{

            }
        }
    }
}


//    protected void LoadData(){
//        PegawaiModel Data = this.TPegawai.GetDataKaryawanByIndex(this.SelectedData);
//        this.IdMst = Data.getId();
//        this.txtNik.setText(Data.getNik());
//        this.txtNama.setText(Data.getNama());
//        this.txtTelpon1.setText(Data.getTelpon1());
//        this.txtTelpon2.setText(Data.getTelpon2());
//        this.txtEmail.setText(Data.getEmail());
//        this.txtGajiPokok.setText(fmt.format(Data.getgaji_pokok()));
//        this.txtTglLahir.setText(FungsiGeneral.getTglFormat(Data.getTgl_lahir()));
//    }

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

//    protected boolean IsSavedEdit(){
//        PegawaiModel Data = new PegawaiModel(IdMst,txtNik.getText().toString().trim(),
//                                             txtNama.getText().toString().trim(),
//                                             txtAlamat.getText().toString().trim(),
//                                             txtTelpon1.getText().toString().trim(),
//                                             txtTelpon2.getText().toString().trim(),
//                                             txtEmail.getText().toString().trim(),
//                                             StrFmtToDouble(txtGajiPokok.getText().toString()),
//                                             TRUE_STRING,
//                                             getSimpleDate(txtTglLahir.getText().toString())
//        );
//        TPegawai.Update(Data);
//        PegawaiInput.this.onBackPressed();
//        return true;
//    }