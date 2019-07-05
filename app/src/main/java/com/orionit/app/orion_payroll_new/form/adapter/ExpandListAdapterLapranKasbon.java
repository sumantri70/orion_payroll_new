package com.orionit.app.orion_payroll_new.form.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModelHeader;
import com.orionit.app.orion_payroll_new.models.LaporanKasbonModelChild;

import java.util.HashMap;
import java.util.List;

import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class ExpandListAdapterLapranKasbon extends BaseExpandableListAdapter {
    private Context context;
    private List<LaporanKasbonModelHeader> ListHeader;
    private HashMap<LaporanKasbonModelHeader, List<LaporanKasbonModelChild>> ListHasMap;
    private Context ctx;

    public ExpandListAdapterLapranKasbon(Context context, List<LaporanKasbonModelHeader> listHeader, HashMap<LaporanKasbonModelHeader, List<LaporanKasbonModelChild>> listHasMap) {
        this.context = context;
        this.ListHeader = listHeader;
        this.ListHasMap = listHasMap;
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
        LaporanKasbonModelHeader DataHeader = (LaporanKasbonModelHeader)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_laporan_kasbon_header, null);
        }

        TextView txtNama       = (TextView)convertView.findViewById(R.id.txtNomor);
        TextView txtSaldoAwal  = (TextView)convertView.findViewById(R.id.txtSaldoAwal);
        TextView txtSaldoAkhir = (TextView)convertView.findViewById(R.id.txtSaldoAkhir);
        txtNama.setTypeface(null, Typeface.BOLD);

        txtNama.setText(Get_Nama_Master_Pegawai(DataHeader.getId_pegawai()));
        txtSaldoAwal.setText(fmt.format(DataHeader.getSaldo_awal()));
        txtSaldoAkhir.setText(fmt.format(DataHeader.getSaldo_akhir()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final LaporanKasbonModelChild DataChild = (LaporanKasbonModelChild) getChild(groupPosition, childPosition);
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_laporan_kasbon, null);
        }

        TextView txtNomor   = (TextView)v.findViewById(R.id.txtNomor);
        TextView txtTanggal = (TextView)v.findViewById(R.id.txtTanggal);
        TextView txtTipe    = (TextView)v.findViewById(R.id.txtTipe);
        TextView txtJumlah  = (TextView)v.findViewById(R.id.txtJumlah);

        txtNomor.setText(DataChild.getNomor());
        txtTanggal.setText(getTglFormat(DataChild.getTanggal()));
        txtTipe.setText(DataChild.getTipe());
        txtJumlah.setText(fmt.format(DataChild.getJumlah()));

        if (DataChild.getTipe().equals("Bayar")){
            txtTipe.setTextColor(ctx.getResources().getColor(R.color.colorSoftGrean));
        }else if (DataChild.getTipe().equals("Pinjam")){
            txtTipe.setTextColor(ctx.getResources().getColor(R.color.colorSoftRed));
        }
        return v;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

}
