package com.orionit.app.orion_payroll_new.form.transaksi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.KasbonPegawaiTable;
import com.orionit.app.orion_payroll_new.database.master.PegawaiTable;
import com.orionit.app.orion_payroll_new.form.adapter.KasbonPegawaiAdapter;
import com.orionit.app.orion_payroll_new.form.filter.FilterKasbonPegawai;
import com.orionit.app.orion_payroll_new.models.JCons;
import com.orionit.app.orion_payroll_new.models.KasbonPegawaiModel;

import java.util.ArrayList;

import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;

public class KasbonPegawaiRekap extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;
    private RadioGroup RgFilter;
    private Dialog DialogFilter;
    private RadioButton rbt1, rbt2, rbt3;

    private ImageButton btnFilter, btnSort;
    private FloatingActionButton btnTambah;

    private SwipeRefreshLayout swipe;

    private ArrayList<KasbonPegawaiModel> ListData;
    private ListView ListRekap;
    public static KasbonPegawaiAdapter Adapter;
    public static PegawaiTable Data;

    public static String Fstatus;
    public static String OrderBy;

    private Long tgl_dari, tgl_Sampai;
    private int IdPegawai, status;

    private KasbonPegawaiTable DbMaster;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnFilter  = (ImageButton) findViewById(R.id.BtnFilter);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);

        this.DialogFilter = new Dialog(KasbonPegawaiRekap.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
        this.rbt1 = (RadioButton) DialogFilter.findViewById(R.id.RbtSemua);
        this.rbt2 = (RadioButton) DialogFilter.findViewById(R.id.RbtAktif);
        this.rbt3 = (RadioButton) DialogFilter.findViewById(R.id.RbtNonAktif);
        this.RgFilter = (RadioGroup) DialogFilter.findViewById(R.id.RgFilter);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Kasbon Pegawai");

        Fstatus = TRUE_STRING;
        OrderBy = "nomor";
        ListData = new ArrayList<KasbonPegawaiModel>();
        this.ListRekap.setDividerHeight(1);

        tgl_dari   = serverNowStartOfTheMonthLong();
        tgl_Sampai = serverNowLong();
        IdPegawai  = 0;
        status     = 0;

        DbMaster = new KasbonPegawaiTable(this);
        DbMaster.SetRecords(ListData);
    }

    protected void EventClass(){
        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int Id =  ListData.get(i).getId();
                if (Id > 0) {
                    Intent s = new Intent(KasbonPegawaiRekap.this, KasbonPegawaiInput.class);
                    s.putExtra("MODE", JCons.DETAIL_MODE);
                    s.putExtra("ID",Id);
                    startActivity(s);
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(KasbonPegawaiRekap.this, FilterKasbonPegawai.class);
                s.putExtra("TGL_DARI", tgl_dari);
                s.putExtra("TGL_SAMPAI", tgl_Sampai);
                s.putExtra("STATUS", status);
                s.putExtra("PEGAWAI_ID", IdPegawai);
                KasbonPegawaiRekap.this.startActivityForResult(s, RESULT_LOV);
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

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(KasbonPegawaiRekap.this, KasbonPegawaiInput.class);
                s.putExtra("MODE","");
                startActivityForResult(s, 1);
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(KasbonPegawaiRekap.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_kabon_pegawai, PmFilter.getMenu());
                PmFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString().trim()){
                            case "Nomor" :
                                OrderBy = "nomor";
                                break;
                            case "Tanggal" :
                                OrderBy = "tanggal";
                                break;
                            case "Pegawai" :
                                OrderBy = "nama_pegawai";
                                break;
                            case "Jumlah" :
                                OrderBy = "jumlah";
                                break;
                            default:
                                OrderBy  = "";
                        }
                        LoadData();
                        return true;
                    }
                });
                PmFilter.show();
            }
        });

        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                KasbonPegawaiRekap.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        this.DbMaster.ReloadList(tgl_dari,tgl_Sampai,IdPegawai, status, OrderBy);
        Adapter = new KasbonPegawaiAdapter(this, R.layout.list_kasbon_pegawai_rekap, ListData);
        ListRekap.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasbon_pegawai_rekap);
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

//    public void LoadData(){
//        swipe.setRefreshing(true);
//        String filter;
//        Fstatus = "";
//        filter = "?tgl_dari="+getTglFormatMySql(tgl_dari)+ "&tgl_sampai="+getTglFormatMySql(tgl_Sampai)+ "&status="+status+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
//        String url = route.URL_SELECT_KASBON + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                KasbonPegawaiModel Data;
//                ListData.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new KasbonPegawaiModel(
//                                obj.getInt("id"),
//                                getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
//                                obj.getString("nomor"),
//                                obj.getInt("id_pegawai"),
//                                obj.getDouble("jumlah"),
//                                obj.getDouble("sisa"),
//                                obj.getInt("cicilan"),
//                                obj.getString("keterangan"),
//                                obj.getString("user_id"),
//                                getMillisDate(FormatDateFromSql(obj.getString("tgl_input"))),
//                                obj.getString("user_edit"),
//                                getMillisDate(FormatDateFromSql(obj.getString("tgl_edit")))
//                        );
//                        Data.setNama_pegawai(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
//                        ListData.add(Data);
//                    }
//                    //Satu baris kosong di akhir
//                    Data = new KasbonPegawaiModel(0,0,"",0,0,0,0,"","HIDE",0,"",0);
//                    ListData.add(Data);
//
//                    Adapter = new KasbonPegawaiAdapter(KasbonPegawaiRekap.this, R.layout.list_kasbon_pegawai_rekap, ListData);
//                    Adapter.notifyDataSetChanged();
//                    ListRekap.setAdapter(Adapter);
//                    swipe.setRefreshing(false);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(KasbonPegawaiRekap.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                    swipe.setRefreshing(false);
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                ListData.clear();
//                swipe.setRefreshing(false);
//                Toast.makeText(KasbonPegawaiRekap.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//    }
