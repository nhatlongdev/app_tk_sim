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
    public static final String URL_ADD_CARD_CHAN = APP_DOMAIN_CHAN + "addcard";
    public static final String URL_ADD_CARD_PHOM = APP_DOMAIN_PHOM + "addcard";
    public static final String URL_ADD_CARD_CO = APP_DOMAIN_CO + "addcard";

    /*push card*/
    public static final String URL_SENT_CARD = APP_DOMAIN + "/addcard";

    /*get card*/
    public static final String URL_GET_CARD = APP_DOMAIN + "/getcard";

    /*update card*/
    public static final String URL_UPDATE_CARD = APP_DOMAIN + "/updatecard";

    /*get count card*/
    public static final String URL_GET_COUNT_CARD = APP_DOMAIN + "/getcountcards";

    /*find card*/
    public static final String URL_FIND_CARD = APP_DOMAIN + "/findcard";

    /*get ok card*/
    public static final String URL_GET_OK_CARD = APP_DOMAIN + "/getokcard";

    /*update ok card*/
    public static final String URL_UPDATE_OK_CARD = APP_DOMAIN + "/updateokcard";

    /*send card to game*/
    public static final String URL_SEND_CARD_TO_GAME = APP_DOMAIN + "/postcardtogame";

    /*tra the*/
    public static final String URL_TRA_THE = APP_DOMAIN + "/resetcard";

    /*CHECK PASS*/
    public static final String URL_CHECK_PASS = APP_DOMAIN + "/checkpassword";

    /*UNLOCK*/
    public static final String URL_UNLOCK = APP_DOMAIN + "/unlocksim";
}
