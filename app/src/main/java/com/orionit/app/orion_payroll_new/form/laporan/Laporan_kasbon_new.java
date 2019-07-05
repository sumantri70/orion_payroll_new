package com.orionit.app.orion_payroll_new.form.laporan;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.PegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.SaldoTable;
import com.orionit.app.orion_payroll_new.form.adapter.ExpandListAdapterLapranKasbon;
import com.orionit.app.orion_payroll_new.form.adapter.ExpandListAdapterPegawai;
import com.orionit.app.orion_payroll_new.form.adapter.LaporanKasbonAdapter;
import com.orionit.app.orion_payroll_new.form.filter.FilterLaporanPenggajian;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModel;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModelChild;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModelHeader;
import com.orionit.app.orion_payroll_new.models.SaldoKasbonModel;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;
import com.orionit.app.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatMySql;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;

public class Laporan_kasbon_new extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private Dialog DialogFilter;
    public ExpandableListView ListView;

    private Button btnFilter;
    private SwipeRefreshLayout swipe;
    public static PegawaiTable Data;

    private Long tgl_dari, tgl_Sampai;
    private int IdPegawai, status;

    private PegawaiTable DbPegawai;
    private SaldoTable DbSaldo;

    
    public ExpandListAdapterLapranKasbon ListAdapter;
    private List<LaporanKasbonModelHeader> ListDataHeader;
    public List<LaporanKasbonModelChild> ArListChild;

    private HashMap<LaporanKasbonModelHeader, List<LaporanKasbonModelChild>> ListHash;


    private void CreateVew(){
        this.ListView   = (ExpandableListView)findViewById(R.id.ExpLv);
        this.btnFilter  = (Button) findViewById(R.id.BtnFilter);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);
        ListView        = (ExpandableListView)findViewById(R.id.ExpLv);

        this.DialogFilter = new Dialog(Laporan_kasbon_new.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
    }

    private void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Laporan Kasbon");

        DbPegawai  = new PegawaiTable(this);
        DbSaldo    = new SaldoTable(this);

        tgl_dari   = serverNowStartOfTheMonthLong();
        tgl_Sampai = serverNowLong();
        IdPegawai  = 0;
        status     = 0;

        ListDataHeader = new ArrayList<>();
        ListHash = new HashMap<>();
        ArListChild = new ArrayList<>();

        //ListHash.put(ListDataHeader.get(0), ArListChild);
//        ListAdapter = new ExpandListAdapterLapranKasbon(this, ListDataHeader, ListHash);
//        ListView.setAdapter(ListAdapter);
    }

    protected void EventClass(){

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(Laporan_kasbon_new.this, FilterLaporanPenggajian.class);
                s.putExtra("TGL_DARI", tgl_dari);
                s.putExtra("TGL_SAMPAI", tgl_Sampai);
                s.putExtra("STATUS", status);
                s.putExtra("PEGAWAI_ID", IdPegawai);
                Laporan_kasbon_new.this.startActivityForResult(s, RESULT_LOV);
            }
        });

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           LoadData();
                       }
                   }
        );
    }

    public void LoadData(){
        swipe.setRefreshing(true);

        ArrayList<Integer> ListPegawai = new ArrayList<Integer>();
        ListPegawai = DbPegawai.get_list_id_pegawai("",IdPegawai);

        ArrayList<SaldoKasbonModel> ListSaldoAwal = new ArrayList<SaldoKasbonModel>();
        ListSaldoAwal = DbSaldo.get_list_saldo_kasbon(tgl_dari,IdPegawai, true);

        ArrayList<SaldoKasbonModel> ListSaldoAkhir = new ArrayList<SaldoKasbonModel>();
        ListSaldoAkhir = DbSaldo.get_list_saldo_kasbon(tgl_Sampai, IdPegawai, false);

        ListDataHeader.clear();
        for (int i =0; i < ListPegawai.size(); i++){
            LaporanKasbonModelHeader header = new LaporanKasbonModelHeader(
                    ListPegawai.get(i),
                    CariListSaldo(ListPegawai.get(i), ListSaldoAwal).getSaldo(),
                    CariListSaldo(ListPegawai.get(i), ListSaldoAkhir).getSaldo()
            );
            ArrayList<LaporanKasbonModelChild> ListChild = new ArrayList<LaporanKasbonModelChild>();
            ListChild = DbSaldo.get_laporan_kasbon(tgl_dari, tgl_Sampai, ListPegawai.get(i));

            if ((ListChild.size() > 0) || (header.getSaldo_awal() > 0) || (header.getSaldo_akhir() > 0)){
                ListDataHeader.add(header);
                ListHash.put(header, ListChild);
            }
        }

        ListAdapter = new ExpandListAdapterLapranKasbon(this, ListDataHeader, ListHash);
        ListView.setAdapter(ListAdapter);
        swipe.setRefreshing(false);
    }

    private SaldoKasbonModel CariListSaldo(int id_pegawai, ArrayList<SaldoKasbonModel> Data){
        SaldoKasbonModel hasil = new SaldoKasbonModel();
        for (int i=0; i < Data.size(); i++){
            if (Data.get(i).getId_pegawai() == id_pegawai){
                hasil = Data.get(i);
                return hasil;
            }
        }
        return hasil;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kasbon_new);
        CreateVew();
        InitClass();
        EventClass();
        LoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ListView.setIndicatorBoundsRelative(ListView.getRight()- 100, ListView.getWidth());
    }

    @Override
    public void onRefresh() {
        LoadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOV) {
                Bundle extra = data.getExtras();
                IdPegawai  = extra.getInt("PEGAWAI_ID");
                tgl_dari   = extra.getLong("TGL_DARI");
                tgl_Sampai = extra.getLong("TGL_SAMPAI");
                status     = extra.getInt("STATUS");
            }
            LoadData();
        }
    }
}

