package com.example.user.orion_payroll_new;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.orion_payroll_new.form.master.PegawaiInput;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.models.PegawaiModel;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_SAVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_CONECT;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.getMillisDate;

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
                //GetHashPegawai();
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
