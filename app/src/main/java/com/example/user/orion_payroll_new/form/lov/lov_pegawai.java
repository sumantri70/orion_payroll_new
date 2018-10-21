package com.example.user.orion_payroll_new.form.lov;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.adapter.lov_pegawai_adapter;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;

public class lov_pegawai extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SearchView txtSearch;

    private ImageButton btnSort;
    private SwipeRefreshLayout swipe;

    private ListView ListRekap;
    public static lov_pegawai_adapter Adapter;
    private List<PegawaiModel> ListData;

    public static String Fstatus;
    public static String OrderBy;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekap);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    private void InitClass(){
        getSupportActionBar().hide();
        Fstatus = TRUE_STRING;
        OrderBy = "NIK";
        ListData = new ArrayList<PegawaiModel>();
        this.ListRekap.setDividerHeight(1);
    }

    protected void EventClass(){
        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PegawaiModel data;
                data =  ListData.get(i);
                if (data.getId() > 0) {
//                    if (OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(data.getId())).getClass() == null ){
//                        OrionPayrollApplication.getInstance().GetHashPegawai();
//                    }

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
                PopupMenu PmFilter = new PopupMenu(lov_pegawai.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_master_pegawai, PmFilter.getMenu());
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
                lov_pegawai.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        filter = "?status="+Fstatus+"&order_by="+OrderBy;
        String url = route.URL_SELECT_PEGAWAI + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PegawaiModel Data;
                ListData.clear();
                try {
                    Log.w("pegawaiiiiiiiiiiii", response.toString());
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new PegawaiModel(
                                obj.getInt("id"),
                                obj.getString("nik"),
                                obj.getString("nama"),
                                obj.getString("alamat"),
                                obj.getString("no_telpon_1"),
                                obj.getString("no_telpon_2"),
                                obj.getString("email"),
                                obj.getDouble("gaji_pokok"),
                                obj.getString("status"),
                                getMillisDate(obj.getString("tgl_lahir")),
                                getMillisDate(obj.getString("tgl_mulai_kerja")),
                                obj.getString("keterangan")
                        );
                        ListData.add(Data);
                    }
                    Adapter = new lov_pegawai_adapter(lov_pegawai.this, R.layout.list_lov_pegawai, ListData);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(lov_pegawai.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListData.clear();
                swipe.setRefreshing(false);
                Toast.makeText(lov_pegawai.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lov_pegawai);
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
