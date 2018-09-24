package com.example.user.orion_payroll_new.form.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.orion_payroll_new.OrionPayrollApplication;
import com.example.user.orion_payroll_new.R;
import com.example.user.orion_payroll_new.database.master.TunjanganTable;
import com.example.user.orion_payroll_new.form.master.PegawaiRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganRekap;
import com.example.user.orion_payroll_new.form.master.TunjanganInput;
import com.example.user.orion_payroll_new.models.JCons;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.models.TunjanganModel;
import com.example.user.orion_payroll_new.utility.FungsiGeneral;
import com.example.user.orion_payroll_new.utility.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.user.orion_payroll_new.models.JCons.FALSE_STRING;
import static com.example.user.orion_payroll_new.models.JCons.MSG_SUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.MSG_UNSUCCESS_ACTIVE;
import static com.example.user.orion_payroll_new.models.JCons.TRUE_STRING;
import static com.example.user.orion_payroll_new.utility.FormatNumber.fmt;
import static com.example.user.orion_payroll_new.utility.route.URL_AKTIVASI_TUNJANGAN;
import static com.example.user.orion_payroll_new.utility.route.URL_INSERT_TUNJANGAN;

public class TunjanganAdapter extends ArrayAdapter<TunjanganModel> {
    private ProgressDialog Loading;
    private Context ctx;

    public TunjanganAdapter(Context context, int resource, List<TunjanganModel> object) {
        super(context, resource, object);
        this.ctx = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.list_tunjangan_rekap, null);
        final TunjanganModel Data = getItem(position);

        TextView lblNik  = (TextView) v.findViewById(R.id.lblKode);
        TextView lblNama = (TextView) v.findViewById(R.id.lblNama);

        final ImageButton btnAction = (ImageButton) v.findViewById(R.id.btnAction);

        if (Data.getStatus() == "HIDE"){
            btnAction.setVisibility(View.GONE);
        }
        final int IdMSt = Data.getId();
        lblNik.setText(Data.getKode());
        lblNama.setText(Data.getNama());

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu po = new PopupMenu(getContext(), btnAction);
                po.getMenuInflater().inflate(R.menu.menu_action_master, po.getMenu());
                po.getMenu().getItem(1).setEnabled(Data.getStatus().equals(TRUE_STRING));
                po.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (IdMSt > 0){
                            if (item.getTitle().equals("Detail")){
                                Intent s = new Intent(getContext(), TunjanganInput.class);
                                s.putExtra("MODE", JCons.DETAIL_MODE);
                                s.putExtra("ID",IdMSt);
                                getContext().startActivity(s);
                            } else if (item.getTitle().equals("Edit")){
                                Intent s = new Intent(getContext(), TunjanganInput.class);
                                s.putExtra("MODE", JCons.EDIT_MODE);
                                s.putExtra("ID",IdMSt);
                                ((TunjanganRekap)ctx).startActivityForResult(s, 1);
                            } else if (item.getTitle().equals("Aktivasi")){
                                StringRequest strReq = new StringRequest(Request.Method.POST, URL_AKTIVASI_TUNJANGAN, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            ((TunjanganRekap)ctx).LoadData();
                                            Toast.makeText(getContext(),MSG_SUCCESS_ACTIVE, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(),MSG_UNSUCCESS_ACTIVE, Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", String.valueOf(IdMSt));
                                        return params;
                                    }
                                };
                                OrionPayrollApplication.getInstance().addToRequestQueue(strReq, FungsiGeneral.tag_json_obj);
                            }
                        }
                        return false;
                    }
                });
                po.show();
            }
        });
        return v;
    }

}
