package com.example.computer.thongkesimcard.configs;

/**
 * Created by Trang on 6/14/2016.
 */
public class Constant {

    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String LOGIN_TOKEN = "login_token";
    public static final String CALL_HISTORY_RC = "CALL_HISTORY_RC";
    public static final String CALL_HISTORY_TABLE = "CALL_HISTORY_TABLE";
    public static final String CALL_HISTORY_TYPE_SHOP = "CALL_HISTORY_TYPE_SHOP";
    public static final String CALL_HISTORY_PERMISSION = "CALL_HISTORY_PERMISSION";
    public static final String CALL_HISTORY_PERMISSION_ORDER = "CALL_HISTORY_PERMISSION_ORDER";

    public static final String PREF_KEY_ID = "PREF_KEY_ID";
    public static final String STATUS_ON_PRINTER = "STATUS_ON_PRINTER";
    public static final String STATUS_OFF_PRINTER = "STATUS_OFF_PRINTER";

    public static final String KEY_TOTAL_PAGE = "total_page";

    public static final String KEY_CREATE_SHOPA ="check_size_shop";
    public static final String RESULT_CREATE_SHOP ="createShop";
    public static final String RESULT_ONE_SHOP ="oneChooseShop";
    public static final String RESULT_LIST_SHOP ="listShop";
    public static final String IMAGE_DISH_DOWNLOAD = "imageDish";
    public static final String IMAGE_INACTIVE_CATEGORY_DOWNLOAD = "imageInactiveCategory";
    public static final String IMAGE_ACTIVE_CATEGORY_DOWNLOAD = "imageActiveCategory";

    public class Caching{
        public static final String KEY_REQUEST = "request";
        public static final String KEY_RESPONSE = "response";
        public static final String KEY_TIME_UPDATED = "time_updated";
        public static final String CACHING_PARAMS_TIME_REQUEST = "caching_time_request";

    }

    public static final String UPDATE_STATUS_PRINTER = "update_status_printer";
    public static final String UPDATE_STATUS_BUTTON_PRINTER = "update_status_button_printer";
    public static final String UPDATE_STATUS_BLUETOOTH = "update_status_bluetooth";
    public static final String UPDATE_STATUS_BLUETOOTH_TO_SERVER = "update_status_bluetooth_to_server";
    public static final String UPDATE_STATUS_PRINTER_PRESS_CONNECT = "update_status_printer_press_connect";

    public static final String REFRESH_ACTIVITY_CHILDREN = "refresh_activity_order";
    public static final String REFRESH_ACTIVITY_MAIN = "refresh_activity_main";
    public static final String REFRESH_SHOP_MANAGER = "refresh_shop_manager";
    public static final String REFRESH_LIST_SHOP = "refresh_list_shop";
    public static final String REFRESH_ACTIVITY_MANAGER_TABLE = "refresh_activity_manager_table";
    public static final String MSG_REFRESH_ACTIVITY_ORDER = "msg_refresh_activity_order";
    public static final String CLOSE_ACTIVITY_MANAGER_TABLE = "close_activity_manager_table";

    public static final String CLOSE_ACTIVITY = "close_activity";
    public static final String CLOSE_SHOP_MANAGER = "close_shop_manager";
    public static final String UPDATE_NAME_SHOP = "update_name_shop";
    public static final String REFRESH_ACTIVITY_TO_SETTING = "refresh_activity_to_setting";
    public static final String RECONNECT_SOCKET = "reconnect_socket";

    public static final String MSG_PRINT_OK = "msg_print_ok";

    public static final String MSG_OPEN_PRINTER_LAN = "msg_open_printer_lan";

    public static final String RETRY_CONNECT_SOCKET = "retry_connect_socket";
    public static final String MSG_TO_CONNECT_SPLASH = "msg_to_connect_splash";
    public static final String MSG_TO_RECONNECT_SOCKET = "msg_to_connect_in_out";

    public static final String MSG_REFRESH_ACTIVITY_WHEN_RECONNECT_SOCKET = "msg_refresh_activity_when_reconnect_socket";

    /*request print*/
    public static final String MSG_REQUEST_PRINTER = "MSG_REQUEST_PRINTER";

    /*on language*/
    public static final String MSG_SETTING_LANGUAGE = "msg_setting_language";

    public static final String MSG_JOIN_SUCCESS = "msg_join_success";

    public static final int TIME_POST_DELAY = 20000;

    /*list model shop*/
    public static final String FULL_BAR_NORMAL = "FULL_BAR_NORMAL";
    public static final String FULL_BAR_NO_ACTION = "FULL_BAR_NO_ACTION";
    public static final String NO_BAR_HAS_SERVED = "NO_BAR_HAS_SERVED";
    public static final String NO_BAR_NO_SERVED = "NO_BAR_NO_SERVED";

    /*ticket style print*/
    public static final String BILL_ON_ORDER = "BILL_ON_ORDER";
    public static final String TICKET_FOR_ORDER = "TICKET_FOR_ORDER";

    /*type report*/
    public static final String TYPE_REPORT_SALE = "sales_report_by_item";
    public static final String TYPE_REPORT_REVENUE = "sales_report_by_day";
    public static final String TYPE_REPORT_TIME_SERVED= "time_served_report";

    /*language*/
    public static final String TYPE_VI = "vi_VN";
    public static final String TYPE_EN = "en_US";

    public static final String TYPE_EN_SAVE = "en";
    public static final String TYPE_VI_SAVE = "vi";

    /*event msg box*/
    public static final String EVENT_INVITE_TO_SHOP = "INVITE_TO_SHOP";
    public static final String EVENT_LEAVE_FROM_SHOP = "LEAVE_FROM_SHOP";
    public static final String REJECT_FROM_SHOP = "REJECT_FROM_SHOP";

    /*value option report*/
    public static final String TODAY = "today";
    public static final String YESTERDAY = "yesterday";
    public static final String THIS_WEEK = "this week";
    public static final String LAST_WEEK = "last week";
    public static final String THIS_MONTH = "this month";
    public static final String LAST_MONTH = "last month";
    public static final String CHOOSE_DATE = "choose date";

    /*DS GIÁ TRỊ ĐỂ KIỂM TRA KHI ON BROADCASD CHO ACTIVITY*/
    public static final String ADD_PRINTER_ACTIVTY = "ADD_PRINTER_ACTIVTY";
    public static final String BAR_LIST_ACTIVTY = "BAR_LIST_ACTIVTY";
    public static final String CONFIG_DISH_ACTIVTY = "CONFIG_DISH_ACTIVTY";
    public static final String DETAIL_ORDER_NEW_ACTIVITY = "DETAIL_ORDER_NEW_ACTIVITY";
    public static final String INVITE_EMPLOYEE_ACTIVITY = "INVITE_EMPLOYEE_ACTIVITY";
    public static final String LANGUAGE_ACTIVITY = "LANGUAGE_ACTIVITY";
    public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
    public static final String MANAGER_BARS_ACTIVITY = "MANAGER_BARS_ACTIVITY";
    public static final String MANAGER_DEVICE_PRINTER_ACTIVITY = "MANAGER_DEVICE_PRINTER_ACTIVITY";
    public static final String MANAGER_TABLE_ACTIVITY = "MANAGER_TABLE_ACTIVITY";
    public static final String ONE_SHOP_ACTIVITY = "ONE_SHOP_ACTIVITY";
    public static final String ORDER_ACTIVITY = "ORDER_ACTIVITY";
    public static final String REPORT_REVENUE_ACTIVITY = "REPORT_REVENUE_ACTIVITY";
    public static final String SAMPLE_PRINT_ACTIVITY = "SAMPLE_PRINT_ACTIVITY";
    public static final String SAMPLE_PRINT_BILL_ACTIVITY = "SAMPLE_PRINT_BILL_ACTIVITY";
    public static final String SETTING_ACTIVITY = "SETTING_ACTIVITY";
    public static final String SETTING_HOME_ACTIVITY = "SETTING_HOME_ACTIVITY";
    public static final String SETTING_INFORMATION_SHOP = "SETTING_INFORMATION_SHOP";
    public static final String SETTING_MODEL_SHOP_ACTIVITY = "SETTING_MODEL_SHOP_ACTIVITY";
    public static final String SHOP_MANAGER_ACTIVITY = "SHOP_MANAGER_ACTIVITY";
    public static final String UPDATE_INFO_SAMPLE_ACTIVITY = "UPDATE_INFO_SAMPLE_ACTIVITY";

    public static final String ACTIVITY_GLOBAL = "ACTIVITY_GLOBAL";

    /*=========================NAP THE====================*/
    public static final String PARAM_THE_CHUA_NAP = "CHUA+GIU";

    /*LUU CACHE FILE*/
    public static final String NAME_FILE_LIST_SIM = "danhSachSim.text";
    public static final String NAME_FILE_LOG_APP = "log_app_card.text";

}

