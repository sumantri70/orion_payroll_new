package com.example.user.orion_payroll_new.form.filter;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Filter;
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
import static com.example.user.orion_payroll_new.utility.JEngine.*;


public class FilterKasbonPegawai extends AppCompatActivity {

    private TextInputEditText txtTglDari, txtTglSampai, txtPegawai;
    private Spinner txtStatus;
    private Button btnSimpan, btnReset;

    private long tgl_dari, tgl_Sampai;
    private int Status, IdPegawai, IdPegawaiTmp, StatusTmp;
    private List<String> ListStatus;
    private ArrayAdapter<String> AdapterStatus;

    private static String TEXT_SEMUA = "Semua";
    private static String TEXT_LUNAS = "Lunas";
    private static String TEXT_BELUM_LUNAS = "Belum Lunas";

    protected void CreateView(){
        txtTglDari   = (TextInputEditText) findViewById(R.id.txtTglDari);
        txtTglSampai = (TextInputEditText) findViewById(R.id.txtTglSampai);
        txtPegawai   = (TextInputEditText) findViewById(R.id.txtPegawai);
        txtStatus    = (Spinner) findViewById(R.id.txtStatus);
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
        this.Status     = extra.getInt("STATUS");
        this.IdPegawai  = extra.getInt("PEGAWAI_ID");
        IdPegawaiTmp    = IdPegawai;
        StatusTmp       = Status;

        this.txtTglDari.setText(FungsiGeneral.getTglFormat(tgl_dari));
        this.txtTglSampai.setText(FungsiGeneral.getTglFormat(tgl_Sampai));

        if(IdPegawai != 0){
            this.txtPegawai.setText(Get_Nama_Master_Pegawai(IdPegawai));
        }

        ListStatus = new ArrayList<>();
        ListStatus.add(TEXT_SEMUA);
        ListStatus.add(TEXT_LUNAS);
        ListStatus.add(TEXT_BELUM_LUNAS);

        AdapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListStatus);
        AdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtStatus.setAdapter(AdapterStatus);
        txtStatus.setSelection(Status);

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
                hideSoftKeyboard(FilterKasbonPegawai.this);
                if (txtTglDari.getText().toString().equals("")){
                    txtTglDari.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTglDari.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterKasbonPegawai.this,
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
                hideSoftKeyboard(FilterKasbonPegawai.this);
                if (txtTglSampai.getText().toString().equals("")){
                    txtTglSampai.setText(FungsiGeneral.serverNowFormated());
                }
                Long tgl = FungsiGeneral.getMillisDate(txtTglSampai.getText().toString());
                int mYear = (Integer.parseInt(FungsiGeneral.getTahun(tgl)));
                int mMonth = (Integer.parseInt(FungsiGeneral.getBulan(tgl)))-1;
                int mDay = (Integer.parseInt(FungsiGeneral.getHari(tgl)));

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterKasbonPegawai.this,
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

        txtStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Stat = parent.getItemAtPosition(position).toString();
                if (Stat.equals(TEXT_SEMUA)){
                    StatusTmp = 0;
                } else if (Stat.equals(TEXT_LUNAS)){
                    StatusTmp = 1;
                } else if (Stat.equals(TEXT_BELUM_LUNAS)){
                    StatusTmp = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdPegawaiTmp = 0;
                txtPegawai.setText("");
                Intent s = new Intent(FilterKasbonPegawai.this, lov_pegawai.class);
                FilterKasbonPegawai.this.startActivityForResult(s, RESULT_LOV);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgl_Sampai = getSimpleDate(txtTglSampai.getText().toString());
                tgl_dari   = getSimpleDate(txtTglDari.getText().toString());
                IdPegawai  = IdPegawaiTmp;
                Status     = StatusTmp;

                Intent intent = getIntent();
                intent.putExtra("PEGAWAI_ID", IdPegawai);
                intent.putExtra("TGL_DARI", tgl_dari);
                intent.putExtra("TGL_SAMPAI", tgl_Sampai);
                intent.putExtra("STATUS", Status);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgl_dari      = serverNowStartOfTheMonthLong();
                tgl_Sampai    = serverNowLong();
                Status        = 0;
                IdPegawai     = 0;
                IdPegawaiTmp  = 0;
                StatusTmp     = 0;

                txtPegawai.setText("");
                txtStatus.setSelection(Status);
                txtTglDari.setText(FungsiGeneral.getTglFormat(tgl_dari));
                txtTglSampai.setText(FungsiGeneral.getTglFormat(tgl_Sampai));

                Intent intent = getIntent();
                intent.putExtra("PEGAWAI_ID", IdPegawai);
                intent.putExtra("TGL_DARI", tgl_dari);
                intent.putExtra("TGL_SAMPAI", tgl_Sampai);
                intent.putExtra("STATUS", Status);
                setResult(RESULT_OK, intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_kasbon_pegawai);
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
