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
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.adapter.KasbonPegawaiAdapter;
import com.example.user.orion_payroll_new.form.filter.FilterKasbonPegawai;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.KasbonPegawaiModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.serverNowLong;

public class KasbonPegawaiRekap extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;
    private RadioGroup RgFilter;
    private Dialog DialogFilter;
    private RadioButton rbt1, rbt2, rbt3;

    private ImageButton btnFilter, btnSort;
    private FloatingActionButton btnTambah;

    private SwipeRefreshLayout swipe;

    private List<KasbonPegawaiModel> ListData;
    private ListView ListRekap;
    public static KasbonPegawaiAdapter Adapter;
    public static PegawaiTable Data;

    public static String Fstatus;
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

        this.DialogFilter = new Dialog(KasbonPegawaiRekap.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
        this.rbt1 = (RadioButton) DialogFilter.findViewById(R.id.RbtSemua);
        this.rbt2 = (RadioButton) DialogFilter.findViewById(R.id.RbtAktif);
        this.rbt3 = (RadioButton) DialogFilter.findViewById(R.id.RbtNonAktif);
        this.RgFilter = (RadioGroup) DialogFilter.findViewById(R.id.RgFilter);
    }

    private void InitClass(){
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_group_black_24dp); buat munculin icon
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Kasbon Pegawai");

        Fstatus = TRUE_STRING;
        OrderBy = "tanggal";
        ListData = new ArrayList<KasbonPegawaiModel>();
        //Kodeing buat ngilangin garis
        //this.ListRekap.setDivider(null);
        this.ListRekap.setDividerHeight(1);

        tgl_dari   = serverNowLong();
        tgl_Sampai = serverNowLong();
        IdPegawai  = 0;
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
                s.putExtra("STATUS", 0);
                s.putExtra("PEGAWAI_ID", 73);
                startActivity(s);
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
                //CdSearch.animate().translationY(-50);
                Intent s = new Intent(KasbonPegawaiRekap.this, KasbonPegawaiInput.class);
                s.putExtra("MODE","");
                startActivityForResult(s, 1);
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(KasbonPegawaiRekap.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_master_pegawai, PmFilter.getMenu());
                PmFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString().trim()){
                            case "NIK" :
                                OrderBy = "NIK";
                                break;
                            case "Nama" :
                                OrderBy = "nama";
                                break;
                            case "Gaji" :
                                OrderBy = "gaji_pokok";
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
        String filter;
        Fstatus = "";
        filter = "?tgl_dari="+"01-10-2018"+ "&tgl_sampai="+"29-10-2018"+ "&status="+Fstatus+ "&id_pegawai="+Fstatus+"&order_by="+OrderBy;
        String url = route.URL_SELECT_KASBON + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                KasbonPegawaiModel Data;
                ListData.clear();
                Log.d("errorrrr",response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new KasbonPegawaiModel(
                                obj.getInt("id"),
                                getMillisDate(obj.getString("tanggal")),
                                obj.getString("nomor"),
                                obj.getInt("id_pegawai"),
                                obj.getDouble("jumlah"),
                                obj.getDouble("sisa"),
                                obj.getInt("cicilan"),                                
                                obj.getString("keterangan"),
                                obj.getString("user_id"),
                                getMillisDate(obj.getString("tgl_input")),
                                obj.getString("user_Edit"),
                                getMillisDate(obj.getString("tgl_edit"))
                        );
                        ListData.add(Data);
                    }
                    //Satu baris kosong di akhir
                    Data = new KasbonPegawaiModel(0,0,"",0,0,0,0,"","HIDE",0,"",0);
                    ListData.add(Data);

                    Adapter = new KasbonPegawaiAdapter(KasbonPegawaiRekap.this, R.layout.list_kasbon_pegawai_rekap, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(KasbonPegawaiRekap.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(KasbonPegawaiRekap.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
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
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                LoadData();
            }else{

            }
        }
    }
}

