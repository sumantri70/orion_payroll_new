package com.example.user.orion_payroll_new;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.orion_payroll_new.form.master.PegawaiInput;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;

import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;

public class Login extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    ConstraintLayout Layouts;
    Button BtnLogin;
    EditText txtUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Layouts = (ConstraintLayout)findViewById(R.id.Layouts);
        txtUserId = (EditText) findViewById(R.id.txtUserId);
        BtnLogin = (Button) findViewById(R.id.BtnLogin);

        /* animasi
        animationDrawable = (AnimationDrawable) Layouts.getBackground();
        animationDrawable.setEnterFadeDuration(100);
        animationDrawable.setExitFadeDuration(1000);*/


        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(Login.this, MainMenu.class);
                startActivity(s);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }
}
