package com.example.user.orion_payroll_new;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.orion_payroll_new.email.MainActivity;
import com.example.user.orion_payroll_new.form.AbsenPegawai;
import com.example.user.orion_payroll_new.form.laporan.Laporan_kasbon;
import com.example.user.orion_payroll_new.form.laporan.Laporan_penggajian;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.form.master.PotonganRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganRekap;
import com.example.user.orion_payroll_new.form.transaksi.KasbonPegawaiRekap;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianRekapNew;

public class MainMenuNew extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CardView BtnMenuPegawai, BtnMenuTunjangan, BtnMenuPenggajian, BtnMenuPotongan, BtnMenuKasbonPegawai, BtnMenuLaporanPenggajian,
            BtnMenuAbsenPegawai, BtnMenuLaporanKasbon;

    protected void CreateView(){
        BtnMenuPegawai       = findViewById(R.id.BtnMenuPegawai);
        BtnMenuTunjangan     = findViewById(R.id.BtnMenuTunjangan);
        BtnMenuPotongan      = findViewById(R.id.BtnMenuPotongan);
        BtnMenuPenggajian    = findViewById(R.id.BtnMenuPenggajian);
        BtnMenuKasbonPegawai = findViewById(R.id.BtnMenuKasbonPegawai);
        BtnMenuLaporanPenggajian = findViewById(R.id.BtnMenuLaporanPenggajian);
        BtnMenuAbsenPegawai = findViewById(R.id.BtnMenuAbsenPegawai);
        BtnMenuLaporanKasbon= findViewById(R.id.BtnMenuLaporanKasbon);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle("Orion Payroll");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CreateView();


        BtnMenuPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, PegawaiRekap.class);
                startActivity(s);
            }
        });

        BtnMenuTunjangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, TunjanganRekap.class);
                startActivity(s);
            }
        });

        BtnMenuPotongan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, PotonganRekap.class);
                startActivity(s);
            }
        });

        BtnMenuPenggajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, PenggajianRekapNew.class);
                startActivity(s);
            }
        });

        BtnMenuKasbonPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, KasbonPegawaiRekap.class);
                startActivity(s);

//            Intent s = new Intent(MainMenu.this, androidpdfcreator.class);
//            startActivity(s);
            }
        });

        BtnMenuLaporanPenggajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, Laporan_penggajian.class);
                startActivity(s);
            }
        });

        BtnMenuAbsenPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, AbsenPegawai.class);
                startActivity(s);
//                Intent s = new Intent(MainMenuNew.this, MainActivity.class);
//                startActivity(s);
            }
        });

        BtnMenuLaporanKasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenuNew.this, Laporan_kasbon.class);
                startActivity(s);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_keluar) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

//        }

        if (id == R.id.nav_keluar) {
            Intent s = new Intent(MainMenuNew.this, Login.class);
            startActivity(s);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
