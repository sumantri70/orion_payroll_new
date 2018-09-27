package com.example.user.orion_payroll_new.form.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.form.lov.lov_tunjangan;
import com.example.user.orion_payroll_new.form.master.PegawaiInput;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FormatNumber;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;

import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;

import java.util.HashMap;
import java.util.List;

import static com.example.user.orion_payroll_new.models.JCons.RESULT_SEARCH_TUNJANGAN;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.FloatToStrFmt;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.example.user.orion_payroll_new.utility.FungsiGeneral.StrFmtToFloatInput;

public class ExpandListAdapterPegawai extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListHeader;
    private HashMap<String, List<TunjanganModel>> ListHasMap;
    private Dialog DialogFilter;
    private Context ctx;
    private EditText txtTmp;

    public ExpandListAdapterPegawai(Context context, List<String> listHeader, HashMap<String, List<TunjanganModel>> listHasMap) {
        this.context = context;
        ListHeader = listHeader;
        ListHasMap = listHasMap;
        this.ctx = context;
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
        this.txtTmp       = (EditText) convertView.findViewById(R.id.txtTmp);

        final ImageButton btnTambah = (ImageButton)convertView.findViewById(R.id.btnTambah);
        lblGroup.setTypeface(null, Typeface.BOLD);
        lblGroup.setText(HeaderTitle);
        txtTmp.setVisibility(View.INVISIBLE);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DialogFilter.show();
                btnTambah.requestFocus();
//                txtTmp.setVisibility(View.VISIBLE);
//                txtTmp.requestFocus();
//                txtTmp.setVisibility(View.INVISIBLE);
                ((PegawaiInput)ctx).txtTmp.setVisibility(View.VISIBLE);
                ((PegawaiInput)ctx).txtTmp.requestFocus();
                ((PegawaiInput)ctx).txtTmp.setVisibility(View.INVISIBLE);

                Intent s = new Intent(((PegawaiInput)ctx), lov_tunjangan.class);
                s.putExtra("MODE","");
                ((PegawaiInput)ctx).startActivityForResult(s, RESULT_SEARCH_TUNJANGAN);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TunjanganModel DataChild = (TunjanganModel) getChild(groupPosition, childPosition);
        final ViewHolder holder;
        View v = convertView;

        if (v == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_tunjangan_pegawai,null);


            holder.Hkode   = (TextView) v.findViewById(R.id.lblItem1);
            holder.Hnama   = (TextView) v.findViewById(R.id.lblItem2);
            holder.Hjumlah = (EditText) v.findViewById(R.id.txtJumlah);
            holder.HbtnHapus = (ImageButton)v.findViewById(R.id.btnHapus);
            v.setTag(holder);
        }else{
            holder = (ViewHolder)v.getTag();
        }

        holder.Hkode.setText(DataChild.getKode().toString());
        holder.Hnama.setText(DataChild.getNama().toString());
        holder.Hjumlah.setText(FloatToStrFmt(DataChild.getJumlah()));
        holder.Hjumlah.setId(childPosition);

        //holder.Hjumlah.setText(FungsiGeneral.FloatToStrFmt(ListHasMap.get(childPosition).get().get));


//        if (isDetail){
//            holder.jumlah.setFocusable(false);
//        }else{
//            holder.jumlah.setFocusable(true);
//        }

        holder.Hjumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;

                    if (!Caption.getText().toString().equals("")) {
                        DataChild.setJumlah(StrFmtToFloatInput(Caption.getText().toString()));
                    }else{
                        DataChild.setJumlah(Float.valueOf(0));
                    }
                    holder.Hjumlah.setText( FungsiGeneral.FloatToStrFmt(DataChild.getJumlah()));
                }else{


                }
            }
        });


//        if(convertView == null){
//            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_item_tunjangan_pegawai, null);
//        }
//        TextView lblItem = (TextView)convertView.findViewById(R.id.lblItem1);
//        TextView lblItem2 = (TextView)convertView.findViewById(R.id.lblItem2);
//        ImageButton btnHapus = (ImageButton)v.findViewById(R.id.btnHapus);
//        final EditText txtJumlah = (EditText) convertView.findViewById(R.id.txtJumlah);
//        txtJumlah.addTextChangedListener(new FormatNumber(txtJumlah));
//
//        lblItem.setText(DataChild.getKode().toString());
//        lblItem2.setText(DataChild.getNama().toString());
//        txtJumlah.setText(fmt.format(DataChild.getJumlah()));

        final int idx = childPosition;

        holder.HbtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PegawaiInput)ctx).ArListTunjangan.remove(idx);
                ((PegawaiInput)ctx).ListAdapter.notifyDataSetChanged();
            }
        });

//        txtJumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ((PegawaiInput)ctx).ArListTunjangan.get(idx).setJumlah(StrFmtToDouble(txtJumlah.getText().toString()));
//            }
//        });

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {
        private TextView Hkode;
        private TextView Hnama;
        public EditText Hjumlah;
        public ImageButton HbtnHapus;

    }
}
