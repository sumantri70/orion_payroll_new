package com.example.user.orion_payroll_new.form.transaksi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.orion_payroll_new.R;

public class TabTunjanganPegawai extends Fragment {

    public TabTunjanganPegawai() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_tunjangan_pegawai, container, false);
    }
}
