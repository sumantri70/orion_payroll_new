package com.orionit.app.orion_payroll_new.form.laporan;

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
import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.PegawaiTable;
import com.orionit.app.orion_payroll_new.form.adapter.LaporanKasbonAdapter;
import com.orionit.app.orion_payroll_new.form.filter.FilterLaporanPenggajian;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModel;
import com.orionit.app.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.FormatDateFromSql;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormatMySql;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowLong;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.serverNowStartOfTheMonthLong;

public class Laporan_kasbon extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;
    private Dialog DialogFilter;

    private ImageButton btnFilter, btnSort;

    private SwipeRefreshLayout swipe;

    private List<LaporanKasbonModel> ListData;
    private ListView ListRekap;
    public static LaporanKasbonAdapter Adapter;
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

        this.DialogFilter = new Dialog(Laporan_kasbon.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Laporan Kasbon");

        Fstatus = TRUE_STRING;
        OrderBy = "tanggal";
        ListData = new ArrayList<LaporanKasbonModel>();
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
                Intent s = new Intent(Laporan_kasbon.this, FilterLaporanPenggajian.class);
                s.putExtra("TGL_DARI", tgl_dari);
                s.putExtra("TGL_SAMPAI", tgl_Sampai);
                s.putExtra("STATUS", status);
                s.putExtra("PEGAWAI_ID", IdPegawai);
                Laporan_kasbon.this.startActivityForResult(s, RESULT_LOV);
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
                PopupMenu PmFilter = new PopupMenu(Laporan_kasbon.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_laporan_kasbon, PmFilter.getMenu());
                PmFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString().trim()) {
                            case "Pegawai":
                                OrderBy = "pegawai";
                                break;
                            case "Tanggal":
                                OrderBy = "tanggal";
                                break;
                            case "Nomor":
                                OrderBy = "nomor";
                                break;
                            case "Jumlah":
                                OrderBy = "jumlah";
                                break;
                            case "Lama Cicilan":
                                OrderBy = "cicilan";
                                break;
                            case "Terbayar":
                                OrderBy = "terbayar";
                                break;
                            case "Sisa":
                                OrderBy = "sisa";
                            default:
                                OrderBy = "";
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
                Laporan_kasbon.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        Fstatus = "";
        filter = "?tgl_dari="+getTglFormatMySql(tgl_dari)+ "&tgl_sampai="+getTglFormatMySql(tgl_Sampai)+ "&status="+status+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
        String url = route.URL_SELECT_LAPORAN_KASBON + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LaporanKasbonModel Data;
                ListData.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new LaporanKasbonModel(
                                obj.getString("nomor"),
                                getMillisDate(FormatDateFromSql(obj.getString("tanggal"))),
                                obj.getInt("id_pegawai"),
                                obj.getInt("jumlah"),
                                obj.getInt("cicilan"),
                                obj.getDouble("terbayar"),
                                obj.getDouble("sisa")
                        );
                        ListData.add(Data);
                    }

                    Adapter = new LaporanKasbonAdapter(Laporan_kasbon.this, R.layout.list_laporan_kasbon, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Laporan_kasbon.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(Laporan_kasbon.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kasbon);
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

