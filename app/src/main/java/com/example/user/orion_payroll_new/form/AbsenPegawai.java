package com.example.user.orion_payroll_new.form;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.adapter.AbsenPegawaiAdapter;
import com.example.user.orion_payroll_new.form.filter.FilterLaporanPenggajian;
import com.example.user.orion_payroll_new.models.AbsenPegawaiModel;
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

public class AbsenPegawai extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;
    private Dialog DialogFilter;


    private ImageButton btnFilter, btnSort;

    private SwipeRefreshLayout swipe;

    private List<AbsenPegawaiModel> ListData;
    private ListView ListRekap;
    public static AbsenPegawaiAdapter Adapter;
    public static PegawaiTable Data;

    public static String Fstatus;
    public static String OrderBy;

    private Long tgl_dari, tgl_Sampai;
    private int IdPegawai, status;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnFilter  = (ImageButton) findViewById(R.id.BtnFilter);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);

        this.DialogFilter = new Dialog(AbsenPegawai.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Absen Pegawai");

        Fstatus = TRUE_STRING;
        OrderBy = "tanggal";
        ListData = new ArrayList<AbsenPegawaiModel>();
        //Kodeing buat ngilangin garis
        //this.ListRekap.setDivider(null);
        this.ListRekap.setDividerHeight(1);

        tgl_dari   = serverNowStartOfTheMonthLong();
        tgl_Sampai = serverNowLong();
        IdPegawai  = 0;
        status     = 0;

        this.ListRekap.setDivider(null);
        this.ListRekap.setDividerHeight(15);
    }

    protected void EventClass(){

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(AbsenPegawai.this, FilterLaporanPenggajian.class);
                s.putExtra("TGL_DARI", tgl_dari);
                s.putExtra("TGL_SAMPAI", tgl_Sampai);
                s.putExtra("STATUS", status);
                s.putExtra("PEGAWAI_ID", IdPegawai);
                AbsenPegawai.this.startActivityForResult(s, RESULT_LOV);
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

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(AbsenPegawai.this, btnSort);
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
                AbsenPegawai.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        Fstatus = "";
        filter = "?tgl_dari="+getTglFormatMySql(tgl_dari)+ "&tgl_sampai="+getTglFormatMySql(tgl_Sampai)+ "&status="+status+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
        String url = route.URL_SELECT_ABSEN_PEGAWAI + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AbsenPegawaiModel Data;
                ListData.clear();

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new AbsenPegawaiModel(0,
                                obj.getInt("id_pegawai"),
                                getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
                                "",
                                "",
                                obj.getInt("telat_satu"),
                                obj.getInt("telat_dua"),
                                obj.getInt("dokter"),
                                obj.getInt("izin_stgh_hari"),
                                obj.getInt("izin_cuti"),
                                obj.getInt("izin_non_cuti"),
                                obj.getInt("masuk")
                        );
                        ListData.add(Data);
                    }

                    Adapter = new AbsenPegawaiAdapter(AbsenPegawai.this, R.layout.list_absen_pegawai, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AbsenPegawai.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(AbsenPegawai.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_pegawai);
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

