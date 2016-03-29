package gmc.com.getmycab.networkapi;

import org.json.JSONObject;

/**
 * Created by Baba on 9/13/2015.
 */
public class CreateJsonFor {

    public static JSONObject oneWaySource(String cityid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city_id", cityid);
            jsonObject.put("city_type", "oneWay");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject roundTrip(String cityid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city_id", cityid);
            jsonObject.put("city_type", "roundTrip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject multiCity(String cityid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city_id", cityid);
            jsonObject.put("city_type", "multiCity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
