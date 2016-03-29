package gmc.com.getmycab.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmc.com.getmycab.bean.BookingBean;
import gmc.com.getmycab.bean.BookingDetailsBean;
import gmc.com.getmycab.bean.BookingHistoryBean;
import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.bean.CarDetails;
import gmc.com.getmycab.bean.City;
import gmc.com.getmycab.bean.CouponDiscount;
import gmc.com.getmycab.bean.DistanceDetails;
import gmc.com.getmycab.bean.MyProfileBean;
import gmc.com.getmycab.bean.PriceDetails;
import gmc.com.getmycab.bean.ReferralDiscount;
import gmc.com.getmycab.bean.State;
import gmc.com.getmycab.bean.StateData;
import gmc.com.getmycab.bean.StateTaxDetails;
import gmc.com.getmycab.bean.UserDetails;

/**
 * Created by Baba on 9/6/2015.
 */
public class ResponseParser {




    public static List<State> parseIntoStateList(String response){
        List<State> list= new ArrayList<State>();
        try {
            JSONObject jsonObject= new JSONObject(response);
            JSONArray data= jsonObject.optJSONArray("data");
            for (int i= 0;i<data.length();i++){
                JSONObject obj= data.optJSONObject(i);
                State bean= new State();
                bean.setStateCode(obj.optString("state_id"));
                bean.setStateName(obj.optString("state_name"));
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    public static List<City> parseIntoCityList(String response){
        List<City> list= new ArrayList<City>();
        try {
            JSONObject jsonObject= new JSONObject(response);
            JSONArray data= jsonObject.optJSONArray("data");
            for (int i= 0;i<data.length();i++){
                JSONObject obj= data.optJSONObject(i);
                City bean= new City();
                bean.setCitycod(obj.optString("location_id"));
                bean.setCityName(obj.optString("city_name"));
                if ((bean.getCitycod()!=null && !bean.getCitycod().equalsIgnoreCase("null") )&&(bean.getCityName()!=null && !bean.getCityName().equalsIgnoreCase("null") ))
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    public static UserDetails parseIntoUserDetails(String response){
        try {
            UserDetails details= new UserDetails();
            JSONObject jsonObject= new JSONObject(response);
            JSONObject data= jsonObject.optJSONObject("data");
            details.setCustomerId(data.optString("customer_id",""));
            details.setStoreId(data.optString("store_id", ""));
            details.setFirstname(data.optString("firstname",""));
            details.setLastname(data.optString("lastname", ""));
            details.setEmail(data.optString("email", ""));
            details.setTelephone(data.optString("telephone", ""));
            details.setFax(data.optString("fax", ""));
            details.setPassword(data.optString("password", ""));
            details.setCart(data.optString("cart", ""));
            details.setNewsletter(data.optString("newsletter", ""));
            details.setAddressId(data.optString("address_id", ""));

            details.setStatus(data.optString("status",""));
            details.setApproved(data.optString("approved",""));
            details.setCustomerGroupId(data.optString("customer_group_id",""));
            details.setIp(data.optString("ip",""));
            details.setDateAdded(data.optString("date_added",""));
            details.setIsTravelagent(data.optString("is_travelagent",""));
            details.setIsManager(data.optString("is_manager",""));
            details.setAgencyId(data.optString("agency_id",""));
            details.setIsAffiliate(data.optString("is_affiliate",""));
            details.setIsReferralUser(data.optString("is_referral_user",""));

            return details;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<Car> parseIntoCarList(String response){
        List<Car> list= new ArrayList<Car>();
        try {
            JSONArray data= new JSONArray(response);
            for (int i= 0;i<data.length();i++){
                JSONObject obj= data.getJSONObject(i);
                Car car= new Car();
                car.setCarInventoryId(obj.optString("car_inventory_id", ""));
                car.setOperatorId(obj.optString("operator_id", ""));
                car.setCarQuality(obj.optString("car_quality", ""));
                car.setDriverQuality(obj.optString("driver_quality", ""));
                car.setOverallQuality(obj.optString("overall_quality", ""));
                car.setTotalReviews(obj.optString("total_reviews", ""));
                car.setOperatorName(obj.optString("operator_name", ""));
                car.setOperatorLocation(obj.optString("operator_location", ""));
                car.setOperatorPhoneNum(obj.optString("operator_phone_num", ""));
                car.setAreaName(obj.optString("area_name", ""));
                car.setBookType(obj.optString("book_type", ""));
                car.setTaxClassId(obj.optString("tax_class_id", ""));

                JSONObject price= obj.optJSONObject("price_details");
                PriceDetails pr= new PriceDetails();
                pr.setBasePrice(price.optString("base_price", ""));
                pr.setTotalPrice(price.optString("total_price", ""));

                pr.setAverageKm(price.optString("avg_km", ""));
                pr.setMinimumKmPerDay(price.optString("minkm_day", ""));
                pr.setFare(price.optString("fare", ""));
                pr.setBaseFare(price.optString("base_fare", ""));
                pr.setBaseDriverCharge(price.optString("base_driver_charge", ""));
                pr.setGmcDriverCharge(price.optString("gmc_driver_charge", ""));
                pr.setDistance(price.optString("distance", ""));
                pr.setDistanceType(price.optString("distance_type", ""));

                JSONArray disArray=price.optJSONArray("distance_details");
                List<DistanceDetails> dsList= new ArrayList<>();
                if (disArray!=null && disArray.length()>=0){
                    for (int k=0;k<disArray.length();k++) {
                        DistanceDetails distanceDetails = new DistanceDetails();
                        distanceDetails.setSourceCity(disArray.optJSONObject(k).optString("src_city", ""));
                        distanceDetails.setDestinationCity(disArray.optJSONObject(k).optString("dest_city", ""));
                        distanceDetails.setDistance(disArray.optJSONObject(k).optString("distance", ""));
                        dsList.add(distanceDetails);
                    }
                }
                pr.setDistanceDetails(dsList);


                car.setPriceDetails(pr);


                JSONObject tax= obj.optJSONObject("state_tax_details");
                if (tax!=null) {
                    StateTaxDetails stateTaxDetails = new StateTaxDetails();
                    stateTaxDetails.setStateTax(tax.optString("state_tax", ""));
                    List<StateData> sList = new ArrayList<StateData>();
                    JSONArray stData = tax.optJSONArray("state_data");
                    for (int j = 0; j < stData.length(); j++) {
                        StateData data1 = new StateData();
                        JSONObject jobj = stData.getJSONObject(j);
                        data1.setStateName(jobj.optString("state_name", ""));
                        data1.setTaxFrequency(jobj.optString("tax_frequency", ""));
                        data1.setStateTax(jobj.optString("state_tax", ""));
                        sList.add(data1);


                    }
                    stateTaxDetails.setStateDatas(sList);
                    car.setStateTaxDetails(stateTaxDetails);
                }

                JSONObject dt= obj.optJSONObject("car_details");
                CarDetails carDetails= new CarDetails();
                carDetails.setCarGroupId(dt.optString("car_group_id",""));
                carDetails.setCarModel(dt.optString("car_model", ""));
                carDetails.setAcType(dt.optString("ac_type",""));
                carDetails.setNumSeats(dt.optString("num_seats",""));
                carDetails.setCarImage(dt.optString("car_image",""));
                carDetails.setCarCategory(dt.optString("car_category",""));
                car.setCarDetails(carDetails);

                list.add(car);



            }
        }
        catch (Exception e){
            if (e!=null)
                    e.printStackTrace();
            //return list;
        }


        return list;
    }

    public static MyProfileBean parseIntoMyProfile(String response){
        try {

            JSONObject jsonObject= new JSONObject(response);
            JSONObject data= jsonObject.optJSONObject("data");
            MyProfileBean profileBean= new MyProfileBean();
            profileBean.setFirstname(data.optString("firstname",""));
            profileBean.setLastname(data.optString("lastname",""));
            profileBean.setEmail(data.optString("email",""));
            profileBean.setTelephone(data.optString("telephone",""));
            profileBean.setAddress(data.optString("address_1",""));
            profileBean.setCity(data.optString("city",""));
            profileBean.setCountry(data.optString("country",""));
            profileBean.setCustomerId(data.optString("customer_id",""));
            profileBean.setState(data.optString("state",""));
            return profileBean;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static ReferralDiscount parseIntoReferralDiscount(String response){
        ReferralDiscount discount= new ReferralDiscount();
        try {
            JSONObject jsonObject= new JSONObject(response);
            JSONObject data= jsonObject.optJSONObject("data");
            discount.setCode(data.optString("code", ""));
            discount.setDiscount(data.optString("discount", ""));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return discount;
    }

    public static CouponDiscount parseIntoCouponDiscount(String response){
        CouponDiscount discount= new CouponDiscount();
        try {
            JSONObject jsonObject= new JSONObject(response);
            JSONObject data= jsonObject.optJSONObject("data");
            discount.setCouponId(data.optString("coupon_id",""));
            discount.setCode(data.optString("code", ""));
            discount.setType(data.optString("type", ""));
            discount.setDiscount(data.optString("discount", ""));
            discount.setLogged(data.optString("logged", ""));
            discount.setTotal(data.optString("total", ""));
            discount.setDateStart(data.optString("date_start", ""));
            discount.setDateEnd(data.optString("date_end", ""));
            discount.setUsesTotal(data.optString("uses_total", ""));
            discount.setUsesCustome(data.optString("uses_customer", ""));
            discount.setStatus(data.optString("status", ""));
            discount.setDateAdded(data.optString("date_added", ""));
            discount.setTourType(data.optString("tour_type", ""));
            discount.setUsedStatus(data.optString("usedStatus", ""));


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return discount;
    }
    public static BookingBean parseIntoBookingBean(String response) {
        BookingBean bookingBean = new BookingBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.optJSONObject("response");
            if (data != null) {
                bookingBean.setOrderId(data.optString("order_id", "PND3542"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            bookingBean.setOrderId("EXP3542");
        }
        return bookingBean;
    }

    public static List<BookingHistoryBean>parseIntoBookingHistoryBeanList(String response) {
        List<BookingHistoryBean> list= new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray = jsonObject.optJSONArray("data");
            if (dataArray != null && dataArray.length()>0) {
                for (int i=0;i < dataArray.length();i++) {
                    BookingHistoryBean b = new BookingHistoryBean();
                    JSONObject object = dataArray.optJSONObject(i);
                    if (object!=null){
                        b.setOrderId(object.optString("order_id",""));
                        b.setAmount(object.optString("total", ""));
                        b.setDate(object.optString("date_added", ""));
                        b.setSourceDestination(object.optString("destination", "No data"));
                        b.setAdvance(object.optString("paid_advance_payment","Rs. 0"));
                        list.add(b);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public static BookingDetailsBean parseIntoBookingDetailsBean(String response) {
        BookingDetailsBean d = new BookingDetailsBean();
        try {
            JSONObject object= new JSONObject(response);
            JSONArray array= object.optJSONArray("data");
            if (array!=null && array.length()>0) {
                JSONObject data = array.getJSONObject(0);
                if (data != null) {
                    d.setOrderId(data.optString("order_id", ""));
                    d.setLocationPairId(data.optString("location_pair_id", ""));
                    d.setCarInventoryId(data.optString("car_inventory_id", ""));
                    d.setBookingStartTime(data.optString("booking_start_time", ""));
                    d.setBookingEndTime(data.optString("booking_end_time", ""));
                    d.setDriverDetailsId(data.optString("driver_details_id", ""));
                    d.setBookType(data.optString("book_type", ""));
                    d.setCarModel(data.optString("car_model", ""));
                    d.setOperator(data.optString("operator", ""));
                    d.setPrice(data.optString("price", ""));
                    d.setTotal(data.optString("total", ""));
                    d.setTax(data.optString("tax", ""));
                    d.setFare(data.optString("fare", ""));
                    d.setDistance(data.optString("distance", ""));
                    d.setDistanceType(data.optString("distance_type", ""));
                    d.setTourType(data.optString("tour_type", ""));
                    d.setFareperkm(data.optString("fareperkm", ""));
                    d.setFareperhr(data.optString("fareperhr", ""));

                    d.setOrderType(data.optString("order_type", ""));
                    d.setCommission(data.optString("commission", ""));
                    d.setStateTax(data.optString("state_tax", ""));
                    d.setServiceTax(data.optString("service_tax", ""));
                    d.setDriverCharges(data.optString("driver_charges", ""));
                    d.setSrcLocation(data.optString("src_location", ""));
                    d.setDestLocation(data.optString("dest_location", ""));
                    d.setPaidAdvancePayment(data.optString("paid_advance_payment", ""));
                    d.setFinalPrice(data.optString("final_price", ""));

                    d.setFinalKm(data.optString("final_km", ""));
                    d.setFinalFare(data.optString("final_fare", ""));
                    d.setAcType(data.optString("ac_type", ""));
                    d.setDriverName(data.optString("driver_name", ""));
                    d.setDriverMobileNum(data.optString("driver_mobile_num", ""));
                    d.setCabRegisterNum(data.optString("cab_register_num", ""));

                    d.setOperatorFinalKm(data.optString("operator_final_km", ""));
                    d.setAdvancePayable(data.optString("advance_payable", ""));
                    d.setOutstationTravelType(data.optString("outstation_travel_type", ""));
                    d.setTourDuration(data.optString("tour_duration", ""));
                    d.setMinKm(data.optString("min_km", ""));
                    d.setNightCharges(data.optString("night_charges", ""));
                    d.setMcList(data.optString("mc_list", ""));
                    d.setGarageDistance(data.optString("garage_distance", ""));


                    d.setAreaSelcted(data.optString("area_selcted", ""));
                    d.setIntracityTravelType(data.optString("intracity_travel_type", ""));
                    d.setFirstEstimateDistance(data.optString("first_estimate_distance", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return d;
    }



}
