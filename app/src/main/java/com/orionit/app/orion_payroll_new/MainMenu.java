package com.orionit.app.orion_payroll_new;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.orionit.app.orion_payroll_new.form.laporan.Laporan_kasbon;
import com.orionit.app.orion_payroll_new.form.laporan.Laporan_kasbon_new;
import com.orionit.app.orion_payroll_new.form.laporan.Laporan_penggajian;
import com.orionit.app.orion_payroll_new.form.master.PegawaiRekap;
import com.orionit.app.orion_payroll_new.form.master.PotonganRekap;
import com.orionit.app.orion_payroll_new.form.master.TunjanganRekap;
import com.orionit.app.orion_payroll_new.form.setting.SettingAplikasi;
import com.orionit.app.orion_payroll_new.form.transaksi.KasbonPegawaiRekap;
import com.orionit.app.orion_payroll_new.form.transaksi.PenggajianRekapNew;

public class MainMenu extends AppCompatActivity {
    Toolbar toolbar;
    private ImageButton btnSetting;
    private PopupMenu PopUpSetting;
    private CardView BtnMenuPegawai, BtnMenuTunjangan, BtnMenuPenggajian, BtnMenuPotongan, BtnMenuKasbonPegawai, BtnMenuLaporanPenggajian,
            BtnMenuAbsenPegawai, BtnMenuLaporanKasbon;


    protected void CreateView(){
        btnSetting           = findViewById(R.id.btnSetting);
        BtnMenuPegawai       = findViewById(R.id.BtnMenuPegawai);
        BtnMenuTunjangan     = findViewById(R.id.BtnMenuTunjangan);
        BtnMenuPotongan      = findViewById(R.id.BtnMenuPotongan);
        BtnMenuPenggajian    = findViewById(R.id.BtnMenuPenggajian);
        BtnMenuKasbonPegawai = findViewById(R.id.BtnMenuKasbonPegawai);
        BtnMenuLaporanPenggajian = findViewById(R.id.BtnMenuLaporanPenggajian);
        BtnMenuAbsenPegawai = findViewById(R.id.BtnMenuAbsenPegawai);
        BtnMenuLaporanKasbon= findViewById(R.id.BtnMenuLaporanKasbon);
    }

    protected void InitClass(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Orion Payroll");

        PopUpSetting = new PopupMenu(MainMenu.this, btnSetting);
        PopUpSetting.getMenu().add("Setting");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        CreateView();
        InitClass();


        BtnMenuPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, PegawaiRekap.class);
                startActivity(s);
            }
        });

        BtnMenuTunjangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, TunjanganRekap.class);
                startActivity(s);
            }
        });

        BtnMenuPotongan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, PotonganRekap.class);
                startActivity(s);
            }
        });

        BtnMenuPenggajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, PenggajianRekapNew.class);
                startActivity(s);
            }
        });

        BtnMenuKasbonPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, KasbonPegawaiRekap.class);
                startActivity(s);
            }
        });

        BtnMenuLaporanPenggajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent s = new Intent(MainMenu.this, Laporan_penggajian.class);
//                startActivity(s);
            }
        });


        BtnMenuLaporanKasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, Laporan_kasbon_new.class);
                startActivity(s);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpSetting.show();
            }
        });

        PopUpSetting.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent s = new Intent(MainMenu.this, SettingAplikasi.class);
                startActivity(s);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(MainMenu.this).create();
        dialog.setMessage("Yakin akan keluar ?");
        dialog.setCancelable(true);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                finishAffinity();
                System.exit(0);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

            }
        });
        dialog.show();
    }

}
