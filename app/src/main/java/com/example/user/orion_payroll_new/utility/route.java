package com.example.user.orion_payroll_new.utility;

public class route {
    //public static final String URL = "http://nsumantry.000webhostapp.com/orion_payroll/";
    public static final String URL = "http://192.168.43.59/orion_payroll/";
    //public static final String URL = "http://10.146.168.145/orion_payroll/";
    //public static final String URL = "http://192.168.1.8/orion_payroll/";

    //*-- MASTER PEGAWAI --*\\
    public static final String URL_PEGAWAI = "master_pegawai/";
    public static final String URL_INSERT_PEGAWAI   = URL + URL_PEGAWAI + "insert.php";
    public static final String URL_SELECT_PEGAWAI   = URL + URL_PEGAWAI + "select.php";
    public static final String URL_GET_PEGAWAI      = URL + URL_PEGAWAI + "get.php";
    public static final String URL_UPDATE_PEGAWAI   = URL + URL_PEGAWAI + "update.php";
    public static final String URL_DELETE_PEGAWAI   = URL + URL_PEGAWAI + "delete.php";
    public static final String URL_AKTIVASI_PEGAWAI = URL + URL_PEGAWAI + "aktivasi.php";

    //*-- DETAIL TUNJANGAN PEGAWAI --*\\
    public static final String URL_DET_TUNJANGAN_PEGAWAI = "detail_tunjangan_pegawai/";
    public static final String URL_DET_TUNJANGAN_PEGAWAI_INSERT   = URL + URL_DET_TUNJANGAN_PEGAWAI + "insert.php";
    public static final String URL_DET_TUNJANGAN_PEGAWAI_SELECT_PEGAWAI   = URL + URL_DET_TUNJANGAN_PEGAWAI + "select.php";
    public static final String URL_DET_TUNJANGAN_PEGAWAI_GET_PEGAWAI      = URL + URL_DET_TUNJANGAN_PEGAWAI + "get.php";
    public static final String URL_DET_TUNJANGAN_PEGAWAI_UPDATE_PEGAWAI   = URL + URL_DET_TUNJANGAN_PEGAWAI + "update.php";
    public static final String URL_DET_TUNJANGAN_PEGAWAI_DELETE_PEGAWAI   = URL + URL_DET_TUNJANGAN_PEGAWAI + "delete.php";
    public static final String URL_DET_TUNJANGAN_PEGAWAI_AKTIVASI_PEGAWAI = URL + URL_DET_TUNJANGAN_PEGAWAI + "aktivasi.php";

    //*-- MASTER TUNJANGAN --*\\
    public static final String URL_TUNJANGAN = "master_tunjangan/";
    public static final String URL_INSERT_TUNJANGAN   = URL + URL_TUNJANGAN + "insert.php";
    public static final String URL_SELECT_TUNJANGAN   = URL + URL_TUNJANGAN + "select.php";
    public static final String URL_GET_TUNJANGAN      = URL + URL_TUNJANGAN + "get.php";
    public static final String URL_UPDATE_TUNJANGAN   = URL + URL_TUNJANGAN + "update.php";
    public static final String URL_DELETE_TUNJANGAN   = URL + URL_TUNJANGAN + "delete.php";
    public static final String URL_AKTIVASI_TUNJANGAN = URL + URL_TUNJANGAN + "aktivasi.php";

    //*-- MASTER POTONGAN --*\\
    public static final String URL_POTONGAN = "master_potongan/";
    public static final String URL_INSERT_POTONGAN   = URL + URL_POTONGAN + "insert.php";
    public static final String URL_SELECT_POTONGAN   = URL + URL_POTONGAN + "select.php";
    public static final String URL_GET_POTONGAN      = URL + URL_POTONGAN + "get.php";
    public static final String URL_UPDATE_POTONGAN   = URL + URL_POTONGAN + "update.php";
    public static final String URL_DELETE_POTONGAN   = URL + URL_POTONGAN + "delete.php";
    public static final String URL_AKTIVASI_POTONGAN = URL + URL_POTONGAN + "aktivasi.php";
}
