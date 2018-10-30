package com.example.user.orion_payroll_new.form.transaksi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.user.orion_payroll_new.form.filter.FilterPenggajian;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PenggajianModel;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getTglFormatMySql;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;
import static com.example.user.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class PenggajianRekapNew extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;

    private RadioButton rbt1, rbt2, rbt3;

    private ImageButton btnFilter, btnSort;
    private FloatingActionButton btnTambah;

    private SwipeRefreshLayout swipe;

    private List<PenggajianModel> ListData;
    private ListView ListRekap;
    public static PenggajianAdapterNew Adapter;

    public static String OrderBy;

    private Long tgl_dari, tgl_Sampai;
    private int IdPegawai;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnFilter  = (ImageButton) findViewById(R.id.BtnFilter);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Penggajian");


        OrderBy = "tanggal";
        ListData = new ArrayList<PenggajianModel>();
        this.ListRekap.setDividerHeight(1);

        tgl_dari   = serverNowStartOfTheMonthLong();
        tgl_Sampai = serverNowLong();
        IdPegawai  = 0;
    }

    protected void EventClass(){
        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int Id =  ListData.get(i).getId();
                if (Id > 0) {
                    Intent s = new Intent(PenggajianRekapNew.this, PenggajianInputNew.class);
                    s.putExtra("MODE", JCons.DETAIL_MODE);
                    s.putExtra("ID",Id);
                    startActivity(s);
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(PenggajianRekapNew.this, FilterPenggajian.class);
                s.putExtra("TGL_DARI", tgl_dari);
                s.putExtra("TGL_SAMPAI", tgl_Sampai);
                s.putExtra("PEGAWAI_ID", IdPegawai);
                PenggajianRekapNew.this.startActivityForResult(s, RESULT_LOV);
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
                Intent s = new Intent(PenggajianRekapNew.this, PenggajianInputNew.class);
                s.putExtra("MODE","");
                startActivityForResult(s, 1);
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(PenggajianRekapNew.this, btnSort);
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
                            case "Total" :
                                OrderBy = "total";
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
                PenggajianRekapNew.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        filter = "?tgl_dari="+getTglFormatMySql(tgl_dari)+ "&tgl_sampai="+getTglFormatMySql(tgl_Sampai)+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
        String url = route.URL_SELECT_PENGGAJIAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PenggajianModel Data;
                ListData.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new PenggajianModel(
                                obj.getInt("id"),
                                obj.getInt("id_pegawai"),
                                obj.getInt("telat_satu"),
                                obj.getInt("telat_dua"),
                                obj.getInt("dokter"),
                                obj.getInt("izin_stgh_hari"),
                                obj.getInt("izin_cuti"),
                                obj.getInt("izin_non_cuti"),
                                obj.getString("nomor"),
                                obj.getString("keterangan"),
                                obj.getString("user_id"),
                                obj.getString("user_edit"),
                                obj.getDouble("gaji_pokok"),
                                obj.getDouble("uang_ikatan"),
                                obj.getDouble("uang_kehadiran"),
                                obj.getDouble("premi_harian"),
                                obj.getDouble("premi_perjam"),
                                obj.getDouble("jam_lembur"),
                                obj.getDouble("total_tunjangan"),
                                obj.getDouble("total_potongan"),
                                obj.getDouble("total_lembur"),
                                obj.getDouble("total_kasbon"),
                                obj.getDouble("total"),
                                getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
                                getMillisDate(FormatDateFromSql(obj.getString("tgl_input"))),
                                getMillisDate(FormatDateFromSql(obj.getString("tgl_edit"))),
                                getMillisDate(FormatDateFromSql(obj.getString("periode")))
                        );
                        Data.setNama_pegawai(Get_Nama_Master_Pegawai(Data.getId_pegawai()));
                        ListData.add(Data);
                    }
                    //Satu baris kosong di akhir
                    Data = new PenggajianModel();
                    Data.setUser_id("HIDE");
                    ListData.add(Data);

                    Adapter = new PenggajianAdapterNew(PenggajianRekapNew.this, R.layout.list_penggajian_rekap_new, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PenggajianRekapNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(PenggajianRekapNew.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penggajian_rekap_new);
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
            }
            LoadData();
        }
    }
}

