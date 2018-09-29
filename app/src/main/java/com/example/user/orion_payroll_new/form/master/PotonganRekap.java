package com.example.user.orion_payroll_new.form.master;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.example.user.orion_payroll_new.form.adapter.PotonganAdapter;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PotonganModel;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;

public class PotonganRekap extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SearchView txtSearch;
    private RadioGroup RgFilter;
    private Dialog DialogFilter;
    private RadioButton rbt1, rbt2, rbt3;

    private ImageButton btnFilter, btnSort;
    private FloatingActionButton btnTambah;
    private CardView CdSearch;

    private SwipeRefreshLayout swipe;

    private ListView ListRekap;
    public static PotonganAdapter Adapter;
    private List<PotonganModel> ListPotongan;

    public static String Fstatus;
    public static String OrderBy;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekapPotongan);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnFilter  = (ImageButton) findViewById(R.id.BtnFilter);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.CdSearch   = (CardView) findViewById(R.id.CdSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);

        this.DialogFilter = new Dialog(PotonganRekap.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
        this.rbt1 = (RadioButton) DialogFilter.findViewById(R.id.RbtSemua);
        this.rbt2 = (RadioButton) DialogFilter.findViewById(R.id.RbtAktif);
        this.rbt3 = (RadioButton) DialogFilter.findViewById(R.id.RbtNonAktif);
        this.RgFilter = (RadioGroup) DialogFilter.findViewById(R.id.RgFilter);
    }

    private void InitClass(){
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Potongan");

        Fstatus = TRUE_STRING;
        OrderBy = "kode";
        ListPotongan = new ArrayList<PotonganModel>();
        this.ListRekap.setDividerHeight(1);
    }

    protected void EventClass(){
        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int Id =  ListPotongan.get(i).getId();
                if (Id > 0) {
                    Intent s = new Intent(PotonganRekap.this, PotonganInput.class);
                    s.putExtra("MODE", JCons.DETAIL_MODE);
                    s.putExtra("ID",Id);
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

                switch (Fstatus){
                    case "T" :
                        RgFilter.check(R.id.RbtAktif);
                        break;
                    case "F" :
                        RgFilter.check(R.id.RbtNonAktif);
                        break;
                    default:
                        RgFilter.check(R.id.RbtSemua);
                }
                DialogFilter.show();
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
                Intent s = new Intent(PotonganRekap.this, PotonganInput.class);
                s.putExtra("MODE","");
                startActivityForResult(s, 1);
            }
        });


        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(PotonganRekap.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.sort_master_potongan, PmFilter.getMenu());
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
                PotonganRekap.this.Adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void LoadData(){
        swipe.setRefreshing(true);
        String filter;
        filter = "?status="+Fstatus+"&order_by="+OrderBy;
        String url = route.URL_SELECT_POTONGAN + filter;
        JsonObjectRequest jArr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PotonganModel Data;
                ListPotongan.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Data = new PotonganModel(
                                obj.getInt("id"),
                                obj.getString("kode"),
                                obj.getString("nama"),
                                obj.getString("keterangan"),
                                obj.getString("status")
                        );
                        ListPotongan.add(Data);
                    }
                    //Satu baris kosong di akhir
                    Data = new PotonganModel(0,"","","","HIDE");
                    ListPotongan.add(Data);

                    Adapter = new PotonganAdapter(PotonganRekap.this, R.layout.list_potongan_rekap, ListPotongan);
                    Adapter.notifyDataSetChanged();
                    ListRekap.setAdapter(Adapter);
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PotonganRekap.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListPotongan.clear();
                swipe.setRefreshing(false);
                Toast.makeText(PotonganRekap.this, MSG_UNSUCCESS_CONECT, Toast.LENGTH_SHORT).show();
            }
        });
        OrionPayrollApplication.getInstance().addToRequestQueue(jArr);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potongan_rekap);
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
