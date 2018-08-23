package com.example.computer.thongkesimcard.configs;

/**
 * Created by TrangPV
 *
 */
public class Apis {

    /*IP*/
    public static String SERVER_IP = "35.185.129.233";
    public static String SERVER_PORT = ":8860";


    public static String APP_DOMAIN = "http://" + SERVER_IP + SERVER_PORT;

    // Domain
    public static String APP_DOMAIN_CHAN = "http://chanleotom.com/api/paymentcard/";
    public static String APP_DOMAIN_PHOM = "http://phomtuoi.com/api/paymentcard/";
    public static String APP_DOMAIN_CO = "http://cotuongsoai.com/api/paymentcard/";

    // Urls
    /*get infor sum */
    public static final String URL_GET_INFOR_SUM = APP_DOMAIN + "/gettopupsum";

    /*update tk for sim*/
    public static final String URL_UPDATE_TK_SIM = APP_DOMAIN + "/updateSimCost";

    /*filter sim*/
    public static final String URL_FILTER_SIM = APP_DOMAIN + "/filtersim";

}
