package com.example.user.orion_payroll_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganRekap;
import com.example.user.orion_payroll_new.form.transaksi.PenggajianInput;

public class MainMenu extends AppCompatActivity {
    Toolbar toolbar;
    private CardView BtnMenuPegawai, BtnMenuTunjangan, BtnMenuGajiPegawai;


    protected void CreateView(){
        BtnMenuPegawai = findViewById(R.id.BtnMenuPegawai);
        BtnMenuTunjangan = findViewById(R.id.BtnMenuTunjangan);
        BtnMenuGajiPegawai = findViewById(R.id.BtnMenuGajiPegawai);
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

        BtnMenuGajiPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(MainMenu.this, PenggajianInput.class);
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
