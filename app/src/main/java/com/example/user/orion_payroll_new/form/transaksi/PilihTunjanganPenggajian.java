package com.example.user.orion_payroll_new.form.transaksi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.adapter.PenggajianAdapterNew;
import com.example.user.orion_payroll_new.form.adapter.PilihTunjanganPenggajianAdapter;
import com.example.user.orion_payroll_new.form.lov.lov_tunjangan;
import com.example.user.orion_payroll_new.form.master.TunjanganInput;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PenggajianDetailModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.models.JCons.TIPE_DET_TUNJANGAN;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Kode_Master_Tunjangan;

public class PilihTunjanganPenggajian extends AppCompatActivity {
    public EditText txtTmp;
    public Button btnTambah;
    private ListView ListRekap;
    public PilihTunjanganPenggajianAdapter Adapter;
    public List<PenggajianDetailModel> ListTunjangan;
    public String Mode;


    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekapTunjangan);
        this.btnTambah  = (Button) findViewById(R.id.btnTambah);
        this.txtTmp     = (EditText) findViewById(R.id.txtTmp);// BUAT NUPANG FOCUSIN AJA
    }

    private void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        ListTunjangan = new ArrayList<PenggajianDetailModel>();
        this.ListRekap.setDividerHeight(1);
    }

    protected void EventClass(){
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTambah.requestFocus();
                LostFocus();
                Intent s = new Intent(PilihTunjanganPenggajian.this, lov_tunjangan.class);
                startActivityForResult(s, RESULT_LOV);
            }
        });
    }

    public void LoadData() {
        Adapter = new PilihTunjanganPenggajianAdapter(PilihTunjanganPenggajian.this, R.layout.list_pilih_tunjangan_penggajian, ListTunjangan);
        Adapter.notifyDataSetChanged();
        ListRekap.setAdapter(Adapter);
    }

    protected boolean IsValid(){
        for(int i=0; i < ListTunjangan.size(); i++){
            if ((ListTunjangan.get(i).getJumlah() == 0)) {
                Toast.makeText(PilihTunjanganPenggajian.this, "Jumlah "+Get_Kode_Master_Tunjangan(ListTunjangan.get(i).getId_tjg_pot_kas()) +" belum diisi", Toast.LENGTH_SHORT).show();
                return false;
        }

//            for(int j=i+1; j < ArListTunjangan.size(); j++){
//                if (ArListTunjangan.get(i).getId() == ArListTunjangan.get(j).getId()) {
//                    Toast.makeText(PegawaiInput.this, ArListTunjangan.get(i).getNama()+" tidak boleh diinput > 1 kali", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_tunjangan_penggajian);
        CreateVew();
        InitClass();
        EventClass();
        ListTunjangan = PenggajianInputNew.getInstance().ArListTunjangan;
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
    public void onBackPressed() {
        LostFocus();
        if (IsValid()){
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV) {
                Bundle extra = data.getExtras();
                PenggajianDetailModel Data = new PenggajianDetailModel();
                Data.setId_tjg_pot_kas(OrionPayrollApplication.getInstance().ListHashTunjanganGlobal.get(Integer.toString(extra.getInt("id"))).getId());
                Data.setJumlah(0.0);
                ListTunjangan.add(Data);
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
