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

import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.adapter.PegawaiAdapter;
import com.example.user.orion_payroll_new.models.JCons;

import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;

public class PegawaiRekap extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
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
    public static PegawaiAdapter Adapter;
    public static PegawaiTable Data;

    public static String Fstatus;
    public static String OrderBy;

    private void CreateVew(){
        this.ListRekap  = (ListView) findViewById(R.id.ListRekapPegawai);
        this.btnTambah  = (FloatingActionButton) findViewById(R.id.btnTambah);
        this.btnSort    = (ImageButton) findViewById(R.id.BtnSort);
        this.btnFilter  = (ImageButton) findViewById(R.id.BtnFilter);
        this.txtSearch  = (SearchView) findViewById(R.id.txtSearch);
        this.CdSearch   = (CardView) findViewById(R.id.CdSearch);
        this.swipe      = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(Color.DKGRAY, Color.GREEN, Color.BLUE, Color.CYAN);

        this.DialogFilter = new Dialog(PegawaiRekap.this);
        this.DialogFilter.setContentView(R.layout.filter_aktivasi);
        this.DialogFilter.setTitle("Filter");
        this.rbt1 = (RadioButton) DialogFilter.findViewById(R.id.RbtSemua);
        this.rbt2 = (RadioButton) DialogFilter.findViewById(R.id.RbtAktif);
        this.rbt3 = (RadioButton) DialogFilter.findViewById(R.id.RbtNonAktif);
        this.RgFilter = (RadioGroup) DialogFilter.findViewById(R.id.RgFilter);
        //this.btnOk = (Button) dialog.findViewById(R.id.BtnOK);
    }

    private void InitClass(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_group_black_24dp); buat munculin icon
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Pegawai");

        Fstatus = TRUE_STRING;
        OrderBy = "NIK";

        this.Data = ((OrionPayrollApplication)getApplicationContext()).TPegawai;
        this.Adapter = new PegawaiAdapter(PegawaiRekap.this, R.layout.list_pegawai_rekap, this.Data.GetRecords());
        this.ListRekap.setAdapter(Adapter);
    }

    private void LoadData(){
        swipe.setRefreshing(true);
        Data.ReloadList(Fstatus, OrderBy);
        PegawaiRekap.this.Adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai_rekap);
        CreateVew();
        InitClass();

        ListRekap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Data.GetDataKaryawanByIndex(i).getId() > 0) {
                    Intent s = new Intent(PegawaiRekap.this, PegawaiInput.class);
                    s.putExtra("MODE", JCons.DETAIL_MODE);
                    s.putExtra("POSITION",i);
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
                Intent s = new Intent(PegawaiRekap.this, PegawaiInput.class);
                s.putExtra("MODE","");
                s.putExtra("POSITION",0);
                startActivity(s);
                LoadData();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(PegawaiRekap.this, btnSort);
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
                            default:
                                OrderBy  = "";
                        }
                        //Toast.makeText(PegawaiRekap.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                Adapter.getFilter().filter(newText);
                return false;
            }
        });


        /*ListRekap.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //CdSearch.setVisibility(View.GONE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/
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
        swipe.setRefreshing(true);
        Adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }
}
