package com.example.user.orion_payroll_new.form.transaksi;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.adapter.ExpandListAdapterPegawai;
import com.example.user.orion_payroll_new.form.adapter.ExpandListAdapterPenggajianNew;
import com.example.user.orion_payroll_new.models.KasbonPegawaiModel;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;
import com.example.user.orion_payroll_new.models.PotonganModel;
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
import java.util.List;
import java.util.Map;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV_POTONGAN;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV_TUNJANGAN;
import static com.example.user.orion_payroll_new.models.JCons.TIPE_DET_POTONGAN;
import static com.example.user.orion_payroll_new.models.JCons.TIPE_DET_TUNJANGAN;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatMySqlDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_PEGAWAI;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_PENGGAJIAN;
import static com.example.user.orion_payroll_new.utility.route.URL_UPDATE_PEGAWAI;

public class PenggajianInputNew extends AppCompatActivity {
    private TextInputEditText txtNomor, txtTanggal, txtPegawai, txtTelat1, txtTelat2, txtDokter, txtNonCuti,
                              txtstghHari, txtCuti, txtLembur, txtKeterangan;
    private TextView lblGajiPokok, lblTotTunjangan, lblTotPotongan, lblTotKasbon, lblTotLembur, lblTotal, lblPilihTunjangan, lblPilihPotongan, lblPilikKAsbon;
    private Button btnSimpan;

    public String Mode;
    private int IdMst, IdPegawai;
    private ProgressDialog Loading;
    public EditText txtTmp;

    public ExpandableListView ListView;
    public ExpandListAdapterPenggajianNew ListAdapter;
    private List<String> ListDataHeader;
    private HashMap<String, List<PenggajianDetailModel>> ListHash;
    public List<PenggajianDetailModel> ArListTunjangan;
    public List<PenggajianDetailModel> ArListPotongan;
    public List<PenggajianDetailModel> ArListKasbon;

    private PegawaiModel DataPegawai;
    public HashMap<String, KasbonPegawaiModel> HashKasbon;

    protected void CreateView(){
        txtNomor      = (TextInputEditText) findViewById(R.id.txtNomor);
        txtTanggal    = (TextInputEditText) findViewById(R.id.txtTanggal);
        txtPegawai    = (TextInputEditText) findViewById(R.id.txtPegawai);
        txtTelat1     = (TextInputEditText) findViewById(R.id.txtTelat1);
        txtTelat2     = (TextInputEditText) findViewById(R.id.txtTelat2);
        txtDokter     = (TextInputEditText) findViewById(R.id.txtDokter);
        txtNonCuti    = (TextInputEditText) findViewById(R.id.txtNonCuti);
        txtstghHari   = (TextInputEditText) findViewById(R.id.txtstghHari);
        txtCuti       = (TextInputEditText) findViewById(R.id.txtCuti);
        txtLembur     = (TextInputEditText) findViewById(R.id.txtLembur);
        txtKeterangan = (TextInputEditText) findViewById(R.id.txtKeterangan);

        btnSimpan     = (Button) findViewById(R.id.btnSimpan);
        ListView      = (ExpandableListView)findViewById(R.id.ExpLv);
        txtTmp        = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA

        lblGajiPokok    = (TextView) findViewById(R.id.lblGajiPokok);
        lblTotTunjangan = (TextView) findViewById(R.id.lblTotTunjangan);
        lblTotPotongan  = (TextView) findViewById(R.id.lblTotPotongan);
        lblTotKasbon    = (TextView) findViewById(R.id.lblTotKasbon);
        lblTotLembur    = (TextView) findViewById(R.id.lblTotLembur);
        lblTotal        = (TextView) findViewById(R.id.lblTotal);

        lblPilihTunjangan = (TextView) findViewById(R.id.lblPilihTunjangan);
        lblPilihPotongan  = (TextView) findViewById(R.id.lblPilihPotongan);
        lblPilikKAsbon    = (TextView) findViewById(R.id.lblPilihKasbon);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.IdMst = extra.getInt("ID");
        Loading = new ProgressDialog(PenggajianInputNew.this);

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Penggajian");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Penggajian");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Penggajian");
        };

        ListDataHeader = new ArrayList<>();
        ListHash = new HashMap<>();
        ListDataHeader.add("TUNJANGAN");
        ListDataHeader.add("POTONGAN");
        ListDataHeader.add("KASBON");

        ArListTunjangan = new ArrayList<>();
        ArListPotongan  = new ArrayList<>();
        ArListKasbon    = new ArrayList<>();

        ListHash.put(ListDataHeader.get(0), ArListTunjangan);
        ListHash.put(ListDataHeader.get(1), ArListPotongan);
        ListHash.put(ListDataHeader.get(2), ArListKasbon);

        ListAdapter = new ExpandListAdapterPenggajianNew(this, ListDataHeader, ListHash);
        ListView.setAdapter(ListAdapter);
        ListView.getLayoutParams().height = 0;

        boolean Enabled = !Mode.equals(DETAIL_MODE);
        txtNomor.setEnabled(Enabled);
        txtTanggal.setEnabled(Enabled);
        txtPegawai.setEnabled(Enabled);
        txtTelat1.setEnabled(Enabled);
        txtTelat2.setEnabled(Enabled);
        txtDokter.setEnabled(Enabled);
        txtNonCuti.setEnabled(Enabled);
        txtstghHari.setEnabled(Enabled);
        txtCuti.setEnabled(Enabled);
        txtLembur.setEnabled(Enabled);
        txtKeterangan.setEnabled(Enabled);

        txtTmp.setVisibility(View.INVISIBLE);

        lblGajiPokok.setText("0");
        lblTotTunjangan.setText("0");
        lblTotPotongan.setText("0");
        lblTotKasbon.setText("0");
        lblTotLembur.setText("0");
        lblTotal.setText("0");
        IdPegawai = 0;

    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTmp.setVisibility(View.VISIBLE);
                txtTmp.requestFocus();
                txtTmp.setVisibility(View.INVISIBLE);
                if (IsValid() == true){
                    if(Mode.equals(EDIT_MODE)){
                        IsSavedEdit();
                    }else{
                        IsSaved();
                    }
                }
            }
        });

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(PenggajianInputNew.this);
                if (txtTanggal.getText().toString().equals("")){
                    txtTanggal.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTanggal.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(PenggajianInputNew.this,
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penggajian_input_new);
        CreateView();
        InitClass();
        EventClass();
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
            ListView.expandGroup(0);
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

    protected void LoadData(){
//        Loading.setMessage("Loading...");
//        Loading.setCancelable(false);
//        Loading.show();
//        String filter;
//        filter = "?id="+IdMst;
//        String url = route.URL_GET_PEGAWAI  + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    JSONObject obj = jsonArray.getJSONObject(0);
//                    txtNik.setText(obj.getString("nik"));
//                    txtNama.setText(obj.getString("nama"));
//                    txtAlamat.setText(obj.getString("alamat"));
//                    txtTelpon1.setText(obj.getString("no_telpon_1"));
//                    txtTelpon2.setText(obj.getString("no_telpon_2"));
//                    txtEmail.setText(obj.getString("email"));
//                    txtTglLahir.setText(FormatDateFromSql(obj.getString("tgl_lahir")));
//                    txtTglMulaiBekerja.setText(FormatDateFromSql(obj.getString("tgl_mulai_kerja")));
//                    lblGGajiPokok.setText(fmt.format(obj.getDouble("gaji_pokok")));
//                    txtKeterangan.setText(obj.getString("keterangan"));
//                    lblGUangIkatan.setText(fmt.format(obj.getDouble("uang_ikatan")));
//                    lblGUangKehadiran.setText(fmt.format(obj.getDouble("uang_kehadiran")));
//                    lblGPremiHarian.setText(fmt.format(obj.getDouble("premi_harian")));
//                    lblGPremiPerjam.setText(fmt.format(obj.getDouble("premi_perjam")));
//                    GajiTerakhir = obj.getDouble("gaji_pokok");
//                    LoadDetail();
//                    Loading.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
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
    }

    protected void LoadDetail(){
//        String filter;
//        filter = "?id_pegawai="+IdMst;
//        String url = route.URL_DET_TUNJANGAN_PEGAWAI_GET_PEGAWAI + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                TunjanganModel Data;
//                try {
//                    JSONArray jsonArrayDetail = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArrayDetail.length(); i++) {
//                        JSONObject objDetail = jsonArrayDetail.getJSONObject(i);
//                        Data = new TunjanganModel(
//                                objDetail.getInt("id_tunjangan"),
//                                objDetail.getString("kode"),
//                                objDetail.getString("nama"),
//                                "",
//                                ""
//                        );
//                        Data.setJumlah(objDetail.getDouble("jumlah"));
//                        ArListTunjangan.add(Data);
//                    }
//                    ListAdapter.notifyDataSetChanged();
//                    ListView.getLayoutParams().height = 200 * ArListTunjangan.size() + 150;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
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
    }

    protected void IsSaved(){
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PENGGAJIAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                JSONArray ArParms = new JSONArray();
                for(int i=0; i < ArListTunjangan.size() ;i++){
                    JSONObject obj= new JSONObject();
                    try {
                        obj.put("id_pegawai", String.valueOf(IdPegawai));
                        obj.put("telat_satu", String.valueOf(txtTelat1.getText().toString()));
                        obj.put("telat_dua", String.valueOf(txtTelat2.getText().toString()));
                        obj.put("dokter", String.valueOf(txtDokter.getText().toString()));
                        obj.put("izin_stgh_hari", String.valueOf(txtstghHari.getText().toString()));
                        obj.put("izin_cuti", String.valueOf(txtCuti.getText().toString()));
                        obj.put("izin_non_cuti", String.valueOf(txtNonCuti.getText().toString()));
                        obj.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
                        obj.put("user_id", String.valueOf(OrionPayrollApplication.getInstance().USER_LOGIN));
                        obj.put("gaji_pokok", String.valueOf(DataPegawai.getGaji_pokok()));
                        obj.put("uang_ikatan", String.valueOf( DataPegawai.getUang_ikatan()));
                        obj.put("uang_kehadiran", String.valueOf(DataPegawai.getUang_kehadiran()));
                        obj.put("premi_harian", String.valueOf(DataPegawai.getPremi_harian()));
                        obj.put("premi_perjam", String.valueOf(DataPegawai.getPremi_perjam()));
                        obj.put("jam_lembur", String.valueOf(StrFmtToDouble(txtLembur.getText().toString())));
                        obj.put("total_tunjangan", String.valueOf(StrFmtToDouble(lblTotTunjangan.getText().toString())));
                        obj.put("total_potongan", String.valueOf(StrFmtToDouble(lblTotPotongan.getText().toString())));
                        obj.put("total_lembur", String.valueOf(StrFmtToDouble(lblTotLembur.getText().toString())));
                        obj.put("total_kasbon", String.valueOf(StrFmtToDouble(lblTotKasbon.getText().toString())));
                        obj.put("total", String.valueOf(StrFmtToDouble(lblTotal.getText().toString())));
                        obj.put("tanggal", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
                        obj.put("tgl_input", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArParms.put(obj);
                }
                params.put("data", ArParms.toString());
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
                    Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", String.valueOf(Integer.toString(IdMst)));
//                params.put("nik", String.valueOf(txtNik.getText().toString()));
//                params.put("nama", String.valueOf(txtNama.getText().toString()));
//                params.put("alamat", String.valueOf(txtAlamat.getText().toString()));
//                params.put("no_telpon_1", String.valueOf(txtTelpon1.getText().toString()));
//                params.put("no_telpon_2", String.valueOf(txtTelpon2.getText().toString()));
//                params.put("email", String.valueOf(txtEmail.getText().toString()));
//                params.put("tgl_lahir", String.valueOf(FormatMySqlDate(txtTglLahir.getText().toString())));
//                params.put("tgl_mulai_kerja", String.valueOf(FormatMySqlDate(txtTglMulaiBekerja.getText().toString())));
//                params.put("gaji_pokok", String.valueOf(StrFmtToDouble(txtGajiPokok.getText().toString())));
//                params.put("status", String.valueOf(TRUE_STRING));
//                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                return params;

                Map<String, String> params = new HashMap<String, String>();
                JSONArray ArParms = new JSONArray();
//                for(int i=0; i < ArListTunjangan.size() ;i++){
//                    JSONObject obj= new JSONObject();
//                    try {
//                        obj.put("id", String.valueOf(Integer.toString(IdMst)));
//                        obj.put("nik", String.valueOf(txtNik.getText().toString()));
//                        obj.put("nama", String.valueOf(txtNama.getText().toString()));
//                        obj.put("alamat", String.valueOf(txtAlamat.getText().toString()));
//                        obj.put("no_telpon_1", String.valueOf(txtTelpon1.getText().toString()));
//                        obj.put("no_telpon_2", String.valueOf(txtTelpon2.getText().toString()));
//                        obj.put("email", String.valueOf(txtEmail.getText().toString()));
//                        obj.put("tgl_lahir", String.valueOf(FormatMySqlDate(txtTglLahir.getText().toString())));
//                        obj.put("tgl_mulai_kerja", String.valueOf(FormatMySqlDate(txtTglMulaiBekerja.getText().toString())));
//                        obj.put("gaji_pokok", String.valueOf(StrFmtToDouble(lblGGajiPokok.getText().toString())));
//                        obj.put("status", String.valueOf(TRUE_STRING));
//                        obj.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                        obj.put("uang_ikatan", String.valueOf(StrFmtToDouble(lblGUangIkatan.getText().toString())));
//                        obj.put("uang_kehadiran", String.valueOf(StrFmtToDouble(lblGUangKehadiran.getText().toString())));
//                        obj.put("premi_harian", String.valueOf(StrFmtToDouble(lblGPremiHarian.getText().toString())));
//                        obj.put("premi_perjam", String.valueOf(StrFmtToDouble(lblGPremiPerjam.getText().toString())));
//                        obj.put("tanggal", String.valueOf(FormatMySqlDate(FungsiGeneral.serverNowFormated())));
//
//                        obj.put("id_tunjangan", String.valueOf(ArListTunjangan.get(i).getId()));
//                        obj.put("jumlah", String.valueOf(ArListTunjangan.get(i).getJumlah()));
//
//                        if (GajiTerakhir != StrFmtToDouble(lblGGajiPokok.getText().toString())){
//                            obj.put("save_histori", String.valueOf(TRUE_STRING));
//                        }else{
//                            obj.put("save_histori", String.valueOf(FALSE_STRING));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ArParms.put(obj);
//                }
                params.put("data", ArParms.toString());
                return params;
            }
        };
        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
    }


    protected boolean IsValid(){
        if (this.txtPegawai.getText().toString().trim().equals("")) {
            txtPegawai.setFocusableInTouchMode(true);
            txtPegawai.requestFocus();
            txtPegawai.setError("Pegawai belum dipilih");
            txtPegawai.setFocusableInTouchMode(false);
            return false;
        }

        if (ArListTunjangan.size() == 0) {
            Toast.makeText(PenggajianInputNew.this, "Belum ada tunjangan yang diinput", Toast.LENGTH_SHORT).show();
            return false;
        }

//        for(int i=0; i < ArListTunjangan.size(); i++){
//            if (ArListTunjangan.get(i).getJumlah() == 0) {
//
//
//                return false;
//            }
//
//            for(int j=i+1; j < ArListTunjangan.size(); j++){
//                if (ArListTunjangan.get(i).getId() == ArListTunjangan.get(j).getId()) {
//                    Toast.makeText(PenggajianInputNew.this, ArListTunjangan.get(i).getNama()+" tidak boleh diinput > 1 kali", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
//        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV_TUNJANGAN) {
                Bundle extra = data.getExtras();
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(extra.getInt("id"));
                Data.setTipe(TIPE_DET_TUNJANGAN);
                Data.setCheck(false);

                ArListTunjangan.add(Data);
                ListAdapter.notifyDataSetChanged();

                if (ArListTunjangan.size() > 0 ){
                    ListView.expandGroup(0);
                }
                ListView.getLayoutParams().height = 200 * ArListTunjangan.size() + ArListPotongan.size() +  ArListKasbon.size() + 450;
            }else if (requestCode == RESULT_LOV_POTONGAN) {
                Bundle extra = data.getExtras();
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(extra.getInt("id"));
                Data.setTipe(TIPE_DET_POTONGAN);
                Data.setCheck(false);

                ArListPotongan.add(Data);
                ListAdapter.notifyDataSetChanged();

                if (ArListPotongan.size() > 0 ){
                    ListView.expandGroup(0);
                }
                ListView.getLayoutParams().height = 200 * ArListTunjangan.size() + ArListPotongan.size() +  ArListKasbon.size() + 450;
            }
        }
    }
}