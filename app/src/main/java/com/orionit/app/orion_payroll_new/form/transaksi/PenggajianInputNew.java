package com.orionit.app.orion_payroll_new.form.transaksi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.DetailTunjanganPegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.KasbonPegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.PenggajianDetailTable;
import com.orionit.app.orion_payroll_new.database.master.PenggajianTable;
import com.orionit.app.orion_payroll_new.form.adapter.ExpandListAdapterPenggajianNew;
import com.orionit.app.orion_payroll_new.form.lov.lov_pegawai;
import com.orionit.app.orion_payroll_new.models.DetailTunjanganPegawaiModel;
import com.orionit.app.orion_payroll_new.models.KasbonPegawaiModel;
import com.orionit.app.orion_payroll_new.models.PegawaiModel;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;
import com.orionit.app.orion_payroll_new.models.PenggajianModel;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orionit.app.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_DOKTER;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_IZIN_NON_CUTI;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_IZIN_STGH_HARI;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_TELAT_15;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_PT_TELAT_LBH_15;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_TJ_INSENTIF;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_TJ_LEMBUR;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV_KASBON;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV_PEGAWAI;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV_POTONGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV_TUNJANGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TDP_KASBON;
import static com.orionit.app.orion_payroll_new.models.JCons.TDP_POTONGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TDP_TUNJANGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_KASBON;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_POTONGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_TUNJANGAN;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.Round;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrToDeobleDef;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrToIntDef;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getMillisDateFmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowFormated;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;
import static com.orionit.app.orion_payroll_new.utility.route.URL_UPDATE_PEGAWAI;

public class PenggajianInputNew extends AppCompatActivity {
    private TextInputEditText txtNomor, txtTanggal, txtPegawai, txtTelat1, txtTelat2, txtDokter, txtNonCuti,
            txtstghHari, txtCuti, txtLembur, txtKeterangan, txtPeriode;
    private TextView lblGajiPokok, lblTotTunjangan, lblTotPotongan, lblTotKasbon, lblTotLembur, lblTotal, lblPilihTunjangan, lblPilihPotongan, lblPilikKasbon, lblCaptionTotalKasbon;
    private Button btnSimpan;

    private ImageButton btnTunjangan, btnPotongan, btnKasbon;

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
    public List<PenggajianDetailModel> ArListKasbonTmp;
    private TunjanganModel DtTunjangan;

    private PegawaiModel DataPegawai;
    public HashMap<String, KasbonPegawaiModel> HashKasbon;

    private static PenggajianInputNew mInstance;

    private PenggajianTable TData;

    protected void CreateView(){
        txtNomor      = (TextInputEditText) findViewById(R.id.txtNomor);
        txtPeriode    = (TextInputEditText) findViewById(R.id.txtPeriode);
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

        btnTunjangan = (ImageButton) findViewById(R.id.btnTunjangan);
        btnPotongan  = (ImageButton) findViewById(R.id.btnPotongan);
        btnKasbon    = (ImageButton) findViewById(R.id.btnKasbon);


        btnSimpan     = (Button) findViewById(R.id.btnSimpan);
        ListView      = (ExpandableListView)findViewById(R.id.ExpLv);
        txtTmp        = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA

        lblGajiPokok    = (TextView) findViewById(R.id.lblGajiPokok);
        lblTotTunjangan = (TextView) findViewById(R.id.lblTotTunjangan);
        lblTotPotongan  = (TextView) findViewById(R.id.lblTotPotongan);
        lblTotKasbon    = (TextView) findViewById(R.id.lblTotKasbon);
        lblTotLembur    = (TextView) findViewById(R.id.lblTotLembur);
        lblTotal        = (TextView) findViewById(R.id.lblTotal);
        lblCaptionTotalKasbon = (TextView) findViewById(R.id.lblCaptionTotalKasbon);

        //lblPilihTunjangan = (TextView) findViewById(R.id.lblPilihTunjangan);
//        lblPilihPotongan  = (TextView) findViewById(R.id.lblPilihPotongan);
//        lblPilikKasbon    = (TextView) findViewById(R.id.lblPilihKasbon);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.IdMst = extra.getInt("ID");
        Loading = new ProgressDialog(PenggajianInputNew.this);
        DataPegawai = new PegawaiModel();
        DtTunjangan = new TunjanganModel();
        TData = new PenggajianTable(getApplicationContext());

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Penggajian");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Penggajian");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Penggajian");
            this.txtNomor.setVisibility(View.GONE);
        };

        ListDataHeader = new ArrayList<>();
        ListHash = new HashMap<>();
        ListDataHeader.add("TUNJANGAN");
        ListDataHeader.add("POTONGAN");
        ListDataHeader.add("KASBON");

        ArListTunjangan = new ArrayList<>();
        ArListPotongan  = new ArrayList<>();
        ArListKasbon    = new ArrayList<>();
        ArListKasbonTmp = new ArrayList<>();

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
        txtPeriode.setEnabled(Enabled);

        txtTmp.setVisibility(View.INVISIBLE);

        txtTelat1.setText("0");
        txtTelat2.setText("0");
        txtDokter.setText("0");
        txtNonCuti.setText("0");
        txtCuti.setText("0");
        txtstghHari.setText("0");
        txtLembur.setText("0");

        lblGajiPokok.setText("0");
        lblTotTunjangan.setText("0");
        lblTotPotongan.setText("0");
        lblTotKasbon.setText("0");
        lblTotLembur.setText("0");
        lblTotal.setText("0");
        IdPegawai = 0;

        txtTanggal.setText(serverNowFormated());
        txtPeriode.setText(getTglFormatCustom(serverNowStartOfTheMonthLong(), "MMMM yyyy"));
    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTmp.setVisibility(View.VISIBLE);
                txtTmp.requestFocus();
                txtTmp.setVisibility(View.INVISIBLE);

                if (IsValid() == true){
                    AlertDialog.Builder bld = new AlertDialog.Builder(PenggajianInputNew.this);
                    bld.setTitle("Konfirmasi");
                    bld.setCancelable(true);
                    bld.setMessage(MSG_SAVE_CONFIRMATION);

                    bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (IsValid() == true){
                                if(Mode.equals(EDIT_MODE)){
                                    if (IsSavedEdit()){
                                        Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }else{
                                        Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    if (IsSaved()){
                                        Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }else{
                                        Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
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

        txtPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(PenggajianInputNew.this);
                if (txtPeriode.getText().toString().equals("")){
                    txtPeriode.setText(serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy");
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
                                format = new SimpleDateFormat("MMMM yyyy");
                                txtPeriode.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                txtPeriode.setError(null);
            }
        });

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(PenggajianInputNew.this);
                if (txtTanggal.getText().toString().equals("")){
                    txtTanggal.setText(serverNowFormated());
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

        txtPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdPegawai = 0;
                DataPegawai = new PegawaiModel();
                DtTunjangan = new TunjanganModel();
                txtPegawai.setText("");
                txtPegawai.setError(null);
                txtTelat1.setText("0");
                txtTelat2.setText("0");
                txtDokter.setText("0");
                txtNonCuti.setText("0");
                txtCuti.setText("0");
                txtstghHari.setText("0");
                txtLembur.setText("0");
                lblGajiPokok.setText("0");
                ArListTunjangan.clear();
                ArListPotongan.clear();
                ArListKasbon.clear();
                HitungDetail();
                HitungTotal();

                Intent s = new Intent(PenggajianInputNew.this, lov_pegawai.class);
                PenggajianInputNew.this.startActivityForResult(s, RESULT_LOV_PEGAWAI);
            }
        });


        txtTelat1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SetPotonganKhusus(ID_PT_TELAT_15);
                }
            }
        });

        txtTelat2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SetPotonganKhusus(ID_PT_TELAT_LBH_15);
                }
            }
        });

        txtDokter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SetPotonganKhusus(ID_PT_DOKTER);
                }
            }
        });

        txtstghHari.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SetPotonganKhusus(ID_PT_IZIN_STGH_HARI);
                }
            }
        });

        txtNonCuti.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SetPotonganKhusus(ID_PT_IZIN_NON_CUTI);
                }
            }
        });

        txtLembur.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SetTunjanganKhusus(ID_TJ_LEMBUR);
                }
            }
        });

        btnTunjangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostFocus();
                Intent s = new Intent(PenggajianInputNew.this, PilihTunjanganPenggajian.class);
                s.putExtra("MODE",Mode);
                s.putExtra("JANGANMUNCULKANSYSTEM",true);
                startActivityForResult(s, RESULT_LOV_TUNJANGAN);
            }
        });

        btnPotongan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostFocus();
                Intent s = new Intent(PenggajianInputNew.this, PilihPotonganPenggajian.class);
                s.putExtra("MODE",Mode);
                startActivityForResult(s, RESULT_LOV_POTONGAN);
            }
        });

        btnKasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostFocus();
                Intent s = new Intent(PenggajianInputNew.this, PilihKasbonPenggajian.class);
                s.putExtra("MODE",Mode);
                startActivityForResult(s, RESULT_LOV_KASBON);
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
        mInstance = this;
        if ((Mode.equals(EDIT_MODE)) || (Mode.equals(DETAIL_MODE))){
            LoadData();
        }
    }

    public static synchronized PenggajianInputNew getInstance() {
        return mInstance;
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
        Loading.setMessage("Loading...");
        Loading.setCancelable(false);
        Loading.show();

        PenggajianModel data = TData.GetData(IdMst);

        IdPegawai = data.getId_pegawai();
        txtPeriode.setText(getTglFormatCustom(data.getPeriode(), "MMMM yyyy"));
        txtNomor.setText(data.getNomor());
        txtTanggal.setText(getTglFormat(data.getTanggal()));
        txtPegawai.setText(Get_Nama_Master_Pegawai(data.getId_pegawai()));
        txtTelat1.setText(String.valueOf(data.getTelat_satu()));
        txtTelat2.setText(String.valueOf(data.getTelat_dua()));
        txtDokter.setText(String.valueOf(data.getDokter()));
        txtstghHari.setText(String.valueOf(data.getIzin_stgh_hari()));
        txtCuti.setText(String.valueOf(data.getIzin_cuti()));
        txtNonCuti.setText(String.valueOf(data.getIzin_non_cuti()));
        txtKeterangan.setText(data.getKeterangan());
        txtLembur.setText(String.valueOf(data.getJam_lembur()));

        lblGajiPokok.setText(fmt.format(data.getGaji_pokok()));
        lblTotTunjangan.setText(fmt.format(data.getTotal_tunjangan()));
        lblTotPotongan.setText(fmt.format(data.getTotal_potongan()));
        lblTotLembur.setText(fmt.format(data.getTotal_lembur()));
        lblTotKasbon.setText(fmt.format(data.getTotal_kasbon()));
        lblTotal.setText(fmt.format(data.getTotal()));

        DataPegawai = new PegawaiModel(OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(IdPegawai)));
        LoadKasbonPegawai();
        LoadDetail();
        HitungDetail();
        HitungTotal();

        if (!Mode.equals(DETAIL_MODE)){
            if (ArListKasbon.size() > 0){
                lblCaptionTotalKasbon.setTextColor(getResources().getColor(R.color.colorSoftRed));
            }else {
                lblCaptionTotalKasbon.setTextColor(getResources().getColor(R.color.colorGrayDark));
            }
        };

        Loading.dismiss();
    }

    protected void LoadDetail(){
        PenggajianDetailTable TDataDet = new PenggajianDetailTable(getApplicationContext());
        List <PenggajianDetailModel> ArrDetail = new ArrayList<>();
        ArrDetail = TDataDet.GetArrList(IdMst);

        PenggajianDetailModel Data;
        for (int i = 0; i < ArrDetail.size(); i++){

            Data = new PenggajianDetailModel();
            Data.setTipe(ArrDetail.get(i).getTipe());
            Data.setId_tjg_pot_kas(ArrDetail.get(i).getId_tjg_pot_kas());
            Data.setJumlah(ArrDetail.get(i).getJumlah());

            if (ArrDetail.get(i).getTipe().equals(TIPE_DET_TUNJANGAN)){
                ArListTunjangan.add(Data);
            }else if (ArrDetail.get(i).getTipe().equals(TIPE_DET_POTONGAN)){
                ArListPotongan.add(Data);
            }else if (ArrDetail.get(i).getTipe().equals(TIPE_DET_KASBON)) {
                Data.setCheck(true);
                int idx = CekKasbonIdxKasbon(Data.getId_tjg_pot_kas());
                if (idx > -1){
                    ArListKasbon.get(idx).setSisa(ArListKasbon.get(idx).getSisa() + Data.getJumlah());
                    ArListKasbon.get(idx).setCheck(true);
                    ArListKasbon.get(idx).setJumlah(Data.getJumlah());
                }
            }
        }

        if (this.Mode.equals(DETAIL_MODE)){
            int i = 0;
            while ( i <= ArListKasbon.size()-1 ) {
                if (ArListKasbon.get(i).getJumlah() == 0) {
                    ArListKasbon.remove(i);
                    i--;
                }
                i++;
            }
        }
    }

//    protected void LoadDetail(){
//        String filter;
//        filter = "?id_master="+IdMst;
//        String url = route.URL_GET_PENGGAJIAN_DETAIL + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PenggajianDetailModel Data;
//                try {
//                    JSONArray jsonArrayDetail = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArrayDetail.length(); i++) {
//                        JSONObject objDetail = jsonArrayDetail.getJSONObject(i);
//                        Data = new PenggajianDetailModel();
//                        Data.setTipe(objDetail.getString("tipe"));
//                        Data.setId_tjg_pot_kas(objDetail.getInt("id_tjg_pot_kas"));
//                        Data.setJumlah(objDetail.getDouble("jumlah"));
//
//                        if (objDetail.getString("tipe").equals(TIPE_DET_TUNJANGAN)){
//                            ArListTunjangan.add(Data);
//                        }else if (objDetail.getString("tipe").equals(TIPE_DET_POTONGAN)){
//                            ArListPotongan.add(Data);
//                        }else if (objDetail.getString("tipe").equals(TIPE_DET_KASBON)) {
//                            Data.setCheck(true);
//
//                            if (CekKasbonTmpExist(Data.getId_tjg_pot_kas())){
//                                int idx = GetIdxArListKasbonTmp(Data.getId_tjg_pot_kas());
//                                Data.setNomor(ArListKasbonTmp.get(idx).getNomor());
//                                Data.setTanggal(ArListKasbonTmp.get(idx).getTanggal());
//                                Data.setTotal(ArListKasbonTmp.get(idx).getTotal());
//                                Data.setSisa(ArListKasbonTmp.get(idx).getSisa() + Data.getJumlah());
//                                Data.setLama_cicilan(ArListKasbonTmp.get(idx).getLama_cicilan());
//                            }
//                            ArListKasbon.add(Data);
//                        }
//                    }
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
//                Loading.dismiss();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

//    protected void IsSaved(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PENGGAJIAN, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                JSONArray ArParms = new JSONArray();
//                for(int i=0; i < ArListTunjangan.size() ;i++){
//                    JSONObject obj= new JSONObject();
//                    try {
//                        obj.put("id_pegawai", String.valueOf(DataPegawai.getId()));
//                        obj.put("telat_satu", String.valueOf(txtTelat1.getText().toString()));
//                        obj.put("telat_dua", String.valueOf(txtTelat2.getText().toString()));
//                        obj.put("dokter", String.valueOf(txtDokter.getText().toString()));
//                        obj.put("izin_stgh_hari", String.valueOf(txtstghHari.getText().toString()));
//                        obj.put("izin_cuti", String.valueOf(txtCuti.getText().toString()));
//                        obj.put("izin_non_cuti", String.valueOf(txtNonCuti.getText().toString()));
//                        obj.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                        obj.put("user_id", String.valueOf(OrionPayrollApplication.getInstance().USER_LOGIN));
//                        obj.put("gaji_pokok", String.valueOf(DataPegawai.getGaji_pokok()));
//                        obj.put("uang_ikatan", String.valueOf( DataPegawai.getUang_ikatan()));
//                        obj.put("uang_kehadiran", String.valueOf(DataPegawai.getUang_kehadiran()));
//                        obj.put("premi_harian", String.valueOf(DataPegawai.getPremi_harian()));
//                        obj.put("premi_perjam", String.valueOf(DataPegawai.getPremi_perjam()));
//                        obj.put("jam_lembur", String.valueOf(StrFmtToDouble(txtLembur.getText().toString())));
//                        obj.put("total_tunjangan", String.valueOf(StrFmtToDouble(lblTotTunjangan.getText().toString())));
//                        obj.put("total_potongan", String.valueOf(StrFmtToDouble(lblTotPotongan.getText().toString())));
//                        obj.put("total_lembur", String.valueOf(StrFmtToDouble(lblTotLembur.getText().toString())));
//                        obj.put("total_kasbon", String.valueOf(StrFmtToDouble(lblTotKasbon.getText().toString())));
//                        obj.put("total", String.valueOf(StrFmtToDouble(lblTotal.getText().toString())));
//                        obj.put("tanggal", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
//                        obj.put("tgl_input", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ArParms.put(obj);
//                }
//
//                params.put("data", ArParms.toString());
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//    }

    protected boolean IsSaved(){
        try {
            PenggajianModel Data = new PenggajianModel(
                    0,DataPegawai.getId(),
                    StrToIntDef(txtTelat1.getText().toString(),0),
                    StrToIntDef(txtTelat2.getText().toString(),0),
                    StrToIntDef(txtDokter.getText().toString(),0),
                    StrToIntDef(txtstghHari.getText().toString(),0),
                    StrToIntDef(txtCuti.getText().toString(),0),
                    StrToIntDef(txtNonCuti.getText().toString(),0),
                    TData.getNextNumber(getSimpleDate(txtTanggal.getText().toString())),
                    txtKeterangan.getText().toString().trim(),
                    OrionPayrollApplication.getInstance().USER_LOGIN,
                    "",
                    DataPegawai.getGaji_pokok(),
                    DataPegawai.getUang_ikatan(),
                    DataPegawai.getUang_kehadiran(),
                    DataPegawai.getPremi_harian(),
                    DataPegawai.getPremi_perjam(),
                    StrFmtToDouble(txtLembur.getText().toString()),
                    StrFmtToDouble(lblTotTunjangan.getText().toString()),
                    StrFmtToDouble(lblTotPotongan.getText().toString()),
                    StrFmtToDouble(lblTotLembur.getText().toString()),
                    StrFmtToDouble(lblTotKasbon.getText().toString()),
                    StrFmtToDouble(lblTotal.getText().toString()),
                    getSimpleDate(txtTanggal.getText().toString()),
                    serverNowLong(),
                    0,
                    StartOfTheMonthLong(getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy"))
            );
            int IdMaster = TData.Insert(Data);

            PenggajianDetailTable TDataDetail = new PenggajianDetailTable(getApplicationContext());
            PenggajianDetailModel DataDet;

            for(int i=0; i < ArListPotongan.size() ;i++){
                DataDet = new PenggajianDetailModel(0,
                        IdMaster,
                        ArListPotongan.get(i).getId_tjg_pot_kas(),
                        TDP_POTONGAN,
                        ArListPotongan.get(i).getJumlah());
                TDataDetail.Insert(DataDet);
            }

            for(int i=0; i < ArListTunjangan.size() ;i++){
                DataDet = new PenggajianDetailModel(0,
                        IdMaster,
                        ArListTunjangan.get(i).getId_tjg_pot_kas(),
                        TDP_TUNJANGAN,
                        ArListTunjangan.get(i).getJumlah());
                TDataDetail.Insert(DataDet);
            }

            KasbonPegawaiTable TKasbon = new KasbonPegawaiTable(getApplicationContext());

            for(int i=0; i < ArListKasbon.size() ;i++){
                if (ArListKasbon.get(i).getJumlah() > 0){
                    DataDet = new PenggajianDetailModel(0,
                        IdMaster,
                        ArListKasbon.get(i).getId_tjg_pot_kas(),
                        TDP_KASBON,
                        ArListKasbon.get(i).getJumlah());
                    TDataDetail.Insert(DataDet);
                    TKasbon.UpdateSisa(ArListKasbon.get(i).getId_tjg_pot_kas(), ArListKasbon.get(i).getJumlah(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean IsSavedEdit(){
        try {
            PenggajianDetailTable TDataDetail = new PenggajianDetailTable(getApplicationContext());
            KasbonPegawaiTable TKasbon = new KasbonPegawaiTable(getApplicationContext());

            List <PenggajianDetailModel> ArrDetail = new ArrayList<>();
            ArrDetail = TDataDetail.GetArrList(IdMst);

            TDataDetail.delete(IdMst);

            for (int i = 0; i < ArrDetail.size(); i++){
                if (ArrDetail.get(i).getTipe().equals(TIPE_DET_KASBON)) {
                    TKasbon.UpdateSisa(ArrDetail.get(i).getId_tjg_pot_kas(), ArrDetail.get(i).getJumlah(), true);
                }
            }

            PenggajianModel Data = new PenggajianModel(
                    IdMst,DataPegawai.getId(),
                    StrToIntDef(txtTelat1.getText().toString(),0),
                    StrToIntDef(txtTelat2.getText().toString(),0),
                    StrToIntDef(txtDokter.getText().toString(),0),
                    StrToIntDef(txtstghHari.getText().toString(),0),
                    StrToIntDef(txtCuti.getText().toString(),0),
                    StrToIntDef(txtNonCuti.getText().toString(),0),
                    TData.getNextNumber(getSimpleDate(txtTanggal.getText().toString())),
                    txtKeterangan.getText().toString().trim(),
                    OrionPayrollApplication.getInstance().USER_LOGIN,
                    "",
                    DataPegawai.getGaji_pokok(),
                    DataPegawai.getUang_ikatan(),
                    DataPegawai.getUang_kehadiran(),
                    DataPegawai.getPremi_harian(),
                    DataPegawai.getPremi_perjam(),
                    StrFmtToDouble(txtLembur.getText().toString()),
                    StrFmtToDouble(lblTotTunjangan.getText().toString()),
                    StrFmtToDouble(lblTotPotongan.getText().toString()),
                    StrFmtToDouble(lblTotLembur.getText().toString()),
                    StrFmtToDouble(lblTotKasbon.getText().toString()),
                    StrFmtToDouble(lblTotal.getText().toString()),
                    getSimpleDate(txtTanggal.getText().toString()),
                    serverNowLong(),
                    serverNowLong(),
                    StartOfTheMonthLong(getMillisDateFmt(txtPeriode.getText().toString(), "MMMM yyyy"))
            );
            TData.Update(Data);

            PenggajianDetailModel DataDet;

            for(int i=0; i < ArListPotongan.size() ;i++){
                DataDet = new PenggajianDetailModel(0,
                        IdMst,
                        ArListPotongan.get(i).getId_tjg_pot_kas(),
                        TDP_POTONGAN,
                        ArListPotongan.get(i).getJumlah());
                TDataDetail.Insert(DataDet);
            }

            for(int i=0; i < ArListTunjangan.size() ;i++){
                DataDet = new PenggajianDetailModel(0,
                        IdMst,
                        ArListTunjangan.get(i).getId_tjg_pot_kas(),
                        TDP_TUNJANGAN,
                        ArListTunjangan.get(i).getJumlah());
                TDataDetail.Insert(DataDet);
            }

            for(int i=0; i < ArListKasbon.size() ;i++){
                if (ArListKasbon.get(i).getJumlah() > 0){
                    DataDet = new PenggajianDetailModel(0,
                            IdMst,
                            ArListKasbon.get(i).getId_tjg_pot_kas(),
                            TDP_KASBON,
                            ArListKasbon.get(i).getJumlah());
                    TDataDetail.Insert(DataDet);
                    TKasbon.UpdateSisa(ArListKasbon.get(i).getId_tjg_pot_kas(), ArListKasbon.get(i).getJumlah(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    protected void IsSaved(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PENGGAJIAN, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                JSONObject JsonMasterDet = new JSONObject();
//                JSONArray JsonDetail = new JSONArray();
//
//                JSONObject JsonMaster= new JSONObject();
//                try {
//                    JsonMaster.put("id_pegawai", String.valueOf(DataPegawai.getId()));
//                    JsonMaster.put("telat_satu", String.valueOf(txtTelat1.getText().toString()));
//                    JsonMaster.put("telat_dua", String.valueOf(txtTelat2.getText().toString()));
//                    JsonMaster.put("dokter", String.valueOf(txtDokter.getText().toString()));
//                    JsonMaster.put("izin_stgh_hari", String.valueOf(txtstghHari.getText().toString()));
//                    JsonMaster.put("izin_cuti", String.valueOf(txtCuti.getText().toString()));
//                    JsonMaster.put("izin_non_cuti", String.valueOf(txtNonCuti.getText().toString()));
//                    JsonMaster.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
//                    JsonMaster.put("user_id", String.valueOf(OrionPayrollApplication.getInstance().USER_LOGIN));
//                    JsonMaster.put("gaji_pokok", String.valueOf(DataPegawai.getGaji_pokok()));
//                    JsonMaster.put("uang_ikatan", String.valueOf( DataPegawai.getUang_ikatan()));
//                    JsonMaster.put("uang_kehadiran", String.valueOf(DataPegawai.getUang_kehadiran()));
//                    JsonMaster.put("premi_harian", String.valueOf(DataPegawai.getPremi_harian()));
//                    JsonMaster.put("premi_perjam", String.valueOf(DataPegawai.getPremi_perjam()));
//                    JsonMaster.put("jam_lembur", String.valueOf(StrFmtToDouble(txtLembur.getText().toString())));
//                    JsonMaster.put("total_tunjangan", String.valueOf(StrFmtToDouble(lblTotTunjangan.getText().toString())));
//                    JsonMaster.put("total_potongan", String.valueOf(StrFmtToDouble(lblTotPotongan.getText().toString())));
//                    JsonMaster.put("total_lembur", String.valueOf(StrFmtToDouble(lblTotLembur.getText().toString())));
//                    JsonMaster.put("total_kasbon", String.valueOf(StrFmtToDouble(lblTotKasbon.getText().toString())));
//                    JsonMaster.put("total", String.valueOf(StrFmtToDouble(lblTotal.getText().toString())));
//                    JsonMaster.put("tanggal", String.valueOf(FormatMySqlDate(txtTanggal.getText().toString())));
//                    JsonMaster.put("periode", String.valueOf(FormatMySqlDatePeriode(txtPeriode.getText().toString()))); //sementara tutu dulu
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                for(int i=0; i < ArListTunjangan.size() ;i++){
//                    JSONObject obj= new JSONObject();
//                    try {
//                        obj.put("tipe", String.valueOf(TDP_TUNJANGAN));
//                        obj.put("id_tjg_pot_kas", String.valueOf(ArListTunjangan.get(i).getId_tjg_pot_kas()));
//                        obj.put("jumlah", String.valueOf(ArListTunjangan.get(i).getJumlah()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JsonDetail.put(obj);
//                }
//
//                for(int i=0; i < ArListPotongan.size() ;i++){
//                    JSONObject obj= new JSONObject();
//                    try {
//                        obj.put("tipe", String.valueOf(TDP_POTONGAN));
//                        obj.put("id_tjg_pot_kas", String.valueOf(ArListPotongan.get(i).getId_tjg_pot_kas()));
//                        obj.put("jumlah", String.valueOf(ArListPotongan.get(i).getJumlah()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JsonDetail.put(obj);
//                }
//
//                for(int i=0; i < ArListKasbon.size() ;i++){
//                    if (ArListKasbon.get(i).getJumlah() > 0){
//                        JSONObject obj= new JSONObject();
//                        try {
//                            obj.put("tipe", String.valueOf(TDP_KASBON));
//                            obj.put("id_tjg_pot_kas", String.valueOf(ArListKasbon.get(i).getId_tjg_pot_kas()));
//                            obj.put("jumlah", String.valueOf(ArListKasbon.get(i).getJumlah()));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        JsonDetail.put(obj);
//                    }
//                }
//
//                try {
//                    JsonMasterDet.put("master", JsonMaster);
//                    JsonMasterDet.put("detail", JsonDetail);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                params.put("data", JsonMasterDet.toString());
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//    }



    //    String message;
//    JSONObject json = new JSONObject();
//
//
//        try {
//        json.put("name", String.valueOf(txtDokter.getText())) ;
//
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//
//    JSONArray array = new JSONArray();
//    JSONObject item = new JSONObject();
//        item.put("information", "test");
//        item.put("id", 3);
//        item.put("name", "course1");
//        array.add(item);
//
//        json.put("course", array);
//
//    message = json.toString();
//
//    protected boolean IsSavedEdit(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_PEGAWAI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(PenggajianInputNew.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("id", String.valueOf(Integer.toString(IdMst)));
////                params.put("nik", String.valueOf(txtNik.getText().toString()));
////                params.put("nama", String.valueOf(txtNama.getText().toString()));
////                params.put("alamat", String.valueOf(txtAlamat.getText().toString()));
////                params.put("no_telpon_1", String.valueOf(txtTelpon1.getText().toString()));
////                params.put("no_telpon_2", String.valueOf(txtTelpon2.getText().toString()));
////                params.put("email", String.valueOf(txtEmail.getText().toString()));
////                params.put("tgl_lahir", String.valueOf(FormatMySqlDate(txtTglLahir.getText().toString())));
////                params.put("tgl_mulai_kerja", String.valueOf(FormatMySqlDate(txtTglMulaiBekerja.getText().toString())));
////                params.put("gaji_pokok", String.valueOf(StrFmtToDouble(txtGajiPokok.getText().toString())));
////                params.put("status", String.valueOf(TRUE_STRING));
////                params.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
////                return params;
//
//                Map<String, String> params = new HashMap<String, String>();
//                JSONArray ArParms = new JSONArray();
////                for(int i=0; i < ArListTunjangan.size() ;i++){
////                    JSONObject obj= new JSONObject();
////                    try {
////                        obj.put("id", String.valueOf(Integer.toString(IdMst)));
////                        obj.put("nik", String.valueOf(txtNik.getText().toString()));
////                        obj.put("nama", String.valueOf(txtNama.getText().toString()));
////                        obj.put("alamat", String.valueOf(txtAlamat.getText().toString()));
////                        obj.put("no_telpon_1", String.valueOf(txtTelpon1.getText().toString()));
////                        obj.put("no_telpon_2", String.valueOf(txtTelpon2.getText().toString()));
////                        obj.put("email", String.valueOf(txtEmail.getText().toString()));
////                        obj.put("tgl_lahir", String.valueOf(FormatMySqlDate(txtTglLahir.getText().toString())));
////                        obj.put("tgl_mulai_kerja", String.valueOf(FormatMySqlDate(txtTglMulaiBekerja.getText().toString())));
////                        obj.put("gaji_pokok", String.valueOf(StrFmtToDouble(lblGGajiPokok.getText().toString())));
////                        obj.put("status", String.valueOf(TRUE_STRING));
////                        obj.put("keterangan", String.valueOf(txtKeterangan.getText().toString()));
////                        obj.put("uang_ikatan", String.valueOf(StrFmtToDouble(lblGUangIkatan.getText().toString())));
////                        obj.put("uang_kehadiran", String.valueOf(StrFmtToDouble(lblGUangKehadiran.getText().toString())));
////                        obj.put("premi_harian", String.valueOf(StrFmtToDouble(lblGPremiHarian.getText().toString())));
////                        obj.put("premi_perjam", String.valueOf(StrFmtToDouble(lblGPremiPerjam.getText().toString())));
////                        obj.put("tanggal", String.valueOf(FormatMySqlDate(FungsiGeneral.serverNowFormated())));
////
////                        obj.put("id_tunjangan", String.valueOf(ArListTunjangan.get(i).getId()));
////                        obj.put("jumlah", String.valueOf(ArListTunjangan.get(i).getJumlah()));
////
////                        if (GajiTerakhir != StrFmtToDouble(lblGGajiPokok.getText().toString())){
////                            obj.put("save_histori", String.valueOf(TRUE_STRING));
////                        }else{
////                            obj.put("save_histori", String.valueOf(FALSE_STRING));
////                        }
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                    ArParms.put(obj);
////                }
//                params.put("data", ArParms.toString());
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//        return true;
//    }

//    protected void LoadTunjanganPegawai(){
//        String filter;
//        filter = "?id_pegawai="+DataPegawai.getId();
//        String url = route.URL_DET_TUNJANGAN_PEGAWAI_GET_PEGAWAI + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PenggajianDetailModel Data;
//                try {
//                    JSONArray jsonArrayDetail = response.getJSONArray("data");
//                    DtTunjangan = new TunjanganModel();
//                    for (int i = 0; i < jsonArrayDetail.length(); i++) {
//                        JSONObject objDetail = jsonArrayDetail.getJSONObject(i);
//                        Data = new PenggajianDetailModel();
//                        Data.setId_tjg_pot_kas(objDetail.getInt("id_tunjangan"));
//                        Data.setJumlah(objDetail.getDouble("jumlah"));
//                        ArListTunjangan.add(Data);
//
//                        if (Data.getId_tjg_pot_kas() == ID_TJ_INSENTIF){
//                            DtTunjangan.setId(Data.getId_tjg_pot_kas());
//                            DtTunjangan.setJumlah(Data.getJumlah());
//                        }
//                    }
//                    HitungDetail();
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
//                Loading.dismiss();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

    protected void LoadTunjanganPegawai(){
        DetailTunjanganPegawaiTable DtTjDet = new DetailTunjanganPegawaiTable(getApplicationContext());
        List<DetailTunjanganPegawaiModel> ListData =  DtTjDet.GetListData(DataPegawai.getId());
        DtTunjangan = new TunjanganModel();
        PenggajianDetailModel Data;
        for (int i = 0; i < ListData.size(); i++) {
            Data = new PenggajianDetailModel();
            Data.setId_tjg_pot_kas(ListData.get(i).getId_tunjangan());
            Data.setJumlah(ListData.get(i).getJumlah());
            ArListTunjangan.add(Data);

            if (Data.getId_tjg_pot_kas() == ID_TJ_INSENTIF){
                DtTunjangan.setId(Data.getId_tjg_pot_kas());
                DtTunjangan.setJumlah(Data.getJumlah());
            }
        }
        HitungDetail();
    }

//    public void LoadKasbonPegawai(){
//        String filter = "";
//        filter = "?status=1&id_pegawai="+Integer.toString(DataPegawai.getId());
//        String url = route.URL_SELECT_KASBON_4_PENGGAJIAN + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PenggajianDetailModel Data;
//                ArListKasbon.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new PenggajianDetailModel();
//                        Data.setId_tjg_pot_kas(obj.getInt("id"));
//                        Data.setNomor(obj.getString("nomor"));
//                        Data.setTanggal(getMillisDate(FormatDateFromSql(obj.getString("tanggal"))));
//                        Data.setSisa(obj.getDouble("sisa"));
//                        Data.setLama_cicilan(obj.getInt("cicilan"));
//                        Data.setTotal(obj.getDouble("jumlah"));
//                        ArListKasbon.add(Data);
//                    }
//                    HitungDetail();
//                    HitungTotal();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

    public void LoadKasbonPegawai(){
        KasbonPegawaiTable DTKasbon = new KasbonPegawaiTable(getApplicationContext());
        ArListKasbon = DTKasbon.Load4Penggajian(getSimpleDate(txtTanggal.getText().toString()), IdPegawai,1,IdMst);
    }

//    public void LoadKasbonPegawaiEdit(int id_pegawai){
//        String filter = "";
//        filter = "?status=0&id_pegawai="+Integer.toString(id_pegawai);
//        String url = route.URL_SELECT_KASBON_4_PENGGAJIAN + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                PenggajianDetailModel Data;
//                ArListKasbonTmp.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new PenggajianDetailModel();
//                        Data.setId_tjg_pot_kas(obj.getInt("id"));
//                        Data.setNomor(obj.getString("nomor"));
//                        Data.setTanggal(getMillisDate(FormatDateFromSql(obj.getString("tanggal"))));
//                        Data.setSisa(obj.getDouble("sisa"));
//                        Data.setLama_cicilan(obj.getInt("cicilan"));
//                        Data.setTotal(obj.getDouble("jumlah"));
//                        ArListKasbonTmp.add(Data);
//                    }
//                    LoadDetail();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }

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

    protected boolean CekPotonganExist (int id){
        for(int i=0; i < ArListPotongan.size(); i++){
            if (ArListPotongan.get(i).getId_tjg_pot_kas() == id){
                return true;
            }
        }
        return false;
    }

    protected int GetIdxArListPotongan (int id){
        for(int i=0; i < ArListPotongan.size(); i++){
            if (ArListPotongan.get(i).getId_tjg_pot_kas() == id){
                return i;
            }
        }
        return -1;
    }

    protected void SetPotonganKhusus (int Id){
        int banyak   = 0;
        Double jumlah = 0.0;
        Boolean tambahkan = false;
        if (DataPegawai != null) {
            if (DataPegawai.getId() > 0) {
                if (Id == ID_PT_TELAT_15) {
                    banyak = StrToIntDef(txtTelat1.getText().toString(),0);
                    if (banyak > 5) {
                        //jumlah = (banyak - 5) * Round(DataPegawai.getPremi_harian() * 0.25,0);
                        jumlah = Round((banyak - 5) * (DataPegawai.getUang_kehadiran() / 21) * 0.25,0);
                        tambahkan = true;
                    }
                } else if (Id == ID_PT_TELAT_LBH_15) {
                    banyak    = StrToIntDef(txtTelat2.getText().toString(),0);
                    //jumlah    =  Round((banyak) * (DataPegawai.getPremi_harian() * 0.25), 0);
                    jumlah    =  Round((banyak) * ((DataPegawai.getUang_kehadiran() / 21) * 0.25), 0);
                    tambahkan = banyak > 0;
                } else if (Id == ID_PT_IZIN_STGH_HARI) {
                    banyak   = StrToIntDef(txtstghHari.getText().toString(),0);
                    //jumlah    = Round((banyak) * (DataPegawai.getPremi_harian() * 0.5), 0);
                    jumlah    = Round((banyak) * ((DataPegawai.getUang_kehadiran() / 21) * 0.5), 0);
                    tambahkan = banyak > 0;
                } else if (Id == ID_PT_IZIN_NON_CUTI) {
                    banyak    = StrToIntDef(txtNonCuti.getText().toString(),0);
//                    jumlah    = (banyak) * (DataPegawai.getPremi_harian());
                    jumlah    = Round((banyak) * (DataPegawai.getUang_kehadiran() / 21), 0);
                    tambahkan = banyak > 0;
                }
            }
        }

        if (tambahkan){
            if (CekPotonganExist(Id) == false){
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(Id);
                Data.setJumlah(jumlah);
                ArListPotongan.add(Data);
            }else{
                ArListPotongan.get(GetIdxArListPotongan(Id)).setJumlah(jumlah);
            }
        }else{
            int idx = GetIdxArListPotongan(Id);
            if (idx > -1){
                ArListPotongan.remove(idx);
            }
        }

        SetInsentif(); // Untuj set perlu ada insentif atau ga perlu

        HitungDetail();
        HitungTotal();
    }

    protected boolean CekTunjanganExist (int id){
        for(int i=0; i < ArListTunjangan.size(); i++){
            if (ArListTunjangan.get(i).getId_tjg_pot_kas() == id){
                return true;
            }
        }
        return false;
    }

    protected int GetIdxArListTunjangan (int id){
        for(int i=0; i < ArListTunjangan.size(); i++){
            if (ArListTunjangan.get(i).getId_tjg_pot_kas() == id){
                return i;
            }
        }
        return -1;
    }

    protected void SetTunjanganKhusus (int Id){
        int banyak    = 0;
        Double jumlah = 0.0;
        Boolean tambahkan = false;
        if (DataPegawai != null) {
            if (DataPegawai.getId() > 0) {
                if (Id == ID_TJ_LEMBUR) {
                    banyak    = StrToIntDef(txtLembur.getText().toString(),0);
                    //jumlah    = Round((banyak * DataPegawai.getPremi_perjam()) * 1.5,0);
                    jumlah    = Round((  (banyak * (DataPegawai.getUang_kehadiran() / 21) /8)) * 1.5,0);
                    tambahkan = banyak > 0;
                }
            }
        }

        if (tambahkan){
            if (CekTunjanganExist(Id) == false){
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(Id);
                Data.setJumlah(jumlah);
                ArListTunjangan.add(Data);
            }else{
                ArListTunjangan.get(GetIdxArListTunjangan(Id)).setJumlah(jumlah);
            }
        }else{
            int idx = GetIdxArListTunjangan(Id);
            if (idx > -1){
                ArListTunjangan.remove(idx);
            }
        }

        HitungDetail();
        HitungTotal();
    }

    protected void SetInsentif(){
        boolean IsHapus = false;

        int Telat1, Telat2, Dokter, IzinStghHari, IzinNonCuti;
        Telat1       = StrToIntDef(txtTelat1.getText().toString(),0);
        Telat2       = StrToIntDef(txtTelat2.getText().toString(),0);
        Dokter       = StrToIntDef(txtDokter.getText().toString(),0);
        IzinStghHari = StrToIntDef(txtstghHari.getText().toString(),0);
        IzinNonCuti  = StrToIntDef(txtNonCuti.getText().toString(),0);

        IsHapus      = ((Telat1 > 3) || (Telat2 > 1) || (Dokter > 1) ||(IzinStghHari > 1) || (IzinNonCuti > 0));

        if (IsHapus){
            if (CekTunjanganExist(ID_TJ_INSENTIF)) {
                int idx = GetIdxArListTunjangan(ID_TJ_INSENTIF);
                if (idx > -1) {
                    ArListTunjangan.remove(idx);
                }
            }
        }else {
            if (!CekTunjanganExist(ID_TJ_INSENTIF)) {
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(DtTunjangan.getId());
                Data.setJumlah(DtTunjangan.getJumlah());
                ArListTunjangan.add(Data);
            }
        }

    }

    protected boolean CekKasbonTmpExist (int id){
        for(int i=0; i < ArListKasbonTmp.size(); i++){
            if (ArListKasbonTmp.get(i).getId_tjg_pot_kas() == id){
                return true;
            }
        }
        return false;
    }

    protected int CekKasbonIdxKasbon (int id){
        for(int i=0; i < ArListKasbon.size(); i++){
            if (ArListKasbon.get(i).getId_tjg_pot_kas() == id){
                return i;
            }
        }
        return -1;
    }

    protected int GetIdxArListKasbonTmp (int id){
        for(int i=0; i < ArListKasbonTmp.size(); i++){
            if (ArListKasbonTmp.get(i).getId_tjg_pot_kas() == id){
                return i;
            }
        }
        return -1;
    }

    protected void HitungDetail(){
        Double TotalTJ = 0.0;
        Double TotalLembur = 0.0;
        for(int i=0; i < ArListTunjangan.size(); i++){
            if (ArListTunjangan.get(i).getId_tjg_pot_kas() != ID_TJ_LEMBUR){
                TotalTJ += ArListTunjangan.get(i).getJumlah();
            }else{
                TotalLembur += ArListTunjangan.get(i).getJumlah();
            }
        }

        Double TotalPT = 0.0;
        for(int i=0; i < ArListPotongan.size(); i++){
            TotalPT += ArListPotongan.get(i).getJumlah();
        }

        Double TotalKasbon = 0.0;
        for(int i=0; i < ArListKasbon.size(); i++){
            if (ArListKasbon.get(i).isCheck()){
                TotalKasbon += ArListKasbon.get(i).getJumlah();
            }
        }

        lblTotTunjangan.setText(fmt.format(TotalTJ));
        lblTotPotongan.setText(fmt.format(TotalPT));
        lblTotKasbon.setText(fmt.format(TotalKasbon));
        lblTotLembur.setText(fmt.format(TotalLembur));
    }

    protected void HitungTotal(){
        Double Total = 0.0;

        for(int i=0; i < ArListTunjangan.size(); i++){
            Total += ArListTunjangan.get(i).getJumlah();
        }

        for(int i=0; i < ArListPotongan.size(); i++){
            Total -= ArListPotongan.get(i).getJumlah();
        }

        for(int i=0; i < ArListKasbon.size(); i++){
            Total -= ArListKasbon.get(i).getJumlah();
        }
        Total += DataPegawai.getGaji_pokok();
        lblTotal.setText(fmt.format(Total));
    }

//    public void LoadAbsensi(){
//        String filter;
//        //filter = "?periode="+FormatMySqlDatePeriode(txtPeriode.getText().toString())+ "&id_pegawai="+Integer.toString(DataPegawai.getId());
//        filter = "?periode="+FormatMySqlDate(txtTanggal.getText().toString())+ "&id_pegawai="+Integer.toString(DataPegawai.getId());
//        String url = route.URL_GET_ABSEN_PEGAWAI_4_PENGGAJIAN + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                AbsenPegawaiModel Data;
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//
//                    JSONObject obj = jsonArray.getJSONObject(0);
//                    Data = new AbsenPegawaiModel(0,
//                            obj.getInt("id_pegawai"),
//                            getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
//                            "",
//                            "",
//                            obj.getInt("telat_satu"),
//                            obj.getInt("telat_dua"),
//                            obj.getInt("dokter"),
//                            obj.getInt("izin_stgh_hari"),
//                            obj.getInt("izin_cuti"),
//                            obj.getInt("izin_non_cuti"),
//                            obj.getInt("masuk")
//                    );
//
//                    txtTelat1.setText(Integer.toString(Data.getTelat_satu()));
//                    txtTelat2.setText(Integer.toString(Data.getTelat_dua()));
//                    txtDokter.setText(Integer.toString(Data.getDokter()));
//                    txtNonCuti.setText(Integer.toString(Data.getIzin_non_cuti()));
//                    txtstghHari.setText(Integer.toString(Data.getIzin_stgh_hari()));
//                    txtCuti.setText(Integer.toString(Data.getIzin_cuti()));
//
//                    if (Data.getTelat_satu() > 0){
//                        SetPotonganKhusus(ID_PT_TELAT_15);
//                    }
//
//                    if (Data.getTelat_dua() > 0){
//                        SetPotonganKhusus(ID_PT_TELAT_LBH_15);
//                    }
//
//                    if (Data.getDokter() > 0){
//                        SetPotonganKhusus(ID_PT_DOKTER);
//                    }
//
//                    if (Data.getIzin_stgh_hari() > 0){
//                        SetPotonganKhusus(ID_PT_IZIN_STGH_HARI);
//                    }
//
//                    if (Data.getIzin_non_cuti() > 0){
//                        SetPotonganKhusus(ID_PT_IZIN_NON_CUTI);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PenggajianInputNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV_TUNJANGAN) {
                HitungDetail();
                HitungTotal();
            }else if (requestCode == RESULT_LOV_POTONGAN) {
                HitungDetail();
                HitungTotal();
            }else if (requestCode == RESULT_LOV_KASBON) {
                HitungDetail();
                HitungTotal();
            }else if (requestCode == RESULT_LOV_PEGAWAI) {
                Loading.setMessage("Loading...");
                Loading.setCancelable(false);
                Loading.show();

                Bundle extra = data.getExtras();
                IdPegawai = extra.getInt("id");
                txtPegawai.setText(Get_Nama_Master_Pegawai(IdPegawai));
                DataPegawai = new PegawaiModel(OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(IdPegawai)));
                lblGajiPokok.setText(fmt.format(DataPegawai.getGaji_pokok()));

                LoadTunjanganPegawai();
                LoadKasbonPegawai();
//                LoadAbsensi();
                HitungDetail();
                HitungTotal();

                if (!Mode.equals(DETAIL_MODE)){
                    if (ArListKasbon.size() > 0){
                        lblCaptionTotalKasbon.setTextColor(getResources().getColor(R.color.colorSoftRed));
                    }else {
                        lblCaptionTotalKasbon.setTextColor(getResources().getColor(R.color.colorGrayDark));
                    }
                };
            }
        }
        Loading.dismiss();
    }

    private void LostFocus(){
        txtTmp.setVisibility(View.VISIBLE);
        txtTmp.requestFocus();
        txtTmp.setVisibility(View.INVISIBLE);
    }
}


//    protected void LoadData(){
//        Loading.setMessage("Loading...");
//        Loading.setCancelable(false);
//        Loading.show();
//        String filter;
//        filter = "?id="+IdMst;
//        String url = route.URL_GET_PENGGAJIAN  + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    JSONObject obj = jsonArray.getJSONObject(0);
//                    IdPegawai = obj.getInt("id_pegawai");
//                    //txtPeriode.setText(getTglFormatCustom(serverNowStartOfTheMonthLong(), "MMMM yyyy"));
//                    txtNomor.setText(obj.getString("nomor"));
//                    txtTanggal.setText(FormatDateFromSql(obj.getString("tanggal")));
//                    txtPegawai.setText(Get_Nama_Master_Pegawai(obj.getInt("id_pegawai")));
//                    txtTelat1.setText(obj.getString("telat_satu"));
//                    txtTelat2.setText(obj.getString("telat_dua"));
//                    txtDokter.setText(obj.getString("dokter"));
//                    txtstghHari.setText(obj.getString("izin_stgh_hari"));
//                    txtCuti.setText(obj.getString("izin_cuti"));
//                    txtNonCuti.setText(obj.getString("izin_non_cuti"));
//                    txtKeterangan.setText(obj.getString("keterangan"));
//                    txtLembur.setText(obj.getString("jam_lembur"));
//                    lblGajiPokok.setText(fmt.format(obj.getDouble("gaji_pokok")));
//                    lblTotTunjangan.setText(fmt.format(obj.getDouble("total_tunjangan")));
//                    lblTotPotongan.setText(fmt.format(obj.getDouble("total_potongan")));
//                    lblTotLembur.setText(fmt.format(obj.getDouble("total_lembur")));
//                    lblTotKasbon.setText(fmt.format(obj.getDouble("total_kasbon")));
//                    lblTotal.setText(fmt.format(obj.getDouble("total")));
//
//                    LoadKasbonPegawaiEdit(IdPegawai);
//                    DataPegawai = new PegawaiModel(OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(IdPegawai)));
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
//    }