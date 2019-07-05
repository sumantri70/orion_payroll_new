package com.orionit.app.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orionit.app.orion_payroll_new.OrionPayrollApplication;
import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.models.PenggajianModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orionit.app.orion_payroll_new.utility.FormatNumber.fmt;
import static com.orionit.app.orion_payroll_new.utility.FungsiGeneral.getTglFormat;
import static com.orionit.app.orion_payroll_new.utility.JEngine.Get_Nama_Master_Pegawai;

public class PiliKirimEmailAdapterNew extends RecyclerView.Adapter {
    Context context;
    List<PenggajianModel> PenggajianModels;
    private ProgressDialog Loading;

    public PiliKirimEmailAdapterNew(Context context, List<PenggajianModel> PenggajianModels) {
        this.context = context;
        this.PenggajianModels = PenggajianModels;
        this.Loading = new ProgressDialog(context);
    }

    public void addModels(List<PenggajianModel> PenggajianModels) {
        int pos = this.PenggajianModels.size();
        this.PenggajianModels.addAll(PenggajianModels);
        notifyItemRangeInserted(pos, PenggajianModels.size());
    }

    public void addMoel(PenggajianModel PenggajianModel) {
        this.PenggajianModels.add(PenggajianModel);
        notifyItemRangeInserted(PenggajianModels.size()-1,PenggajianModels.size()-1);
    }

    public void removeMoel(int idx) {
        if (PenggajianModels.size() > 0){
            this.PenggajianModels.remove(PenggajianModels.size()-1);
            notifyItemRemoved(PenggajianModels.size());
        }
    }

    public void removeAllModel(){
        int LastPosition = PenggajianModels.size();
        this.PenggajianModels.removeAll(PenggajianModels);
        notifyItemRangeRemoved(0, LastPosition);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.list_item_kirim_email, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PenggajianModel mCurrentItem = PenggajianModels.get(position);
        final ItemHolder itemHolder = (ItemHolder) holder;

        itemHolder.chbPilih.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                mCurrentItem.setPilih(isChecked);
                PenggajianModels.get(position).setPilih(isChecked);
                itemHolder.chbPilih.setChecked(isChecked);
            }
        });

        itemHolder.lblNomor.setText(mCurrentItem.getNomor());
        itemHolder.lblTanggal.setText(getTglFormat(mCurrentItem.getTanggal()));
        itemHolder.lblJumlah.setText(fmt.format(mCurrentItem.getTotal()));
        itemHolder.Hvisible = mCurrentItem.getUser_id();
        itemHolder.chbPilih.setChecked(mCurrentItem.isPilih());
        itemHolder.chbPilih.setVisibility(View.VISIBLE);

        if (mCurrentItem.getUser_id().equals("HIDE")){
            itemHolder.lblTanggal.setText("");
            itemHolder.lblJumlah.setText("");
            itemHolder.lblPegawai.setText("");
            itemHolder.chbPilih.setVisibility(View.GONE);
        }


        if (mCurrentItem.getId_pegawai() > 0) {
            itemHolder.lblPegawai.setText(Get_Nama_Master_Pegawai(mCurrentItem.getId_pegawai()));
        }
    }

    @Override
    public int getItemCount() {
        return PenggajianModels.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView lblNomor, lblTanggal, lblPegawai, lblJumlah;
        CheckBox chbPilih;
        String Hvisible;

        public ItemHolder(View itemView) {
            super(itemView);
            lblNomor = itemView.findViewById(R.id.lblNomor);
            lblTanggal = itemView.findViewById(R.id.lblTanggal);
            lblPegawai = itemView.findViewById(R.id.lblPegawai);
            lblJumlah = itemView.findViewById(R.id.lblJumlah);
            chbPilih = itemView.findViewById(R.id.chbPilih);
            Hvisible = "";
        }
    }

}
