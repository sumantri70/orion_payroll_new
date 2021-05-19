package com.orionit.app.orion_payroll_new.models;

import android.app.Application;

public class JCons extends Application {
    public static String MSG_SUCCESS_SAVE      = "Data berhasil disimpan";
    public static String MSG_UNSUCCESS_SAVE    = "Data gagal disimpan";
    public static String MSG_SUCCESS_UPDATE    = "Data berhasil diubah";
    public static String MSG_UNSUCCESS_UPDATE  = "Data gagal diubah";
    public static String MSG_SUCCESS_DELETE    = "Data berhasil dihapus";
    public static String MSG_UNSUCCESS_DELETE  = "Data gagal dihapus";
    public static String MSG_SUCCESS_ACTIVE    = "Data berhasil diaktif / non aktifkan";
    public static String MSG_UNSUCCESS_ACTIVE  = "Data gagal diaktif / non aktifkan";

    public static String MSG_SAVE_CONFIRMATION = "Apakah data sudah benar?";
    public static String MSG_DELETE_CONFIRMATION = "Yakin akan menghapus transaksi?";
    public static String MSG_AKTIVASI_CONFIRMATION = "Apakah data akan diaktif/non aktifkan?";

    public static String MSG_CANT_AKTIVASI = "Tidak dapat dinonaktifkan karena diinput oleh system";
    public static String MSG_CANT_EDIT     = "Tidak dapat diedit karena diinput oleh system";


    public static String MSG_POSITIVE = "Ya";
    public static String MSG_NEGATIVE = "Tidak";


    public static String MSG_UNSUCCESS_CONECT  = "Terjadi kesalahan";

    public static String DETAIL_MODE  = "D";
    public static String EDIT_MODE    = "E";

    public static String TRUE_STRING  = "T";
    public static String FALSE_STRING = "F";

    //TIPE DETAIL PENGGAJIAN
    public static String TDP_TUNJANGAN  = "T";
    public static String TDP_POTONGAN   = "P";
    public static String TDP_KASBON     = "K";

    //RESULT
    public static int RESULT_LOV = 2;
    public static int RESULT_FILTER = 3;

    public static int RESULT_LOV_TUNJANGAN = 4;
    public static int RESULT_LOV_POTONGAN  = 5;
    public static int RESULT_LOV_PEGAWAI   = 6;
    public static int RESULT_LOV_KASBON    = 7;

    //TIPE DETAIL PENGGAJIAN
    public static String TIPE_DET_TUNJANGAN = "T";
    public static String TIPE_DET_POTONGAN  = "P";
    public static String TIPE_DET_KASBON    = "K";


    //-----ID MASTER TUNJANGAN-----\\
    public static int ID_TJ_INSENTIF = 1;
    public static int ID_TJ_LEMBUR   = 2;

    //-----ID MASTER POTONGAN-----\\
    public static int ID_PT_TELAT_15       = 1;
    public static int ID_PT_TELAT_LBH_15   = 2;
    public static int ID_PT_IZIN_STGH_HARI = 3;
    public static int ID_PT_IZIN_NON_CUTI  = 4;
    public static int ID_PT_DOKTER         = 5;

}
