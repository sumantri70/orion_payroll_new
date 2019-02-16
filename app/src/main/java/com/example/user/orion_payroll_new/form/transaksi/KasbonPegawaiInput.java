package com.example.user.orion_payroll_new.form.transaksi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.KasbonPegawaiTable;
import com.example.user.orion_payroll_new.database.master.PotonganTable;
import com.example.user.orion_payroll_new.form.adapter.ExpandListAdapterPegawai;
import com.example.user.orion_payroll_new.form.lov.lov_pegawai;
import com.example.user.orion_payroll_new.form.master.PegawaiInput;
import com.example.user.orion_payroll_new.models.KasbonPegawaiModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_DELETE_CONFIRMATION;
import static com.example.user.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatMySqlDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getBulan;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_KASBON;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_PEGAWAI;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_KASBON;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_TUNJANGAN;

public class KasbonPegawaiInput extends AppCompatActivity {
    private TextInputEditText txtTanggal, txtPegawai, txtJumlah, txtCicilan, txtKeterangan, txtNomor;
    private Button btnSimpan;
    private TextInputLayout LayoutNomor;


    public String Mode;
    private int IdMst, IdPegawai;
    private ProgressDialog Loading;
    private KasbonPegawaiTable TData;

    protected void CreateView(){
        txtNomor        = (TextInputEditText) findViewById(R.id.txtNomor);
        txtTanggal      = (TextInputEditText) findViewById(R.id.txtTanggal);
        txtPegawai      = (TextInputEditText) findViewById(R.id.txtPegawai);
        txtJumlah       = (TextInputEditText) findViewById(R.id.txtJumlah);
        txtCicilan      = (TextInputEditText) findViewById(R.id.txtCicilan);
        txtKeterangan   = (TextInputEditText) findViewById(R.id.txtKeterangan);
        LayoutNomor     = (TextInputLayout) findViewById(R.id.LayoutNomor);
        btnSimpan       = (Button) findViewById(R.id.btnSimpan);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.IdMst = extra.getInt("ID");
        Loading = new ProgressDialog(KasbonPegawaiInput.this);
        TData = new KasbonPegawaiTable(getApplicationContext());
        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Kasbon Pegawai");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Kasbon Pegawai");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Kasbon Pegawai");
            this.txtNomor.setVisibility(View.GONE);
            this.LayoutNomor.setVisibility(View.GONE);
        };

        IdPegawai = 0;
        txtTanggal.setText(FungsiGeneral.serverNowFormated());

        boolean Enabled = !Mode.equals(DETAIL_MODE);
        this.txtNomor.setEnabled(Enabled);
        this.txtTanggal.setEnabled(Enabled);
        this.txtPegawai.setEnabled(Enabled);
        this.txtJumlah.setEnabled(Enabled);
        this.txtCicilan.setEnabled(Enabled);
        this.txtKeterangan.setEnabled(Enabled);
        txtJumlah.addTextChangedListener(new FormatNumber(txtJumlah));
    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsValid() == true){
                    AlertDialog.Builder bld = new AlertDialog.Builder(KasbonPegawaiInput.this);
                    bld.setTitle("Konfirmasi");
                    bld.setCancelable(true);
                    bld.setMessage(MSG_DELETE_CONFIRMATION);

                    bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (IsValid() == true){
                                if(Mode.equals(EDIT_MODE)){
                                    if (IsSavedEdit()){
                                        Toast.makeText(KasbonPegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }else{
                                        Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    if (IsSaved()){
                                        Toast.makeText(KasbonPegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }else{
                                        Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                                    };
                                }
                            }
                        }
                    });

                    bld.setNegativeButton(MSG_NEGATIVE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = bld.create();
                    dialog.show();
                }
            }
        });

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(KasbonPegawaiInput.this);
                Long tgl = FungsiGeneral.getMillisDate(txtTanggal.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(KasbonPegawaiInput.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("dd-MM-yyyy");
                                txtTanggal.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                txtTanggal.setError(null);
            }
        });

        txtPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdPegawai = 0;
                txtPegawai.setText("");
                txtPegawai.setError(null);
                Intent s = new Intent(KasbonPegawaiInput.this, lov_pegawai.class);
                KasbonPegawaiInput.this.startActivityForResult(s, RESULT_LOV);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasbon_pegawai_input);
        CreateView();
        InitClass();
        EventClass();
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

//    protected void LoadData(){
//        Loading.setMessage("Loading...");
//        Loading.setCancelable(false);
//        Loading.show();
//        String filter;
//        filter = "?id="+IdMst;
//        String url = route.URL_GET_KASBON  + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    JSONObject obj = jsonArray.getJSONObject(0);
//                    IdPegawai = obj.getInt("id_pegawai");
//                    txtNomor.setText(obj.getString("nomor"));
//                    txtTanggal.setText(FormatDateFromSql(obj.getString("tanggal")));
//                    txtPegawai.setText(OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(obj.getInt("id_pegawai"))).getNama());
//                    txtJumlah.setText(fmt.format(obj.getDouble("jumlah")));
//                    txtCicilan.setText(obj.getString("cicilan"));
//                    txtKeterangan.setText(obj.getString("keterangan"));
//                    Loading.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                    Loading.dismiss();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("error", "Error: " + error.getMessage());
//                Loading.dismiss();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

    protected void LoadData(){
        Loading.setMessage("Loading...");
        Loading.setCancelable(false);
        Loading.show();

        KasbonPegawaiModel Data = TData.GetData(IdMst);
        IdPegawai = Data.getId_pegawai();
        txtNomor.setText(Data.getNomor());
        txtTanggal.setText(getTglFormat(Data.getTanggal()));
        txtPegawai.setText(Get_Nama_Master_Pegawai(IdPegawai));
        txtJumlah.setText(fmt.format(Data.getJumlah() ));
        txtCicilan.setText(String.valueOf(Data.getCicilan()));
        txtKeterangan.setText(Data.getKeterangan());

        Loading.dismiss();
    }

//    protected void IsSaved(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_KASBON, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(KasbonPegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("tanggal", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
//                params.put("id_pegawai", String.valueOf(IdPegawai));
//                params.put("jumlah", String.valueOf(StrFmtToDouble(txtJumlah.getText().toString())));
//                params.put("sisa", String.valueOf(StrFmtToDouble(txtJumlah.getText().toString())));
//                params.put("cicilan", String.valueOf(StrFmtToDouble(txtCicilan.getText().toString())));
//                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                params.put("user_id", String.valueOf(OrionPayrollApplication.getInstance().USER_LOGIN));
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//    }

    protected boolean IsSaved(){
        try {
            KasbonPegawaiModel Data = new KasbonPegawaiModel(
                    0,getSimpleDate(txtTanggal.getText().toString()),
                    TData.getNextNumber(getSimpleDate(txtTanggal.getText().toString())),
                    IdPegawai,
                    StrFmtToDouble(txtJumlah.getText().toString()),
                    StrFmtToDouble(txtJumlah.getText().toString()),
                    Integer.valueOf(txtCicilan.getText().toString()),
                    txtKeterangan.getText().toString(),
                    OrionPayrollApplication.getInstance().USER_LOGIN,
                    serverNowLong(),"",0);
            TData.Insert(Data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


//    protected void IsSavedEdit(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_KASBON, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(KasbonPegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(KasbonPegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", String.valueOf(IdMst));
//                params.put("nomor", String.valueOf(txtNomor.getText()));
//                params.put("tanggal", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
//                params.put("id_pegawai", String.valueOf(IdPegawai));
//                params.put("jumlah", String.valueOf(StrFmtToDouble(txtJumlah.getText().toString())));
//                params.put("sisa", String.valueOf(StrFmtToDouble(txtJumlah.getText().toString())));
//                params.put("cicilan", String.valueOf(StrFmtToDouble(txtCicilan.getText().toString())));
//                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                params.put("user_edit", String.valueOf(OrionPayrollApplication.getInstance().USER_LOGIN));
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//    }

    protected boolean IsSavedEdit(){
        KasbonPegawaiModel DataOld = new KasbonPegawaiModel(TData.GetData(IdMst));
        try {
            KasbonPegawaiModel DataNew = new KasbonPegawaiModel(DataOld);

            DataNew.setTanggal(getSimpleDate(txtTanggal.getText().toString()));
            DataNew.setNomor(txtNomor.getText().toString().trim());
            DataNew.setId_pegawai(IdPegawai);
            DataNew.setJumlah(StrFmtToDouble(txtJumlah.getText().toString()));
            DataNew.setSisa(StrFmtToDouble(txtJumlah.getText().toString()));
            DataNew.setCicilan(Integer.valueOf(txtCicilan.getText().toString()));
            DataNew.setKeterangan(txtKeterangan.getText().toString());
            DataNew.setUser_edit(OrionPayrollApplication.getInstance().USER_LOGIN);
            DataNew.setTgl_edit(serverNowLong());
            TData.Update(DataNew);
        } catch (Exception e) {
            e.printStackTrace();
            TData.Update(DataOld);
            return false;
        }
        return true;
    }

    protected boolean IsValid(){
        if (this.txtPegawai.getText().toString().trim().equals("")) {
            txtPegawai.setFocusableInTouchMode(true);
            txtPegawai.requestFocus();
            txtPegawai.setError("Pegawai belum dipilih");
            txtPegawai.setFocusableInTouchMode(false);
            return false;
        }

        if (StrFmtToDouble(txtJumlah.getText().toString()) == 0) {
            txtJumlah.requestFocus();
            txtJumlah.setError("Jumlah belum diisi");
            return false;
        }

        if (StrFmtToDouble(txtCicilan.getText().toString()) == 0) {
            txtCicilan.requestFocus();
            txtCicilan.setError("Cicilan belum diisi");
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV) {
                Bundle extra = data.getExtras();
                IdPegawai = extra.getInt("id");
                txtPegawai.setText(Get_Nama_Master_Pegawai(extra.getInt("id")));
            }
        }
    }
}
