package gmc.com.getmycab.Utils;

import java.util.ArrayList;
import java.util.List;

import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.bean.City;
import gmc.com.getmycab.bean.State;
import gmc.com.getmycab.bean.UserDetails;

/**
 * Created by Baba on 9/5/2015.
 */
public class AppConstants {
    public static List<State> stateList;
    public static List<City> oneWaySource;
    public static List<City> roundTripSource;
    public static List<City> multiCitySource;


    public static final String PREF_KEY_USER_DETAILS_JSON = "user_details_json";
    public static final String PREF_KEY_REMEMBER_ME = "remember_me";
    public static final String PREF_KEY_USER_EMAIL_ID = "user_email_id";
    public static final String PREF_KEY_USER_PASSWORD = "user_password";
    public static final String PREF_KEY_CUSTOMER_ID = "customer_id";
    public static final String PREF_KEY_USER_NAME = "user_name";
    public static final String PREF_KEY_USER_PHONE = "user_phone";
    public static final String PREF_KEY_USER_ADDRESS = "user_address";
    public static final String PREF_KEY_USER_CITY = "user_city";
    public static final String PREF_KEY_USER_STATE = "user_state";
    public static final String PREF_KEY_LATITUDE = "lat";
    public static final String PREF_KEY_LONGITUDE = "longitude";
    public static final String PREF_KEY_CURRENT_ADDRESS = "current_address";
    public static final String PREF_KEY_IS_LOGEDIN = "is_logedin";

    public static final String CAB_SEARCH_RESULT_JSON = "cab_search_result";

    public static final String CAB_SEARCH_DATE = "cab_search_date";
    public static final String CAB_SEARCH_TIME = "cab_search_time";
    public static final String CAB_SEARCH_DURATION = "cab_search_duration";
    public static final String CAB_SEARCH_FROM_TO = "cab_search_from_to";

    public static final String BOOKING_QUERY_JSON="booking_query";
    public static final String BOOKING_AC_TYPE="ac_type";
    public static final String BOOKING_PICKUP_ADDRESS="pickup_address";
    public static final String BOOKING_AMOUNT="booking_amt";
    public static final String temp="{" +
            "    \"Result\": true," +
            "    \"msg\": \"Payment History\"," +
            "    \"data\": [" +
            "        {" +
            "            \"order_id\": \"OODEL0220818\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"30/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0220818\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0220818\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0220811\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"30/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0220811\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0220811\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0220804\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"30/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0220804\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0220804\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0220797\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"30/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0220797\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0220797\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0220790\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"30/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0220790\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0220790\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0220314\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"Cancelled\"," +
            "            \"order_status_id\": \"7\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"28/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0220314\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0220314\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0219922\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"27/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0219922\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0219922\"" +
            "        }," +
            "        {" +
            "            \"order_id\": \"OODEL0219915\"," +
            "            \"name\": \"ghh \"," +
            "            \"status\": \"FakeOrder\"," +
            "            \"order_status_id\": \"15\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"paid_advance_payment\": \"0\"," +
            "            \"advance_payable\": \"560\"," +
            "            \"date_added\": \"27/09/2015\"," +
            "            \"products\": \"0\"," +
            "            \"total\": \"Rs. 0\"," +
            "            \"href\": \"http://www.getmecab.com/index.php?route=account/invoice&order_id=OODEL0219915\"," +
            "            \"payment_href\": \"http://www.getmecab.com/index.php?route=payment/gateway&order_id=OODEL0219915\"" +
            "        }" +
            "    ]" +
            "}";

    public static final String detisl="{" +
            "    \"Result\": true," +
            "    \"msg\": \"Payment History Details\"," +
            "    \"data\": [" +
            "        {" +
            "            \"order_id\": \"ORDEL0220538\"," +
            "            \"location_pair_id\": \"167\"," +
            "            \"car_inventory_id\": \"3281\"," +
            "            \"booking_start_time\": \"2015-09-30 11:00:00\"," +
            "            \"booking_end_time\": \"2015-09-30 23:59:00\"," +
            "            \"driver_details_id\": \"0\"," +
            "            \"book_type\": \"OnRequest\"," +
            "            \"car_model\": \"Toyota Etios/Swift Dzire\"," +
            "            \"operator\": \"Get Me Cab Delhi, Delhi\"," +
            "            \"price\": \"4650\"," +
            "            \"total\": \"4650\"," +
            "            \"tax\": \"0\"," +
            "            \"fare\": \"10\"," +
            "            \"distance\": \"440\"," +
            "            \"distance_type\": \"G2G\"," +
            "            \"tour_type\": \"Outstation\"," +
            "            \"fareperkm\": \"0\"," +
            "            \"fareperhr\": \"0\"," +
            "            \"airport_price_type\": \"\"," +
            "            \"airport_travel_type\": \"\"," +
            "            \"order_type\": \"Online\"," +
            "            \"commission\": \"0\"," +
            "            \"state_tax\": \"150\"," +
            "            \"service_tax\": \"260\"," +
            "            \"driver_charges\": \"250\"," +
            "            \"src_location\": \"Delhi\"," +
            "            \"dest_location\": \"Agra\"," +
            "            \"paid_advance_payment\": \"1\"," +
            "            \"final_price\": \"0\"," +
            "            \"final_km\": \"0\"," +
            "            \"final_fare\": \"0\"," +
            "            \"ac_type\": \"1\"," +
            "            \"driver_name\": \"\"," +
            "            \"driver_mobile_num\": \"\"," +
            "            \"cab_register_num\": \"\"," +
            "            \"driver_payment\": \"0\"," +
            "            \"operator_final_km\": \"0\"," +
            "            \"advance_payable\": \"930\"," +
            "            \"outstation_travel_type\": \"Roundtrip\"," +
            "            \"tour_duration\": \"1\"," +
            "            \"min_km\": \"200\"," +
            "            \"night_charges\": \"0\"," +
            "            \"mc_list\": \"\"," +
            "            \"garage_distance\": \"0\"," +
            "            \"area_selcted\": null," +
            "            \"area_drop_selected\": null," +
            "            \"min_p2p_price\": null," +
            "            \"intracity_travel_type\": null," +
            "            \"first_estimate_distance\": \"\"" +
            "        }" +
            "    ]" +
            "}";


    static {
        stateList = new ArrayList<>();
        oneWaySource = new ArrayList<>();
        roundTripSource = new ArrayList<>();
        roundTripSource = new ArrayList<>();


    }
}
