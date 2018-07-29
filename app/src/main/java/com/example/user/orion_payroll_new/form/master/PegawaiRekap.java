package com.example.user.orion_payroll_new.form.master;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.PegawaiTable;
import com.example.user.orion_payroll_new.form.adapter.PegawaiAdapter;
import com.example.user.orion_payroll_new.form.search;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.PegawaiModel;

import java.util.List;

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
        getSupportActionBar().setLogo(R.drawable.ic_group_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("  Pegawai");

        Fstatus = TRUE_STRING;
        OrderBy = "NIK";

        this.Data = ((OrionPayrollApplication)getApplicationContext()).TPegawai;
        this.Adapter = new PegawaiAdapter(PegawaiRekap.this, R.layout.list_pegawai_rekap, this.Data.GetRecords());
        this.ListRekap.setAdapter(Adapter);
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
                Data.ReloadList(Fstatus, OrderBy);
                PegawaiRekap.this.Adapter.notifyDataSetChanged();
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
                PegawaiRekap.this.Adapter.notifyDataSetChanged();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu PmFilter = new PopupMenu(PegawaiRekap.this, btnSort);
                PmFilter.getMenuInflater().inflate(R.menu.filter_master_pegawai, PmFilter.getMenu());

                PmFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(PegawaiRekap.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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


    final int CONTEXT_MENU_VIEW = 1;
    final int CONTEXT_MENU_EDIT = 2;
    final int CONTEXT_MENU_ARCHIVE = 3;
    public void onCreateContextMenu(ContextMenu menu, View tampil,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (tampil.getId() == R.id.ListRekapPegawai) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("My Context Menu");
            menu.add(Menu.NONE, CONTEXT_MENU_VIEW, Menu.NONE, "Add");
            menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit");
            menu.add(Menu.NONE, CONTEXT_MENU_ARCHIVE, Menu.NONE, "Delete");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        Log.w("asup","asuppp");

        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case CONTEXT_MENU_VIEW: {

            }
            break;
            case CONTEXT_MENU_EDIT: {
                // Edit Action

            }
            break;
            case CONTEXT_MENU_ARCHIVE: {

            }
            break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        Adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }
}
