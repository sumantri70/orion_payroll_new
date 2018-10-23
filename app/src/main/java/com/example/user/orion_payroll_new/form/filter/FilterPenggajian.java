package com.example.user.orion_payroll_new.form.filter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.lov.lov_pegawai;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getSimpleDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.hideSoftKeyboard;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class FilterPenggajian extends AppCompatActivity {

    private TextInputEditText txtTglDari, txtTglSampai, txtPegawai;
    private Button btnSimpan, btnReset;

    private long tgl_dari, tgl_Sampai;
    private int IdPegawai, IdPegawaiTmp;

    protected void CreateView(){
        txtTglDari   = (TextInputEditText) findViewById(R.id.txtTglDari);
        txtTglSampai = (TextInputEditText) findViewById(R.id.txtTglSampai);
        txtPegawai   = (TextInputEditText) findViewById(R.id.txtPegawai);
        btnSimpan    = (Button) findViewById(R.id.btnSet);
        btnReset     = (Button) findViewById(R.id.btnReset);
    }

    protected void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.setTitle("Filter");

        Bundle extra = this.getIntent().getExtras();
        this.tgl_dari   = extra.getLong("TGL_DARI");
        this.tgl_Sampai = extra.getLong("TGL_SAMPAI");
        this.IdPegawai  = extra.getInt("PEGAWAI_ID");
        IdPegawaiTmp    = IdPegawai;

        this.txtTglDari.setText(FungsiGeneral.getTglFormat(tgl_dari));
        this.txtTglSampai.setText(FungsiGeneral.getTglFormat(tgl_Sampai));

        if(IdPegawai != 0){
            this.txtPegawai.setText(Get_Nama_Master_Pegawai(IdPegawai));
        }


    }

    protected void EventClass(){
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                tgl_Sampai = getSimpleDate(txtTglSampai.getText().toString());
                tgl_dari   = getSimpleDate(txtTglDari.getText().toString());
                IdPegawai  = IdPegawaiTmp;

                Intent intent = getIntent();
                intent.putExtra("PEGAWAI_ID", IdPegawai);
                intent.putExtra("TGL_DARI", tgl_dari);
                intent.putExtra("TGL_SAMPAI", tgl_Sampai);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgl_dari      = serverNowStartOfTheMonthLong();
                tgl_Sampai    = serverNowLong();
                IdPegawai     = 0;
                IdPegawaiTmp  = 0;

                txtPegawai.setText("");
                txtTglDari.setText(FungsiGeneral.getTglFormat(tgl_dari));
                txtTglSampai.setText(FungsiGeneral.getTglFormat(tgl_Sampai));

                Intent intent = getIntent();
                intent.putExtra("PEGAWAI_ID", IdPegawai);
                intent.putExtra("TGL_DARI", tgl_dari);
                intent.putExtra("TGL_SAMPAI", tgl_Sampai);
                setResult(RESULT_OK, intent);
            }
        });
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
