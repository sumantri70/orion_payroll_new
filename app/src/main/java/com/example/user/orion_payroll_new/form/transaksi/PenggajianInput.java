package com.example.user.orion_payroll_new.form.transaksi;

import android.database.DataSetObserver;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.user.orion_payroll_new.ModelsHelper.PenggajianDetailModel;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.adapter.ExpandListAadapterPenggajian;
import com.example.user.orion_payroll_new.utility.FormatNumber;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.example.user.orion_payroll_new.models.JCons.EDIT_MODE;

public class PenggajianInput extends AppCompatActivity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private ExpandableListView ListView;
    private ExpandListAadapterPenggajian ListAdapter;
    private List<String> ListDataHeader;
    private HashMap<String, List<String>> ListHash;
    private ArrayList<PenggajianDetailModel> ArListTunjangan;
    private ArrayList<PenggajianDetailModel> ArListPotongan;
    private ArrayList<PenggajianDetailModel> ArListKasbon;

    public Boolean EditMode;
    private String Mode;

    protected void CreateView(){
        ListView = (ExpandableListView)findViewById(R.id.ExpLv);

    }

    protected void InitClass(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Bundle extra = this.getIntent().getExtras();
        //this.Mode = extra.getString("MODE");
//        this.SelectedData = extra.getInt("POSITION");
//        this.TPegawai = ((OrionPayrollApplication)getApplicationContext()).TPegawai;

        ListDataHeader = new ArrayList<>();
        ListHash = new HashMap<>();
        ListDataHeader.add("TUNJANGAN");
        ListDataHeader.add("POTONGAN");
        ListDataHeader.add("KASBON");

        List<String> ArListTunjangan = new ArrayList<>();
        ArListTunjangan.add("Tunjangan 1");
        ArListTunjangan.add("Tunjangan 2");

        List<String> ArListPotongan  = new ArrayList<>();
        ArListPotongan.add("Potongan 1");
        ArListPotongan.add("Potongan 2");

        List<String> ArListKasbon    = new ArrayList<>();
        ArListKasbon.add("Kasbon 1");
        ArListKasbon.add("Kasbon 2");

        ListHash.put(ListDataHeader.get(0), ArListTunjangan);
        ListHash.put(ListDataHeader.get(1), ArListPotongan);
        ListHash.put(ListDataHeader.get(2), ArListKasbon);

        ListAdapter = new ExpandListAadapterPenggajian(this, ListDataHeader, ListHash);
        ListView.setAdapter(ListAdapter);

//        if (Mode.equals(EDIT_MODE)){
//            this.setTitle("Edit Pegawai");
//        }else if (Mode.equals(DETAIL_MODE)){
//            this.setTitle("Detail Pegawai");
//            //this.btnSimpan.setVisibility(View.INVISIBLE);
//        }else{
//            this.setTitle("Input Pegawai");
//        };

        //boolean Enabled = !Mode.equals(DETAIL_MODE);
//        this.txtNik.setEnabled(Enabled);
//        this.txtNama.setEnabled(Enabled);
//        this.txtTelpon1.setEnabled(Enabled);
//        this.txtTelpon2.setEnabled(Enabled);
//        this.txtEmail.setEnabled(Enabled);
//        this.txtGajiPokok.setEnabled(Enabled);
//        this.btnSimpan.setEnabled(Enabled);
//        this.txtTglLahir.setEnabled(Enabled);
//
//        txtGajiPokok.addTextChangedListener(new FormatNumber(txtGajiPokok));
//        txtNik.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); //untuk uppercase
    }

    protected void EventClass(){
    }

//    protected void RefreshDetail(int idx){
//        switch (idx){
//            case 0 :
//                ListHash.put(ListDataHeader.get(0), ArListTunjangan);
//                ListHash.put(ListDataHeader.get(1), ArListPotongan);
//                ListHash.put(ListDataHeader.get(2), ArListKasbon);
//            case 1 :
//                ListHash.put(ListDataHeader.get(0), ArListTunjangan);
//                break;
//            case 2 :
//                ListHash.put(ListDataHeader.get(1), ArListPotongan);
//                break;
//            case 3 :
//                ListHash.put(ListDataHeader.get(2), ArListKasbon);
//                break;
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penggajian_input);
        CreateView();
        InitClass();
        EventClass();

//        mViewPager = (ViewPager) findViewById(R.id.vp_tab);
//        mSlidingTabLayout = (SlidingTabLayout)  findViewById(R.id.stl_tabs);
//
//        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),this));
//        mSlidingTabLayout.setDistributeEvenly(true);
//        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorwhite));
//        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
//        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
