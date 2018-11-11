package com.example.user.orion_payroll_new.form.laporan;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.adapter.LaporanPenggajianAdapter;
import com.example.user.orion_payroll_new.form.filter.FilterLaporanPenggajian;
import com.example.user.orion_payroll_new.models.LaporanPenggajianModel;
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

public class Laporan_penggajian extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;
    private Dialog DialogFilter;


    private ImageButton btnFilter, btnSort;

    private SwipeRefreshLayout swipe;

    private List<LaporanPenggajianModel> ListData;
    private ListView ListRekap;
    public static LaporanPenggajianAdapter Adapter;
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

        this.DialogFilter = new Dialog(Laporan_penggajian.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
    }

    private void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Laporan Penggajian");

        Fstatus = TRUE_STRING;
        OrderBy = "tanggal";
        ListData = new ArrayList<LaporanPenggajianModel>();
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
                Intent s = new Intent(Laporan_penggajian.this, FilterLaporanPenggajian.class);
                s.putExtra("TGL_DARI", tgl_dari);
                s.putExtra("TGL_SAMPAI", tgl_Sampai);
                s.putExtra("STATUS", status);
                s.putExtra("PEGAWAI_ID", IdPegawai);
                Laporan_penggajian.this.startActivityForResult(s, RESULT_LOV);
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
                PopupMenu PmFilter = new PopupMenu(Laporan_penggajian.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_laporan_penggajian, PmFilter.getMenu());
                PmFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString().trim()) {
                            case "Periode":
                                OrderBy = "periode";
                                break;
                            case "Gaji Pokok":
                                OrderBy = "gaji_pokok";
                                break;
                            case "Total Tunjangan":
                                OrderBy = "total_tunjangan";
                                break;
                            case "Total Potongan":
                                OrderBy = "total_potongan";
                                break;
                            case "Total Kasbon":
                                OrderBy = "total_kasbon";
                                break;
                            case "Total Lembur":
                                OrderBy = "total_lembur";
                                break;
                            case "Total":
                                OrderBy = "total";
                                break;
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
                Laporan_penggajian.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        Fstatus = "";
        filter = "?tgl_dari="+getTglFormatMySql(tgl_dari)+ "&tgl_sampai="+getTglFormatMySql(tgl_Sampai)+ "&status="+status+ "&id_pegawai="+Integer.toString(IdPegawai)+"&order_by="+OrderBy;
        String url = route.URL_SELECT_LAPORAN_PENGGAJIAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LaporanPenggajianModel Data;
                ListData.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new LaporanPenggajianModel(
                                getMillisDate(FormatDateFromSql(obj.getString("periode"))),
                                obj.getDouble("gaji_pokok"),
                                obj.getDouble("total_tunjangan"),
                                obj.getDouble("total_potongan"),
                                obj.getDouble("total_kasbon"),
                                obj.getDouble("total_lembur"),
                                obj.getDouble("total")
                        );
                        ListData.add(Data);
                    }

                    Adapter = new LaporanPenggajianAdapter(Laporan_penggajian.this, R.layout.list_laporan_penggajian, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Laporan_penggajian.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(Laporan_penggajian.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penggajian);
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

