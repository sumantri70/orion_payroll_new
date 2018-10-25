package com.example.user.orion_payroll_new.utility;

import com.example.user.orion_payroll_new.OrionPayrollApplication;

public class JEngine {
    public static String Get_Nik_Master_Pegawai(int Id){
        return OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(Id)).getNik();
    }

    public static String Get_Nama_Master_Pegawai(int Id){
        return OrionPayrollApplication.getInstance().ListHashPegawaiGlobal.get(Integer.toString(Id)).getNama();
    }

    public static String Get_Kode_Master_Tunjangan(int Id){
        return OrionPayrollApplication.getInstance().ListHashTunjanganGlobal.get(Integer.toString(Id)).getKode();
    }

    public static String Get_Nama_Master_Tunjangan(int Id){
        return OrionPayrollApplication.getInstance().ListHashTunjanganGlobal.get(Integer.toString(Id)).getNama();
    }

    public static String Get_Kode_Master_Potongan(int Id){
        return OrionPayrollApplication.getInstance().ListHashPotonganGlobal.get(Integer.toString(Id)).getKode();
    }

    public static String Get_Nama_Master_Potongan(int Id){
        return OrionPayrollApplication.getInstance().ListHashPotonganGlobal.get(Integer.toString(Id)).getNama();
    }
}
