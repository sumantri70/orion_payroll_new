package com.orionit.app.orion_payroll_new.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.form.lov.lov_potongan;
import com.orionit.app.orion_payroll_new.form.lov.lov_tunjangan;
import com.orionit.app.orion_payroll_new.form.transaksi.PenggajianInputNew;
import com.orionit.app.orion_payroll_new.models.PenggajianDetailModel;
import com.orionit.app.orion_payroll_new.utility.FormatNumber;
import com.orionit.app.orion_payroll_new.utility.FungsiGeneral;

import java.util.HashMap;
import java.util.List;

import static com.orionit.app.orion_payroll_new.models.JCons.DETAIL_MODE;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV_POTONGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.RESULT_LOV_TUNJANGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_KASBON;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_POTONGAN;
import static com.orionit.app.orion_payroll_new.models.JCons.TIPE_DET_TUNJANGAN;
import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.StrFmtToDouble;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Kode_Master_Potongan;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Kode_Master_Tunjangan;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Potongan;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Tunjangan;

public class ExpandListAdapterPenggajianNew extends BaseExpandableListAdapter {
    private Context context;
    private List<String> ListHeader;
    private HashMap<String, List<PenggajianDetailModel>> ListHasMap;
    private Context ctx;

    public ExpandListAdapterPenggajianNew(Context context, List<String> listHeader, HashMap<String, List<PenggajianDetailModel>> listHasMap) {
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
            convertView = inflater.inflate(R.layout.list_group_detail_penggajian, null);
        }

        TextView lblGroup = (TextView)convertView.findViewById(R.id.lblGroup);
        final ImageButton btnTambah = (ImageButton)convertView.findViewById(R.id.btnTambah);

        if (((PenggajianInputNew)ctx).Mode.equals(DETAIL_MODE)){
            btnTambah.setVisibility(View.INVISIBLE);
        }

        lblGroup.setTypeface(null, Typeface.BOLD);
        lblGroup.setText(HeaderTitle);

        final int idx = groupPosition;

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTambah.requestFocus();
                LostFocus();
                Intent s;
                switch (idx){
                    case 0 :
                        s = new Intent(((PenggajianInputNew)ctx), lov_tunjangan.class);
                        ((PenggajianInputNew)ctx).startActivityForResult(s, RESULT_LOV_TUNJANGAN);
                        break;
                    case 1 :
                        s = new Intent(((PenggajianInputNew)ctx), lov_potongan.class);
                        ((PenggajianInputNew)ctx).startActivityForResult(s, RESULT_LOV_POTONGAN);
                        break;
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PenggajianDetailModel DataChild = (PenggajianDetailModel) getChild(groupPosition, childPosition);
        final ExpandListAdapterPenggajianNew.ViewHolder holder;
        View v = convertView;

        if (v == null) {
            holder = new ExpandListAdapterPenggajianNew.ViewHolder();
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_detail_penggajian,null);

            holder.Hkode      = (TextView) v.findViewById(R.id.lblItem1);
            holder.Hnama      = (TextView) v.findViewById(R.id.lblItem2);
            holder.Hjumlah    = (EditText) v.findViewById(R.id.txtJumlah);
            holder.HbtnHapus  = (ImageButton)v.findViewById(R.id.btnAction);
            holder.HchbKasbon = (CheckBox) v.findViewById(R.id.chbKasbon);
            holder.HlblSisa = (TextView) v.findViewById(R.id.lblSisa);
            holder.Hjumlah.addTextChangedListener(new FormatNumber(holder.Hjumlah));

            boolean Enabled = !((PenggajianInputNew)ctx).Mode.equals(DETAIL_MODE);

            if (((PenggajianInputNew)ctx).Mode.equals(DETAIL_MODE)){
                holder.HbtnHapus.setVisibility(View.INVISIBLE);
                holder.HchbKasbon.setVisibility(View.INVISIBLE);
            }else{

                if ((DataChild.getTipe().equals(TIPE_DET_TUNJANGAN)) || (DataChild.getTipe().equals(TIPE_DET_POTONGAN))){
                    holder.HbtnHapus.setVisibility(View.VISIBLE);
                    holder.HchbKasbon.setVisibility(View.INVISIBLE);
                    holder.HlblSisa.setVisibility(View.INVISIBLE);
                }else if (DataChild.getTipe().equals(TIPE_DET_KASBON)){
                    holder.HbtnHapus.setVisibility(View.INVISIBLE);
                    holder.HchbKasbon.setVisibility(View.VISIBLE);
                    holder.HlblSisa.setVisibility(View.VISIBLE);
                }
            }

            holder.Hjumlah.setEnabled(Enabled);
            v.setTag(holder);
        }else{
            holder = (ExpandListAdapterPenggajianNew.ViewHolder)v.getTag();
        }

        if (DataChild.getTipe().equals(TIPE_DET_TUNJANGAN)){
            holder.Hkode.setText(Get_Kode_Master_Tunjangan(DataChild.getId_tjg_pot_kas()));
            holder.Hnama.setText(Get_Nama_Master_Tunjangan(DataChild.getId_tjg_pot_kas()));
        }else if (DataChild.getTipe().equals(TIPE_DET_POTONGAN)) {
            holder.Hkode.setText(Get_Kode_Master_Potongan(DataChild.getId_tjg_pot_kas()));
            holder.Hnama.setText(Get_Nama_Master_Potongan(DataChild.getId_tjg_pot_kas()));
        }else if (DataChild.getTipe().equals(TIPE_DET_KASBON)) {
            if (((PenggajianInputNew)ctx).HashKasbon.size() > 0){
                holder.Hkode.setText(((PenggajianInputNew)ctx).HashKasbon.get(DataChild.getId_tjg_pot_kas()).getNomor());
                holder.Hnama.setText(FungsiGeneral.getTglFormat(((PenggajianInputNew)ctx).HashKasbon.get(DataChild.getId_tjg_pot_kas()).getTanggal()));
                holder.HlblSisa.setText(fmt.format(((PenggajianInputNew)ctx).HashKasbon.get(DataChild.getId_tjg_pot_kas()).getSisa()));
            }
        }

        holder.Hjumlah.setText(fmt.format(DataChild.getJumlah()));
        holder.Hjumlah.setId(childPosition);

        holder.HchbKasbon.setChecked(DataChild.isCheck());
        holder.HchbKasbon.setId(childPosition);

        holder.Hjumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final EditText Caption = (EditText) v;
                    if (!Caption.getText().toString().equals("")) {
                        DataChild.setJumlah(StrFmtToDouble(Caption.getText().toString()));
                    }else{
                        DataChild.setJumlah(StrFmtToDouble("0"));
                    }
                    holder.Hjumlah.setText(FungsiGeneral.FloatToStrFmt(DataChild.getJumlah()));
                    holder.Hjumlah.setText(fmt.format(DataChild.getJumlah()));
                }
            }
        });

        holder.HchbKasbon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                DataChild.setCheck(isChecked);
                holder.HchbKasbon.setChecked(isChecked);
            }
        });


        final int idx = childPosition;

        holder.HbtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostFocus();
                ((PenggajianInputNew)ctx).ArListTunjangan.remove(idx);
                ((PenggajianInputNew)ctx).ListAdapter.notifyDataSetChanged();
                ((PenggajianInputNew)ctx).ListView.getLayoutParams().height = 200 * ((PenggajianInputNew)ctx).ArListTunjangan.size() + ((PenggajianInputNew)ctx).ArListPotongan.size() + ((PenggajianInputNew)ctx).ArListKasbon.size() + 450;
            }
        });
        return v;
    }

    protected void LostFocus(){
        ((PenggajianInputNew)ctx).txtTmp.setVisibility(View.VISIBLE);
        ((PenggajianInputNew)ctx).txtTmp.requestFocus();
        ((PenggajianInputNew)ctx).txtTmp.setVisibility(View.INVISIBLE);
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
        public CheckBox HchbKasbon;
        public TextView HlblSisa;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        LostFocus();
        ((PenggajianInputNew)ctx).ListView.getLayoutParams().height = 450;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        LostFocus();
        ((PenggajianInputNew)ctx).ListView.getLayoutParams().height = 200 * ((PenggajianInputNew)ctx).ArListTunjangan.size() + ((PenggajianInputNew)ctx).ArListPotongan.size() + ((PenggajianInputNew)ctx).ArListKasbon.size() + 450;
    }

}
