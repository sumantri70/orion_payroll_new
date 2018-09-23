package com.example.user.orion_payroll_new.form.master;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.form.adapter.PegawaiAdapter;
import com.example.user.orion_payroll_new.form.adapter.TunjanganAdapter;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;

public class TunjanganRekap extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SearchView txtSearch;
    private RadioGroup RgFilter;
    private Dialog DialogFilter;
    private RadioButton rbt1, rbt2, rbt3;

    private ImageButton btnFilter, btnSort;
    private FloatingActionButton btnTambah;
    private CardView CdSearch;

    private SwipeRefreshLayout swipe;

    private LayoutInflater inflater;
    private View dialogView;

    private ListView ListRekap;
    public static TunjanganAdapter Adapter;
    public static TunjanganTable Data;
    private List<TunjanganModel> ListTunjangan;

    public static String Fstatus;
    public static String OrderBy;
    private ProgressDialog Loading;

    static final int FROM_INPUT_TUNJANGAN = 1;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekapTunjangan);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnFilter  = (ImageButton) findViewById(R.id.BtnFilter);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.CdSearch   = (CardView) findViewById(R.id.CdSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);

        this.DialogFilter = new Dialog(TunjanganRekap.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
        this.rbt1 = (RadioButton) DialogFilter.findViewById(R.id.RbtSemua);
        this.rbt2 = (RadioButton) DialogFilter.findViewById(R.id.RbtAktif);
        this.rbt3 = (RadioButton) DialogFilter.findViewById(R.id.RbtNonAktif);
        this.RgFilter = (RadioGroup) DialogFilter.findViewById(R.id.RgFilter);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tunjangan");

        Fstatus = TRUE_STRING;
        OrderBy = "kode";
        ListTunjangan = new ArrayList<TunjanganModel>();
        Loading = new ProgressDialog(TunjanganRekap.this);
        this.Data =  new TunjanganTable(getApplicationContext());
        this.Adapter = new TunjanganAdapter(TunjanganRekap.this, R.layout.list_tunjangan_rekap, this.Data.GetRecords());
        this.ListRekap.setAdapter(Adapter);
        this.ListRekap.setDividerHeight(1);
    }

    protected void EventClass(){
        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Data.GetDataByIndex(i).getId() > 0) {
                    Intent s = new Intent(TunjanganRekap.this, TunjanganInput.class);
                    s.putExtra("MODE", JCons.DETAIL_MODE);
                    s.putExtra("POSITION",i);
                    s.putExtra("ID",Data.GetDataByIndex(i).getId());
                    startActivity(s);
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()            {

                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                        int SelectedId = RgFilter.getCheckedRadioButtonId();
                        switch (SelectedId){
                            case R.id.RbtSemua :
                                Fstatus = "";
                                break;
                            case R.id.RbtAktif :
                                Fstatus = TRUE_STRING;
                                break;
                            case R.id.RbtNonAktif:
                                Fstatus = FALSE_STRING;
                                break;
                            default:
                                Fstatus = "";
                        }
                        LoadData();
                        DialogFilter.dismiss();
                    }
                });
                DialogFilter.show();
            }
        });

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           Adapter.notifyDataSetChanged();
                           swipe.setRefreshing(false);
                       }
                   }
        );

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CdSearch.animate().translationY(-50);
                Intent s = new Intent(TunjanganRekap.this, TunjanganInput.class);
                s.putExtra("MODE","");
                s.putExtra("POSITION",0);
                startActivityForResult(s, FROM_INPUT_TUNJANGAN);
            }
        });


        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(TunjanganRekap.this, btnSort);
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
                TunjanganRekap.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        filter = "?status="+Fstatus+"&order_by="+OrderBy;
        String url = route.URL_SELECT_TUNJANGAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TunjanganModel Data;
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new TunjanganModel(
                                obj.getInt("id"),
                                obj.getString("kode"),
                                obj.getString("nama"),
                                obj.getString("keterangan"),
                                obj.getString("status")
                        );
                        ListTunjangan.add(Data);
                    }
                    //Satu baris kosong di akhir
                    Data = new TunjanganModel(0,"","","","HIDE");
                    ListTunjangan.add(Data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);
        ListTunjangan.clear();
        this.Adapter = new TunjanganAdapter(TunjanganRekap.this, R.layout.list_tunjangan_rekap, ListTunjangan);
        this.Adapter.notifyDataSetChanged();
        this.ListRekap.setAdapter(Adapter);
        swipe.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunjangan_rekap);
        CreateVew();
        InitClass();
        EventClass();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.Adapter.notifyDataSetChanged();
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
        if (requestCode == FROM_INPUT_TUNJANGAN) {
            if (resultCode == RESULT_OK) {
                LoadData();
            }else{

            }
        }
    }


}
