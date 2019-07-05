package com.orionit.app.orion_payroll_new.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.form.lov.lov_tunjangan;
import com.orionit.app.orion_payroll_new.form.master.PegawaiInput;
import com.orionit.app.orion_payroll_new.models.TunjanganModel;
import com.orionit.app.orion_payroll_new.utility.FormatNumber;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import static com.orionit.app.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.ID_TJ_LEMBUR;
import static com.orionit.app.orion_payroll_new.models.JCons.MSG_SUCCESS_UPDATE;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;

import java.util.HashMap;
import java.util.List;

import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;

public class ExpandListAdapterPegawai extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListHeader;
    private HashMap<String, List<TunjanganModel>> ListHasMap;
    private Context ctx;


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
        final ImageButton btnTambah = (ImageButton)convertView.findViewById(R.id.btnTambah);

        if (((PegawaiInput)ctx).Mode.equals(DETAIL_MODE)){
            btnTambah.setVisibility(View.INVISIBLE);
        }

        lblGroup.setTypeface(null, Typeface.BOLD);
        lblGroup.setText(HeaderTitle);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            btnTambah.requestFocus();
            LostFocus();
            Intent s = new Intent(((PegawaiInput)ctx), lov_tunjangan.class);
            s.putExtra("JANGANMUNCULKANSYSTEM",false);
            s.putExtra("TANPA_ID",String.valueOf(ID_TJ_LEMBUR));
            ((PegawaiInput)ctx).startActivityForResult(s, RESULT_LOV);


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
            holder.HbtnHapus = (ImageButton)v.findViewById(R.id.btnAction);
            holder.Hjumlah.addTextChangedListener(new FormatNumber(holder.Hjumlah));

            boolean Enabled = !((PegawaiInput)ctx).Mode.equals(DETAIL_MODE);
            if (((PegawaiInput)ctx).Mode.equals(DETAIL_MODE)){
                holder.HbtnHapus.setVisibility(View.INVISIBLE);
            }
            holder.Hjumlah.setEnabled(Enabled);

            v.setTag(holder);
        }else{
            holder = (ViewHolder)v.getTag();
        }

        holder.Hkode.setText(DataChild.getKode().toString());
        holder.Hnama.setText(DataChild.getNama().toString());
        holder.Hjumlah.setText(fmt.format(DataChild.getJumlah()));
        holder.Hjumlah.setId(childPosition);

        holder.Hjumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final EditText Caption = (EditText) v;
                    if (!Caption.getText().toString().equals("")) {
                        DataChild.setJumlah(StrFmtToDouble(Caption.getText().toString()));
                    }else{
                        DataChild.setJumlah(StrFmtToDouble("0"));
                    }
                    holder.Hjumlah.setText( FungsiGeneral.FloatToStrFmt(DataChild.getJumlah()));
                    holder.Hjumlah.setText(fmt.format(DataChild.getJumlah()));
                }
            }
        });
        final int idx = childPosition;

        holder.HbtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             LostFocus();
            ((PegawaiInput)ctx).ArListTunjangan.remove(idx);
            ((PegawaiInput)ctx).ListAdapter.notifyDataSetChanged();
            ((PegawaiInput)ctx).SetAutoHeightListView();
            }
        });
        return v;

    }

    protected void LostFocus(){
        ((PegawaiInput)ctx).txtTmp.setVisibility(View.VISIBLE);
        ((PegawaiInput)ctx).txtTmp.requestFocus();
        ((PegawaiInput)ctx).txtTmp.setVisibility(View.INVISIBLE);
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

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        LostFocus();
        ((PegawaiInput)ctx).SetAutoHeightListView();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        LostFocus();
        ((PegawaiInput)ctx).SetAutoHeightListView();
    }

}
