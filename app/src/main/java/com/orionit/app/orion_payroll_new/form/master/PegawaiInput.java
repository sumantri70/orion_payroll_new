package com.orionit.app.orion_payroll_new.form.master;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.DetailTunjanganPegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.PegawaiTable;
import com.orionit.app.orion_payroll_new.form.adapter.ExpandListAdapterPegawai;
import com.orionit.app.orion_payroll_new.models.DetailTunjanganPegawaiModel;
import com.orionit.app.orion_payroll_new.models.PegawaiModel;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;
import com.orionit.app.orion_payroll_new.utility.FormatNumber;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.view.View.inflate;
import static com.orionit.app.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_NEGATIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_POSITIVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SAVE_CONFIRMATION;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_SAVE;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.Round;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Kode_Master_Tunjangan;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Tunjangan;

public class PegawaiInput extends AppCompatActivity {
    private TextInputEditText txtNik, txtNama, txtAlamat, txtTelpon1, txtTelpon2, txtEmail, txtTglLahir, txtTglMulaiBekerja, txtKeterangan, txtTelponDarurat;
    private Button btnSimpan;
    private ImageButton btnUpdateGaji;

    private TextInputEditText txtGTanggal, txtGGajiPokok, txtGUangIkatan, txtGUangKehadiran, txtGPremiHarian, txtGPremiPerjam, txtGKeterangan;
    private Button btnGSimpan, btnGBatal;

    private TextView lblGUangIkatan, lblGUangKehadiran, lblGPremiHarian, lblGPremiPerjam, lblGGajiPokok;

    public String Mode;
    private int IdMst;
    private ProgressDialog Loading;
    public EditText txtTmp;

    private Dialog DialogUpdateGaji;

    public ExpandableListView ListView;
    public ExpandListAdapterPegawai ListAdapter;
    private List<String> ListDataHeader;
    private HashMap<String, List<TunjanganModel>> ListHash;
    public List<TunjanganModel> ArListTunjangan;
    private Double GajiTerakhir;

    private PegawaiTable TData;
    private DetailTunjanganPegawaiTable TDetTunjangan;
    private int totalHeightViewGroup = 0;
    private int totalHeightViewChild = 0;

    protected void CreateView(){
        txtNik             = (TextInputEditText) findViewById(R.id.txtNik);
        txtNama            = (TextInputEditText) findViewById(R.id.txtNomor);
        txtAlamat          = (TextInputEditText) findViewById(R.id.txtAlamat);
        txtTelpon1         = (TextInputEditText) findViewById(R.id.txtTelpon1);
        txtTelpon2         = (TextInputEditText) findViewById(R.id.txtTelpon2);
        txtEmail           = (TextInputEditText) findViewById(R.id.txtEmail);
        txtTglLahir        = (TextInputEditText) findViewById(R.id.txtTglLahir);
        txtTelponDarurat   = (TextInputEditText) findViewById(R.id.txtTelponDarurat);
        txtKeterangan      = (TextInputEditText) findViewById(R.id.txtKeterangan);
        txtTglMulaiBekerja = (TextInputEditText) findViewById(R.id.txtTglMulaiBekerja);
        btnSimpan          = (Button) findViewById(R.id.btnSimpan);
        ListView           = (ExpandableListView)findViewById(R.id.ExpLv);
        txtTmp             = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA
        btnUpdateGaji      = (ImageButton) findViewById(R.id.btnUpdateGaji);


        this.DialogUpdateGaji = new Dialog(PegawaiInput.this);
        this.DialogUpdateGaji.setContentView(R.layout.activity_pegawai_input_gaji);
        //this.DialogUpdateGaji.setContentView(R.layout.filter_aktivasi);

        this.txtGTanggal       = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtTanggal);
        this.txtGGajiPokok     = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtGajiPokok);
        this.txtGUangIkatan    = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtUangIkatan);
        this.txtGUangKehadiran = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtUangKehadiran);
        this.txtGPremiHarian   = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtPremiHarian);
        this.txtGPremiPerjam   = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtPremiPerjam);
        this.txtGKeterangan    = (TextInputEditText) DialogUpdateGaji.findViewById(R.id.txtKeterangan);
        this.btnGSimpan        = (Button) DialogUpdateGaji.findViewById(R.id.btnSimpan);
        this.btnGBatal         = (Button) DialogUpdateGaji.findViewById(R.id.btnBatal);

        this.lblGGajiPokok      = (TextView) findViewById(R.id.lblGajiPokok);
        this.lblGUangIkatan     = (TextView) findViewById(R.id.lblUangIkatan);
        this.lblGUangKehadiran  = (TextView) findViewById(R.id.lblUangKehadiran);
        this.lblGPremiHarian    = (TextView) findViewById(R.id.lblPremiHarian);
        this.lblGPremiPerjam    = (TextView) findViewById(R.id.lblPremiPerjam);
    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");
        this.IdMst = extra.getInt("ID");
        Loading = new ProgressDialog(PegawaiInput.this);

        TData = new PegawaiTable(PegawaiInput.this);
        TDetTunjangan = new DetailTunjanganPegawaiTable(PegawaiInput.this);

        if (Mode.equals(EDIT_MODE)){
            this.setTitle("Edit Pegawai");
        }else if (Mode.equals(DETAIL_MODE)){
            this.setTitle("Detail Pegawai");
            this.btnSimpan.setVisibility(View.INVISIBLE);
        }else{
            this.setTitle("Input Pegawai");
        }

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
        this.btnSimpan.setEnabled(Enabled);
        this.txtTglLahir.setEnabled(Enabled);
        this.txtTglMulaiBekerja.setEnabled(Enabled);
        this.txtKeterangan.setEnabled(Enabled);
        this.btnUpdateGaji.setEnabled(Enabled);
        this.txtAlamat.setEnabled(Enabled);
        this.txtTelponDarurat.setEnabled(Enabled);

        txtTmp.setVisibility(View.INVISIBLE);
        txtNik.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); //untuk uppercase
        txtGGajiPokok.addTextChangedListener(new FormatNumber(txtGGajiPokok));
        txtGUangIkatan.addTextChangedListener(new FormatNumber(txtGUangIkatan));
        txtGUangKehadiran.addTextChangedListener(new FormatNumber(txtGUangKehadiran));
        txtGPremiHarian.addTextChangedListener(new FormatNumber(txtGPremiHarian));
        txtGPremiPerjam.addTextChangedListener(new FormatNumber(txtGPremiPerjam));

        this.txtGUangIkatan.setEnabled(false);
        this.txtGUangKehadiran.setEnabled(false);
        this.txtGPremiHarian.setEnabled(false);
        this.txtGPremiPerjam.setEnabled(false);

        this.lblGGajiPokok.setText("0");
        this.lblGUangIkatan.setText("0");
        this.lblGUangKehadiran.setText("0");
        this.lblGPremiHarian.setText("0");
        this.lblGPremiPerjam.setText("0");
    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTmp.setVisibility(View.VISIBLE);
                txtTmp.requestFocus();
                txtTmp.setVisibility(View.INVISIBLE);
                if (IsValid() == true){
                    AlertDialog.Builder bld = new AlertDialog.Builder(PegawaiInput.this);
                    bld.setTitle("Konfirmasi");
                    bld.setCancelable(true);
                    bld.setMessage(MSG_SAVE_CONFIRMATION);

                    bld.setPositiveButton(MSG_POSITIVE,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(Mode.equals(EDIT_MODE)){
                                if (IsSavedEdit()){
                                    Toast.makeText(PegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }else{
                                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                if (IsSaved()){
                                    Toast.makeText(PegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }else{
                                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
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


        txtTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        btnGSimpan.setOnClickListener(new View.OnClickListener() {

            public void SetDetailGaji (){
                lblGGajiPokok.setText(txtGGajiPokok.getText().toString());
                lblGUangIkatan.setText(txtGUangIkatan.getText().toString());
                lblGUangKehadiran.setText(txtGUangKehadiran.getText().toString());
                lblGPremiHarian.setText(txtGPremiHarian.getText().toString());
                lblGPremiPerjam.setText(txtGPremiPerjam.getText().toString());
            }

            @Override
            public void onClick(View view) {
                if (IsValiGaji() == true){
                    txtGKeterangan.requestFocus();
                    SetDetailGaji();
                    DialogUpdateGaji.dismiss();
                }
            }
        });

        btnGBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUpdateGaji.dismiss();
            }
        });


        btnUpdateGaji.setOnClickListener(new View.OnClickListener() {
            protected void SetDialogGaji(){
                txtGTanggal.setText(FungsiGeneral.serverNowFormated());
                txtGGajiPokok.requestFocus();
                txtGGajiPokok.setText(lblGGajiPokok.getText());
                txtGUangIkatan.setText(lblGUangIkatan.getText());
                txtGUangKehadiran.setText(lblGUangKehadiran.getText());
                txtGPremiHarian.setText(lblGPremiHarian.getText());
                txtGPremiPerjam.setText(lblGPremiPerjam.getText());
            }

            @Override
            public void onClick(View v) {
                SetDialogGaji();
                txtGGajiPokok.requestFocus();
                txtGGajiPokok.selectAll();
                DialogUpdateGaji.show();
            }
        });

        txtGGajiPokok.setOnFocusChangeListener( new View.OnFocusChangeListener(){
            protected void HitungDetailGaji(){
                Double GajiPokok, UangIkatan, UangKehadiran, PremiHarian, PremiPerjam;
                GajiPokok     = StrFmtToDouble(txtGGajiPokok.getText().toString());
                UangIkatan    = Round(GajiPokok * 0.2,0);
                UangKehadiran = GajiPokok - UangIkatan;
                PremiHarian   = Round(UangKehadiran / 21,0);
                PremiPerjam   = Round(PremiHarian / 8,0);

                txtGUangIkatan.setText(fmt.format(UangIkatan));
                txtGUangKehadiran.setText(fmt.format(UangKehadiran));
                txtGPremiHarian.setText(fmt.format(PremiHarian));
                txtGPremiPerjam.setText(fmt.format(PremiPerjam));
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                HitungDetailGaji();
            }
        });


        ListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                SetAutoHeightListView();
            }
        });

        ListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                SetAutoHeightListView();
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
        PegawaiModel Data = TData.GetData(IdMst);
        txtNik.setText(Data.getNik());
        txtNama.setText(Data.getNama());
        txtAlamat.setText(Data.getAlamat());
        txtTelpon1.setText(Data.getTelpon1());
        txtTelpon2.setText(Data.getTelpon2());
        txtEmail.setText(Data.getEmail());
        txtTglLahir.setText(getTglFormat(Data.getTgl_lahir()));
        txtTglMulaiBekerja.setText(getTglFormat(Data.getTgl_mulai_kerja()));
        txtKeterangan.setText(Data.getKeterangan());
        txtTelponDarurat.setText(Data.getNo_telpon_darurat());
        lblGGajiPokok.setText(fmt.format(Data.getGaji_pokok()));
        lblGUangIkatan.setText(fmt.format(Data.getUang_ikatan()));
        lblGUangKehadiran.setText(fmt.format(Data.getUang_kehadiran()));
        lblGPremiHarian.setText(fmt.format(Data.getPremi_harian()));
        lblGPremiPerjam.setText(fmt.format(Data.getPremi_perjam()));
        GajiTerakhir = Data.getGaji_pokok();

        ExpandableListAdapter listAdapter = (ExpandableListAdapter) ListView.getExpandableListAdapter();
        View groupItem = listAdapter.getGroupView(0, false, null, ListView);
        groupItem.measure(0, View.MeasureSpec.UNSPECIFIED);
        totalHeightViewGroup = groupItem.getMeasuredHeight();

        LayoutInflater inflater = (LayoutInflater)this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItem = inflater.inflate(R.layout.list_item_tunjangan_pegawai,null);
        listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
        totalHeightViewChild = listItem.getMeasuredHeight();

        List<DetailTunjanganPegawaiModel> DataTunjangan = new ArrayList<DetailTunjanganPegawaiModel>();
        DataTunjangan = TDetTunjangan.GetListData(IdMst);

        TunjanganModel DataDetail;
        for (int i = 0; i < DataTunjangan.size(); i++) {
            DataDetail = new TunjanganModel(
                    DataTunjangan.get(i).getId_tunjangan(),
                    Get_Kode_Master_Tunjangan(DataTunjangan.get(i).getId_tunjangan()),
                    Get_Nama_Master_Tunjangan(DataTunjangan.get(i).getId_tunjangan()),
                    "",
                    "",
                    ""
            );
            DataDetail.setJumlah(DataTunjangan.get(i).getJumlah());
            ArListTunjangan.add(DataDetail);
        }
        ListAdapter.notifyDataSetChanged();
    }




    protected boolean IsSaved(){
        try {
            PegawaiModel Data = new PegawaiModel(0,
                    txtNik.getText().toString().trim(),
                    txtNama.getText().toString().trim(),
                    txtAlamat.getText().toString().trim(),
                    txtTelpon1.getText().toString().trim(),
                    txtTelpon2.getText().toString().trim(),
                    txtEmail.getText().toString().trim(),
                    StrFmtToDouble(lblGGajiPokok.getText().toString()),
                    TRUE_STRING,
                    getSimpleDate(txtTglLahir.getText().toString()),
                    getSimpleDate(txtTglMulaiBekerja.getText().toString()),
                    txtKeterangan.getText().toString().trim(),
                    StrFmtToDouble(lblGUangIkatan.getText().toString()),
                    StrFmtToDouble(lblGUangKehadiran.getText().toString()),
                    StrFmtToDouble(lblGPremiHarian.getText().toString()),
                    StrFmtToDouble(lblGPremiPerjam.getText().toString()),
                    txtTelponDarurat.getText().toString().trim());
            Long IdPegawai = TData.Insert(Data);

            for(int i=0; i < ArListTunjangan.size() ;i++) {
                DetailTunjanganPegawaiModel Detail = new DetailTunjanganPegawaiModel(
                    0, IdPegawai.intValue(),
                    ArListTunjangan.get(i).getId(),
                    ArListTunjangan.get(i).getJumlah()
                );
                TDetTunjangan.Insert(Detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean IsSavedEdit(){
        try {
            PegawaiModel Data = new PegawaiModel(IdMst,
                    txtNik.getText().toString().trim(),
                    txtNama.getText().toString().trim(),
                    txtAlamat.getText().toString().trim(),
                    txtTelpon1.getText().toString().trim(),
                    txtTelpon2.getText().toString().trim(),
                    txtEmail.getText().toString().trim(),
                    StrFmtToDouble(lblGGajiPokok.getText().toString()),
                    TRUE_STRING,
                    getSimpleDate(txtTglLahir.getText().toString()),
                    getSimpleDate(txtTglMulaiBekerja.getText().toString()),
                    txtKeterangan.getText().toString().trim(),
                    StrFmtToDouble(lblGUangIkatan.getText().toString()),
                    StrFmtToDouble(lblGUangKehadiran.getText().toString()),
                    StrFmtToDouble(lblGPremiHarian.getText().toString()),
                    StrFmtToDouble(lblGPremiPerjam.getText().toString()),
                    txtTelponDarurat.getText().toString().trim());
            TData.Update(Data);

            TDetTunjangan.delete(IdMst);

            for(int i=0; i < ArListTunjangan.size() ;i++) {
                DetailTunjanganPegawaiModel Detail = new DetailTunjanganPegawaiModel(
                        0, IdMst,
                        ArListTunjangan.get(i).getId(),
                        ArListTunjangan.get(i).getJumlah()
                );
                TDetTunjangan.Insert(Detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    protected boolean IsValid(){
        if (this.txtNik.getText().toString().trim().equals("")) {
            txtNik.requestFocus();
            txtNik.setError("NIK belum diisi");
            return false;
        }

        if (TData.KodeExist(this.txtNik.getText().toString().trim(),IdMst)) {
            txtNik.requestFocus();
            txtNik.setError("NIK sudah pernah digunakan");
            return false;
        }

        if (this.txtNama.getText().toString().equals("")) {
            txtNama.requestFocus();
            txtNama.setError("Nama belum diisi");
            return false;
        }

        if (this.txtAlamat.getText().toString().equals("")) {
            txtAlamat.requestFocus();
            txtAlamat.setError("Alamat belum diisi");
            return false;
        }

        if (this.txtEmail.getText().toString().equals("")) {
            txtEmail.requestFocus();
            txtEmail.setError("Email belum diisi");
            return false;
        }

        if (this.txtEmail.getText().toString().equals("")) {
            txtEmail.requestFocus();
            txtEmail.setError("Email belum diisi");
            return false;
        }

        if (this.txtEmail.getText().toString().equals("")) {
            txtEmail.requestFocus();
            txtEmail.setError("Email belum diisi");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()) {
            txtEmail.requestFocus();
            txtEmail.setError("Email salah");
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

        if ( getSimpleDate(txtTglLahir.getText().toString()) > serverNowLong() ) {
            txtTglLahir.setFocusableInTouchMode(true);
            txtTglLahir.requestFocus();
            txtTglLahir.setError("Tgl. Lahir tidak boleh lebih dari hari ini");
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

        if (this.txtTelpon1.getText().toString().equals("")) {
            txtTelpon1.requestFocus();
            txtTelpon1.setError("Telpon 1 belum diisi");
            return false;
        }

        if (StrFmtToDouble(this.lblGGajiPokok.getText().toString()) == 0) {
            Toast.makeText(PegawaiInput.this, "Gaji pokok belum diisi", Toast.LENGTH_SHORT).show();
            btnUpdateGaji.requestFocus();
            return false;
        }

        if (ArListTunjangan.size() == 0) {
            Toast.makeText(PegawaiInput.this, "Belum ada tunjangan yang diinput", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i=0; i < ArListTunjangan.size(); i++){
            if (ArListTunjangan.get(i).getJumlah() == 0) {
                Toast.makeText(PegawaiInput.this, "Jumlah "+ArListTunjangan.get(i).getNama()+ " belum diisi", Toast.LENGTH_SHORT).show();
                return false;
            }

            for(int j=i+1; j < ArListTunjangan.size(); j++){
                if (ArListTunjangan.get(i).getId() == ArListTunjangan.get(j).getId()) {
                    Toast.makeText(PegawaiInput.this, ArListTunjangan.get(i).getNama()+" tidak boleh diinput > 1 kali", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean IsValiGaji(){
        if (StrFmtToDouble(txtGGajiPokok.getText().toString()) == 0) {
            txtGGajiPokok.requestFocus();
            txtGGajiPokok.setError("Gaji Pokok belum diisi");
            return false;
        }
        return true;
    }


    private void setExpandableListViewHeight(ExpandableListView listView,int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            if (totalHeightViewGroup > 0){
                totalHeight += totalHeightViewGroup;
            }else{
                totalHeight += groupItem.getMeasuredHeight();
            }

            if (listView.isGroupExpanded(i)) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    if (totalHeightViewChild > 0){
                        totalHeight += totalHeightViewChild;
                    }else{
                        totalHeight += listItem.getMeasuredHeight();
                    }
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
        ListView.getLayoutParams().height = height;

    }

    public void SetAutoHeightListView(){
        setExpandableListViewHeight(ListView,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV) {
                Bundle extra = data.getExtras();
                TunjanganModel ModelTmp = OrionPayrollApplication.getInstance().ListHashTunjanganGlobal.get(Integer.toString(extra.getInt("id")));

                TunjanganModel Tunjangan = new TunjanganModel(
                    ModelTmp.getId(),
                    ModelTmp.getKode(),
                    ModelTmp.getNama(),
                    ModelTmp.getKeterangan(),
                    ModelTmp.getStatus(),
                    ModelTmp.getCan_delete()
                );

                ArListTunjangan.add(Tunjangan);
                ListAdapter.notifyDataSetChanged();

                if (ArListTunjangan.size() > 0 ){
                    ListView.expandGroup(0);
                }

                if (ArListTunjangan.size() > 0){
                    ListView.smoothScrollToPosition(ArListTunjangan.size());
                }
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


//    protected void LoadDetail(){
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
//                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
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

//    protected void IsSaved(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PEGAWAI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(PegawaiInput.this, MSG_SUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_SAVE, Toast.LENGTH_SHORT).show();
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
//                        obj.put("id_tunjangan", String.valueOf(ArListTunjangan.get(i).getId()));
//                        obj.put("jumlah", String.valueOf(ArListTunjangan.get(i).getJumlah()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ArParms.put(obj);
//                }
//                Log.d("jsonnnn coyyyy",ArParms.toString());
//                params.put("data", ArParms.toString());
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//    }



//    protected void LoadData(){
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
//                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
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


//    protected void IsSavedEdit(){
//        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_PEGAWAI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    Toast.makeText(PegawaiInput.this, MSG_SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PegawaiInput.this, MSG_UNSUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                JSONArray ArParms = new JSONArray();
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
//                params.put("data", ArParms.toString());
//                return params;
//            }
//        };
//        OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
//    }

