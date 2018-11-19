package com.example.user.orion_payroll_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.user.orion_payroll_new.email.MainActivity;
import com.example.user.orion_payroll_new.form.AbsenPegawai;
import com.example.user.orion_payroll_new.form.laporan.Laporan_kasbon;
import com.example.user.orion_payroll_new.form.laporan.Laporan_penggajian;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.form.master.PotonganRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganRekap;
import com.example.user.orion_payroll_new.form.transaksi.KasbonPegawaiRekap;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianInput;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianRekapNew;

public class MainMenu extends AppCompatActivity {
    Toolbar toolbar;
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

    protected void InitClass(){
        this.setTitle("Orion Payroll");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        CreateView();
        InitClass();
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


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

//            Intent s = new Intent(MainMenu.this, androidpdfcreator.class);
//            startActivity(s);
            }
        });

        BtnMenuLaporanPenggajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, Laporan_penggajian.class);
                startActivity(s);
            }
        });

        BtnMenuAbsenPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent s = new Intent(MainMenu.this, AbsenPegawai.class);
//                startActivity(s);
                Intent s = new Intent(MainMenu.this, MainActivity.class);
                startActivity(s);
            }
        });

        BtnMenuLaporanKasbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, Laporan_kasbon.class);
                startActivity(s);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menumenu, menu);
        return true;
    }

}
