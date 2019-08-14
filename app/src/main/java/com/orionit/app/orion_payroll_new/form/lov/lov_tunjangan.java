package com.orionit.app.orion_payroll_new.form.lov;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.database.master.TunjanganTable;
import com.orionit.app.orion_payroll_new.form.adapter.lov_tunjangan_adapter;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;

import java.util.ArrayList;

import static com.orionit.app.orion_payroll_new.models.JCons.TRUE_STRING;

public class lov_tunjangan extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SearchView txtSearch;
    private RadioGroup RgFilter;

    private ImageButton btnSort;
    private SwipeRefreshLayout swipe;

    private ListView ListRekap;
    public static lov_tunjangan_adapter Adapter;
    private ArrayList<TunjanganModel> ListTunjangan;

    public static String Fstatus;
    public static String OrderBy;
    private TunjanganTable DbMaster;
    private boolean IsMunculkanTunjanganSystem;
    private String TanpaId;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekapTunjangan);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnSort.setVisibility(View.INVISIBLE);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    private void InitClass(){
        getSupportActionBar().hide();
        Fstatus = TRUE_STRING;
        OrderBy = "kode";

        Bundle extra = this.getIntent().getExtras();
        IsMunculkanTunjanganSystem = extra.getBoolean("JANGANMUNCULKANSYSTEM");
        TanpaId = extra.getString("TANPA_ID");

        ListTunjangan = new ArrayList<TunjanganModel>();
        this.ListRekap.setDividerHeight(1);

        DbMaster = new TunjanganTable(this);
        DbMaster.SetRecords(ListTunjangan);
    }

    protected void EventClass(){
        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TunjanganModel data;
            data =  Adapter.getItem(i);
                if (data.getId() > 0) {
                    Intent intent = getIntent();
                    intent.putExtra("id", data.getId() );
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
                PopupMenu PmFilter = new PopupMenu(lov_tunjangan.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_master_tunjangan, PmFilter.getMenu());
                PmFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString().trim()){
                            case "Kode" :
                                OrderBy = "kode";
                                break;
                            case "Nama" :
                                OrderBy = "nama";
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
                lov_tunjangan.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        this.DbMaster.ReloadList(Fstatus, OrderBy,IsMunculkanTunjanganSystem, TanpaId);
        Adapter = new lov_tunjangan_adapter(this, R.layout.list_lov_tunjangan, ListTunjangan);
        ListRekap.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lov_tunjangan);
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

}


//    public void LoadData(){
//        swipe.setRefreshing(true);
//        String filter;
//        filter = "?status="+Fstatus+"&order_by="+OrderBy;
//        String url = route.URL_SELECT_TUNJANGAN + filter;
//        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                TunjanganModel Data;
//                ListTunjangan.clear();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Data = new TunjanganModel(
//                                obj.getInt("id"),
//                                obj.getString("kode"),
//                                obj.getString("nama"),
//                                obj.getString("keterangan"),
//                                obj.getString("status")
//                        );
//                        ListTunjangan.add(Data);
//                    }
//                    Adapter = new lov_tunjangan_adapter(lov_tunjangan.this, R.layout.list_lov_tunjangan, ListTunjangan);
//                    Adapter.notifyDataSetChanged();
//                    ListRekap.setAdapter(Adapter);
//                    swipe.setRefreshing(false);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(lov_tunjangan.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//                    swipe.setRefreshing(false);
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                ListTunjangan.clear();
//                swipe.setRefreshing(false);
//                Toast.makeText(lov_tunjangan.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
//            }
//        });
//        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
//
//    }
