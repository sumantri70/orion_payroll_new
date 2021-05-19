package com.orionit.app.orion_payroll_new.form.transaksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;

import java.util.ArrayList;
import java.util.List;


public class PilihKasbonPenggajian extends AppCompatActivity {
    public EditText txtTmp;
    public RecyclerView RcvRekap;
    public List<PenggajianDetailModel> ListKasbon;
    public String Mode;

    public PilihKasbonPenggajianAdapterNew mAdapter;

    private void CreateVew(){
        this.txtTmp     = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA
        this.RcvRekap = (RecyclerView) findViewById(R.id.RcvRekap);
    }

    private void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        Bundle extra = this.getIntent().getExtras();
        this.Mode = extra.getString("MODE");

        ListKasbon = new ArrayList<PenggajianDetailModel>();
        RcvRekap.addItemDecoration(new DividerItemDecoration(RcvRekap.getContext(), DividerItemDecoration.VERTICAL));
    }


    public void LoadData() {
        mAdapter = new PilihKasbonPenggajianAdapterNew(PilihKasbonPenggajian.this, ListKasbon, Mode);
        RcvRekap.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RcvRekap.setAdapter(mAdapter);
    }

    protected boolean IsValid(){
//        for(int i=0; i < ListKasbon.size(); i++){
//            if ((ListKasbon.get(i).getJumlah() == 0)) {
//                Toast.makeText(PilihKasbonPenggajian.this, "Jumlah "+Get_Kode_Master_Potongan(ListKasbon.get(i).getId_tjg_pot_kas()) +" belum diisi", Toast.LENGTH_SHORT).show();
//                return false;
//            }

//            for(int j=i+1; j < ArListKasbon.size(); j++){
//                if (ArListKasbon.get(i).getId() == ArListKasbon.get(j).getId()) {
//                    Toast.makeText(PegawaiInput.this, ArListKasbon.get(i).getNama()+" tidak boleh diinput > 1 kali", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
        //}
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kasbon_penggajian);
        CreateVew();
        InitClass();
        ListKasbon = PenggajianInputNew.getInstance().ArListKasbon;
        LoadData();
    }

    @Override
    public boolean onSupportNavigateUp(){
        LostFocus();
        if (IsValid()){
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        LostFocus();
        if (IsValid()){
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    protected void LostFocus(){
        txtTmp.setVisibility(View.VISIBLE);
        txtTmp.requestFocus();
        txtTmp.setVisibility(View.INVISIBLE);
    }
}
