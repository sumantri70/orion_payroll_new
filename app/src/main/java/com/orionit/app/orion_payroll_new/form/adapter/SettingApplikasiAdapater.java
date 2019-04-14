package com.orionit.app.orion_payroll_new.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orionit.app.orion_payroll_new.R;
import com.orionit.app.orion_payroll_new.form.setting.GantiPassword;
import com.orionit.app.orion_payroll_new.form.setting.SettingDatabase;
import com.orionit.app.orion_payroll_new.form.setting.SettingEmail;

import java.util.List;

public class SettingApplikasiAdapater extends RecyclerView.Adapter{
    private Context ctx;
    private List<String> objects;


    public SettingApplikasiAdapater(Context context, List<String> object) {
        this.ctx = context;
        this.objects = object;
    }


    public void addModels(List<String> Datas) {
        int pos = this.objects.size();
        this.objects.addAll(Datas);
        notifyItemRangeInserted(pos, Datas.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.item_setting, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String Data = objects.get(position);
        final ItemHolder itemHolder = (ItemHolder) holder;

        itemHolder.lblNama.setText(Data);

        if (Data.equals("Database")){
            itemHolder.imgIcon.setImageResource(R.drawable.ic_dns_black_24dp);
        }else if (Data.equals("Ganti Password")){
            itemHolder.imgIcon.setImageResource(R.drawable.ic_https_black_24dp);
        }else if (Data.equals("Email Pengirim")){
            itemHolder.imgIcon.setImageResource(R.drawable.ic_mail_black_24dp);
        }

        itemHolder.itemSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Data.equals("Database")){
                    Intent s = new Intent(ctx, SettingDatabase.class);
                    ctx.startActivity(s);
                }else if (Data.equals("Ganti Password")){
                    Intent s = new Intent(ctx, GantiPassword.class);
                    ctx.startActivity(s);
                }else if (Data.equals("Email Pengirim")){
                    Intent s = new Intent(ctx, SettingEmail.class);
                    ctx.startActivity(s);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private CardView itemSetting;
        private ImageView imgIcon;
        private TextView lblNama;

        public ItemHolder(View itemView) {
            super(itemView);
            itemSetting = itemView.findViewById(R.id.itemSetting);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            lblNama = itemView.findViewById(R.id.lblNama);
        }
    }

}