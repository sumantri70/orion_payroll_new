package com.orionit.app.orion_payroll_new.form.transaksi;

/**
 * Created by Ujang Wahyu on 24/01/2017.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orionit.app.orion_payroll_new.R;

/**
 * Created by Ujang Wahyu on 18/08/2016.
 */

public class MyAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titles ={"TUNJANGAN","POTONGAN","KASBON"};
    int[] icon = new int[]{R.drawable.ic_add, R.drawable.ic_group_black_24dp, R.drawable.ic_menu_kasbon, R.drawable.ic_add};
    private int heightIcon;

    public MyAdapter(FragmentManager fm, Context c){
        super(fm);
        mContext = c;
        double scale = c.getResources().getDisplayMetrics().density;
        heightIcon=(int)(24*scale+0.5f);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag= null;

        if(position ==0){
            frag = new TabTunjanganPegawai();
        }else if(position == 1){
            frag = new TabPotonganPegawai();
        }else if(position == 2){
            frag = new TabKasbonPegawai();
        }

        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public CharSequence getPageTitle(int position){
        //Drawable d = mContext.getResources().getDrawable(icon[position]);
        //d.setBounds(0,0,heightIcon,heightIcon);
        //ImageSpan is = new ImageSpan(d);

        //SpannableString sp = new SpannableString(" ");
        //sp.setSpan(is,0,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //return sp;

        return titles[position];
    }

}