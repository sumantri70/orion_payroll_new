package com.orionit.app.orion_payroll_new.form.filter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.form.lov.lov_pegawai;
import com.orionit.app.orion_payroll_new.form.transaksi.PenggajianInputNew;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.EndOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getMillisDateFmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatCustom;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNow;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowFormated;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class FilterPenggajian extends AppCompatActivity {

    private TextInputEditText txtTglDari, txtTglSampai, txtPegawai, txtPeriodeDari, txtPeriodeSampai;
    private Button btnSimpan, btnReset;
    private CheckBox chbPeriode, chbTanggal;

    private long tgl_dari, tgl_Sampai, periode_dari, periode_sampai;
    private int IdPegawai, IdPegawaiTmp;

    protected void CreateView(){
        txtPeriodeDari = (TextInputEditText) findViewById(R.id.txtPeriodeDari);
        txtPeriodeSampai = (TextInputEditText) findViewById(R.id.txtPeriodeSampai);
        txtTglDari   = (TextInputEditText) findViewById(R.id.txtTglDari);
        txtTglSampai = (TextInputEditText) findViewById(R.id.txtTglSampai);
        txtPegawai   = (TextInputEditText) findViewById(R.id.txtPegawai);
        btnSimpan    = (Button) findViewById(R.id.btnSet);
        btnReset     = (Button) findViewById(R.id.btnReset);
        chbPeriode   = (CheckBox) findViewById(R.id.chbPeriode);
        chbTanggal   = (CheckBox) findViewById(R.id.chbTanggal);
    }

    protected void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.setTitle("Filter");

        Bundle extra = this.getIntent().getExtras();
        this.periode_dari   = extra.getLong("PERIODE_DARI");
        this.periode_sampai = extra.getLong("PERIODE_SAMPAI");
        this.tgl_dari       = extra.getLong("TGL_DARI");
        this.tgl_Sampai     = extra.getLong("TGL_SAMPAI");
        this.IdPegawai      = extra.getInt("PEGAWAI_ID");
        IdPegawaiTmp        = IdPegawai;

        chbPeriode.setChecked(periode_dari > 0);
        chbTanggal.setChecked(tgl_dari > 0);

        if (periode_dari <= 0){
            txtPeriodeDari.setText(getTglFormatCustom(serverNowStartOfTheMonthLong(), "MMMM yyyy"));
        }else{
            txtPeriodeDari.setText(getTglFormatCustom(periode_dari, "MMMM yyyy"));
        }

        if (periode_sampai <= 0){
            txtPeriodeSampai.setText(getTglFormatCustom(EndOfTheMonthLong(serverNowStartOfTheMonthLong()), "MMMM yyyy"));
        }else{
            txtPeriodeSampai.setText(getTglFormatCustom(periode_sampai, "MMMM yyyy"));
        }

        if (tgl_dari <= 0){
            txtTglDari.setText(getTglFormat(serverNowStartOfTheMonthLong()));
        }else{
            txtTglDari.setText(getTglFormat(tgl_dari));
        }

        if (tgl_Sampai <= 0){
            txtTglSampai.setText(getTglFormat(serverNowLong()));
        }else{
            txtTglSampai.setText(getTglFormat(tgl_Sampai));
        }

        if(IdPegawai != 0){
            this.txtPegawai.setText(Get_Nama_Master_Pegawai(IdPegawai));
        }

        SetEnabled();
    }

    protected void EventClass(){
        txtPeriodeDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(FilterPenggajian.this);
                if (txtPeriodeDari.getText().toString().equals("")){
                    txtPeriodeDari.setText(serverNowFormated());
                }
                Long tgl = getMillisDateFmt(txtPeriodeDari.getText().toString(), "MMMM yyyy");
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterPenggajian.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("MMMM yyyy");
                                txtPeriodeDari.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txtPeriodeSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(FilterPenggajian.this);
                if (txtPeriodeSampai.getText().toString().equals("")){
                    txtPeriodeSampai.setText(serverNowFormated());
                }
                Long tgl = getMillisDateFmt(txtPeriodeSampai.getText().toString(), "MMMM yyyy");
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterPenggajian.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("MMMM yyyy");
                                txtPeriodeSampai.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txtTglDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(FilterPenggajian.this);
                if (txtTglDari.getText().toString().equals("")){
                    txtTglDari.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTglDari.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterPenggajian.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("dd-MM-yyyy");
                                txtTglDari.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txtTglSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(FilterPenggajian.this);
                if (txtTglSampai.getText().toString().equals("")){
                    txtTglSampai.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTglSampai.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterPenggajian.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("MMyy");
                                format = new SimpleDateFormat("dd-MM-yyyy");
                                txtTglSampai.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txtPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdPegawaiTmp = 0;
                txtPegawai.setText("");
                Intent s = new Intent(FilterPenggajian.this, lov_pegawai.class);
                FilterPenggajian.this.startActivityForResult(s, RESULT_LOV);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chbPeriode.isChecked() == true){
                    periode_dari   = StartOfTheMonthLong(getMillisDateFmt(txtPeriodeDari.getText().toString(), "MMMM yyyy"));
                    periode_sampai = EndOfTheMonthLong(getMillisDateFmt(txtPeriodeSampai.getText().toString(), "MMMM yyyy"));
                }else {
                    periode_dari   = 0;
                    periode_sampai = 0;
                }

                if (chbTanggal.isChecked() == true){
                    tgl_dari   = getSimpleDate(txtTglDari.getText().toString());
                    tgl_Sampai = getSimpleDate(txtTglSampai.getText().toString());
                }else {
                    tgl_dari   = 0;
                    tgl_Sampai = 0;
                }

                IdPegawai  = IdPegawaiTmp;

                Intent intent = getIntent();
                intent.putExtra("PEGAWAI_ID", IdPegawai);
                intent.putExtra("PERIODE_DARI", periode_dari);
                intent.putExtra("PERIODE_SAMPAI", periode_sampai);
                intent.putExtra("TGL_DARI", tgl_dari);
                intent.putExtra("TGL_SAMPAI", tgl_Sampai);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periode_dari   = serverNowStartOfTheMonthLong();
                periode_sampai = EndOfTheMonthLong(serverNowLong());

                tgl_dari   = Long.valueOf(0);
                tgl_Sampai = Long.valueOf(0);

                IdPegawai      = 0;
                IdPegawaiTmp   = 0;
                txtPegawai.setText("");

                txtPeriodeDari.setText(getTglFormatCustom(periode_dari, "MMMM yyyy"));
                txtPeriodeSampai.setText(getTglFormatCustom(periode_sampai, "MMMM yyyy"));

                txtTglDari.setText(getTglFormat(serverNowStartOfTheMonthLong()));
                txtTglSampai.setText(getTglFormat(serverNowLong()));

                chbPeriode.setChecked(periode_dari > 0);
                chbTanggal.setChecked(tgl_dari > 0);
                SetEnabled();

                Intent intent = getIntent();
                intent.putExtra("PEGAWAI_ID", IdPegawai);
                intent.putExtra("PERIODE_DARI", periode_dari);
                intent.putExtra("PERIODE_SAMPAI", periode_sampai);
                intent.putExtra("TGL_DARI", tgl_dari);
                intent.putExtra("TGL_SAMPAI", tgl_Sampai);
                setResult(RESULT_OK, intent);
            }
        });

        chbPeriode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                periode_dari   = Long.valueOf(0);
                periode_sampai = Long.valueOf(0);
                txtPeriodeDari.setText(getTglFormatCustom(serverNowStartOfTheMonthLong(), "MMMM yyyy"));
                txtPeriodeSampai.setText(getTglFormatCustom(EndOfTheMonthLong(serverNowStartOfTheMonthLong()), "MMMM yyyy"));
                SetEnabled();
            }
        });

        chbTanggal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                tgl_dari   = 0;
                tgl_Sampai = 0;
                txtTglDari.setText(getTglFormat(serverNowStartOfTheMonthLong()));
                txtTglSampai.setText(getTglFormat(serverNowLong()));
                SetEnabled();
            }
        });
    }

    protected void SetEnabled(){
        txtPeriodeDari.setEnabled(chbPeriode.isChecked());
        txtPeriodeSampai.setEnabled(chbPeriode.isChecked());
        txtTglDari.setEnabled(chbTanggal.isChecked());
        txtTglSampai.setEnabled(chbTanggal.isChecked());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_penggajian_karyawan);
        CreateView();
        InitClass();
        EventClass();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV) {
                Bundle extra = data.getExtras();
                IdPegawaiTmp = extra.getInt("id");
                txtPegawai.setText(Get_Nama_Master_Pegawai(IdPegawaiTmp));
            }else{

            }
        }
    }
}
