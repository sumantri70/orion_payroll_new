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
import com.example.user.orion_payroll_new.form.adapter.PilihPotonganPenggajianAdapter;
import com.example.user.orion_payroll_new.form.lov.lov_potongan;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Kode_Master_Potongan;

public class PilihPotonganPenggajian extends AppCompatActivity {
    public EditText txtTmp;
    public Button btnTambah;
    private ListView ListRekap;
    public PilihPotonganPenggajianAdapter Adapter;
    public List<PenggajianDetailModel> ListPotongan;
    public String Mode;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.btnTambah  = (Button) findViewById(R.id.btnTambah);
        this.txtTmp          = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA
    }

    private void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        ListPotongan = new ArrayList<PenggajianDetailModel>();
        this.ListRekap.setDividerHeight(1);
    }

    protected void EventClass(){
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTambah.requestFocus();
                LostFocus();
                Intent s = new Intent(PilihPotonganPenggajian.this, lov_potongan.class);
                startActivityForResult(s, RESULT_LOV);
            }
        });
    }

    public void LoadData() {
        Adapter = new PilihPotonganPenggajianAdapter(PilihPotonganPenggajian.this, R.layout.list_pilih_potongan_penggajian, ListPotongan);
        Adapter.notifyDataSetChanged();
        ListRekap.setAdapter(Adapter);
    }

    protected boolean IsValid(){
        for(int i=0; i < ListPotongan.size(); i++){
            if ((ListPotongan.get(i).getJumlah() == 0)) {
                Toast.makeText(PilihPotonganPenggajian.this, "Jumlah "+Get_Kode_Master_Potongan(ListPotongan.get(i).getId_tjg_pot_kas()) +" belum diisi", Toast.LENGTH_SHORT).show();
                return false;
            }

//            for(int j=i+1; j < ArListPotongan.size(); j++){
//                if (ArListPotongan.get(i).getId() == ArListPotongan.get(j).getId()) {
//                    Toast.makeText(PegawaiInput.this, ArListPotongan.get(i).getNama()+" tidak boleh diinput > 1 kali", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_potongan_penggajian);
        CreateVew();
        InitClass();
        EventClass();
        ListPotongan = PenggajianInputNew.getInstance().ArListPotongan;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV) {
                Bundle extra = data.getExtras();
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(OrionPayrollApplication.getInstance().ListHashPotonganGlobal.get(Integer.toString(extra.getInt("id"))).getId());
                Data.setJumlah(0.0);
                ListPotongan.add(Data);
                Adapter.notifyDataSetChanged();
            }else{

            }
        }
    }

    protected void LostFocus(){
        txtTmp.setVisibility(View.VISIBLE);
        txtTmp.requestFocus();
        txtTmp.setVisibility(View.INVISIBLE);
    }
}
