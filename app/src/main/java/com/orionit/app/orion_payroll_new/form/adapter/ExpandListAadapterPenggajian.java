package com.orionit.app.orion_payroll_new.form.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;

import java.util.HashMap;
import java.util.List;

public class ExpandListAadapterPenggajian extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListHeader;
    private HashMap<String, List<String>> ListHasMap;
    private Dialog DialogFilter;

    public ExpandListAadapterPenggajian(Context context, List<String> listHeader, HashMap<String, List<String>> listHasMap) {
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
            convertView = inflater.inflate(R.layout.list_group_tunjangan_pegawai, null);
        }

        TextView lblGroup = (TextView)convertView.findViewById(R.id.lblGroup);
        ImageButton btnTambah = (ImageButton)convertView.findViewById(R.id.btnTambah);
        lblGroup.setTypeface(null, Typeface.BOLD);
        lblGroup.setText(HeaderTitle);


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
        final String ChildText = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_tunjangan_pegawai, null);
        }
        TextView lblItem = (TextView)convertView.findViewById(R.id.lblItem1);
        TextView lblItem2 = (TextView)convertView.findViewById(R.id.lblItem2);

        lblItem.setText(ChildText);
        lblItem2.setText(ChildText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
