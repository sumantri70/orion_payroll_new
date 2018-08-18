package com.example.user.orion_payroll_new.form.transaksi;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.orion_payroll_new.R;

public class PenggajianInput extends AppCompatActivity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penggajian_input);

        mViewPager = (ViewPager) findViewById(R.id.vp_tab);
        mSlidingTabLayout = (SlidingTabLayout)  findViewById(R.id.stl_tabs);

        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),this));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorwhite));
        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
