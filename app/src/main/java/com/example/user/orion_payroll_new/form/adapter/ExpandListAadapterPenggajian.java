package com.example.user.orion_payroll_new.form.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.orion_payroll_new.ModelsHelper.PenggajianDetailModel;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.models.JCons;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.*;
import static com.example.user.orion_payroll_new.models.JCons.TDP_KASBON;
import static com.example.user.orion_payroll_new.models.JCons.TDP_POTONGAN;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.DoubleToStr;

public class ExpandListAadapterPenggajian extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListHeader;
    private HashMap<String, ArrayList<PenggajianDetailModel>> ListHasMap;
    private Dialog DialogFilter;


    public ExpandListAadapterPenggajian(Context context, List<String> listHeader, HashMap<String, ArrayList<PenggajianDetailModel>> listHasMap) {
        this.context = context;
        ListHeader = listHeader;
        ListHasMap = listHasMap;
    }

    @Override
    public int getGroupCount() {
        return ListHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ListHasMap.get(ListHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ListHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ListHasMap.get(ListHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String HeaderTitle = (String)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_penggajian, null);
        }

        TextView lblGroup = (TextView)convertView.findViewById(R.id.lblGroup);
        TextView lblTotal = (TextView)convertView.findViewById(R.id.lblTotal);
        ImageButton btnTambah = (ImageButton)convertView.findViewById(R.id.btnTambah);
        lblGroup.setTypeface(null, Typeface.BOLD);
        lblTotal.setTypeface(null, Typeface.BOLD);
        lblGroup.setText(HeaderTitle);
        lblTotal.setText("Jumlah : " + Integer.toString(getGroupCount()));


        this.DialogFilter = new Dialog(context);
        this.DialogFilter.setContentView(R.layout.activity_penggajian_pilih_tunjangan);
        this.DialogFilter.setTitle("Pilih");

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFilter.show();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView lblItem1 = (TextView)convertView.findViewById(R.id.lblItem1);
        TextView lblItem2 = (TextView)convertView.findViewById(R.id.lblItem2);
        TextView lblItem3 = (TextView)convertView.findViewById(R.id.lblItem3);
        TextView lblItem4 = (TextView)convertView.findViewById(R.id.lblItem4);
        final PenggajianDetailModel Data = (PenggajianDetailModel) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_penggajian, null);
        }

        String TipDetail = Data.getTipe_detail();
        if (TipDetail.equals(TDP_POTONGAN)){
            lblItem1.setText(Data.getKode());
            lblItem2.setText(DoubleToStr(Data.getJumlah()));
        }else if (TipDetail.equals(TDP_TUNJANGAN)){
            lblItem1.setText(Data.getKode());
            lblItem2.setText(DoubleToStr(Data.getJumlah()));
        }else if (TipDetail.equals(TDP_KASBON)){
            lblItem1.setText(Data.getKode());
            lblItem2.setText(DoubleToStr(Data.getJumlah()));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
