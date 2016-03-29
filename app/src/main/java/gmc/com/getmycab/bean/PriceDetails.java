package gmc.com.getmycab.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceDetails {

    private String totalPrice;
    private String basePrice;


    private String averateKm;
    private String minimumKmPerDay;
    private String fare;
    private String baseFare;
    private String baseDriverCharge;
    private String gmcDriverCharge;
    private String distance;
    private String distanceType;
    private List<DistanceDetails> distanceDetails;

    public String getAverageKm() {
        return averateKm;
    }

    public void setAverageKm(String averateKm) {
        this.averateKm = averateKm;
    }

    public String getMinimumKmPerDay() {
        return minimumKmPerDay;
    }

    public void setMinimumKmPerDay(String minimumKmPerDay) {
        this.minimumKmPerDay = minimumKmPerDay;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getBaseDriverCharge() {
        return baseDriverCharge;
    }

    public void setBaseDriverCharge(String baseDriverCharge) {
        this.baseDriverCharge = baseDriverCharge;
    }

    public String getGmcDriverCharge() {
        return gmcDriverCharge;
    }

    public void setGmcDriverCharge(String gmcDriverCharge) {
        this.gmcDriverCharge = gmcDriverCharge;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public List<DistanceDetails> getDistanceDetails() {
        return distanceDetails;
    }

    public void setDistanceDetails(List<DistanceDetails> distanceDetails) {
        this.distanceDetails = distanceDetails;
    }

    /**
     *
     * @return
     * The totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     *
     * @param totalPrice
     * The total_price
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     *
     * @return
     * The basePrice
     */
    public String getBasePrice() {
        return basePrice;
    }

    /**
     *
     * @param basePrice
     * The base_price
     */
    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }


}
