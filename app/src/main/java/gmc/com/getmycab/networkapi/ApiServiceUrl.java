package gmc.com.getmycab.networkapi;

/**
 * Created by Baba on 9/5/2015.
 */
public class ApiServiceUrl {
    public static final String DOMAIN_NAME="http://www.getmecab.com/";
    public static final String BASE_URL=DOMAIN_NAME+"GMC/";
    public static final String LOGIN="customer_login";
    public static final String SIGNUP="customer_registration";
    public static final String SEARCH_CAB="gmc_search_cab";
    public static final String STATE_LIST="gmc_states";
    public static final String CITY_LIST_TRAVEL="gmc_cities";
    public static final String SEND_LOCATION="send_location";
    public static final String GET_PROFILE="gmc_user_profile";
    public static final String UPDATE_PROFILE="update_customer_profile";
    public static final String CHANGE_PASSWORD="gmc_change_pasword";
    public static final String FEEDBACK="feedback";
    public static final String DISCOUNT_COUPON="discount_coupon";
    public static final String BOOKING_URL="http://www.getmecab.com/index.php?route=api/confirm";
    public static final String BOOKING_HISTORY="http://www.getmecab.com/index.php?route=account/history/paymentHistoryForApp/";
    public static final String BOOKING_BOOKING_DETAILS="http://www.getmecab.com/index.php?route=account/invoice/orderDetailsForApp&order_id=OID&customerId=";
    public static final String BOOKING_SUCCESS="http://www.getmecab.com/index.php?route=payment/ccavenue/callbackApp&Merchant_Id=MI&Amount=AMT&Order_Id=OID";



}
