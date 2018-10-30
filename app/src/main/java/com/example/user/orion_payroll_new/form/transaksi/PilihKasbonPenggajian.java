package com.example.user.orion_payroll_new.form.transaksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.adapter.PilihKasbonPenggajianAdapter;
import com.example.user.orion_payroll_new.form.lov.lov_potongan;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;


public class PilihKasbonPenggajian extends AppCompatActivity {
    public EditText txtTmp;
    private ListView ListRekap;
    public PilihKasbonPenggajianAdapter Adapter;
    public List<PenggajianDetailModel> ListKasbon;
    public String Mode;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.txtTmp     = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA
    }

    private void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        ListKasbon = new ArrayList<PenggajianDetailModel>();
        this.ListRekap.setDividerHeight(1);
    }

    protected void EventClass(){

    }


    public void LoadData() {
        Adapter = new PilihKasbonPenggajianAdapter(PilihKasbonPenggajian.this, R.layout.list_pilih_kasbon_penggajian, ListKasbon);
        Adapter.notifyDataSetChanged();
        ListRekap.setAdapter(Adapter);
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
        EventClass();
        ListKasbon = PenggajianInputNew.getInstance().ArListKasbon;
        this.Mode = "";
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
        };
        return false;

    }

    protected void LostFocus(){
        txtTmp.setVisibility(View.VISIBLE);
        txtTmp.requestFocus();
        txtTmp.setVisibility(View.INVISIBLE);
    }
}
