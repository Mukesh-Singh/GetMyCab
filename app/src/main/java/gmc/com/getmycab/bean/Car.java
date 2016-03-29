package gmc.com.getmycab.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Car implements Serializable{

    private String carInventoryId;
    private String operatorId;
    private CarDetails carDetails;
    private String carQuality;
    private String driverQuality;
    private String overallQuality;
    private String totalReviews;
    private String operatorName;
    private String operatorLocation;
    private String operatorPhoneNum;
    private String areaName;
    private String bookType;
    private String taxClassId;
    private PriceDetails priceDetails;

    private StateTaxDetails stateTaxDetails;

    public StateTaxDetails getStateTaxDetails() {
        return stateTaxDetails;
    }

    public void setStateTaxDetails(StateTaxDetails stateTaxDetails) {
        this.stateTaxDetails = stateTaxDetails;
    }

    /**
     *
     * @return
     * The carInventoryId
     */
    public String getCarInventoryId() {
        return carInventoryId;
    }

    /**
     *
     * @param carInventoryId
     * The car_inventory_id
     */
    public void setCarInventoryId(String carInventoryId) {
        this.carInventoryId = carInventoryId;
    }

    /**
     *
     * @return
     * The operatorId
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     *
     * @param operatorId
     * The operator_id
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    /**
     *
     * @return
     * The carDetails
     */
    public CarDetails getCarDetails() {
        return carDetails;
    }

    /**
     *
     * @param carDetails
     * The car_details
     */
    public void setCarDetails(CarDetails carDetails) {
        this.carDetails = carDetails;
    }

    /**
     *
     * @return
     * The carQuality
     */
    public String getCarQuality() {
        return carQuality;
    }

    /**
     *
     * @param carQuality
     * The car_quality
     */
    public void setCarQuality(String carQuality) {
        this.carQuality = carQuality;
    }

    /**
     *
     * @return
     * The driverQuality
     */
    public String getDriverQuality() {
        return driverQuality;
    }

    /**
     *
     * @param driverQuality
     * The driver_quality
     */
    public void setDriverQuality(String driverQuality) {
        this.driverQuality = driverQuality;
    }

    /**
     *
     * @return
     * The overallQuality
     */
    public String getOverallQuality() {
        return overallQuality;
    }

    /**
     *
     * @param overallQuality
     * The overall_quality
     */
    public void setOverallQuality(String overallQuality) {
        this.overallQuality = overallQuality;
    }

    /**
     *
     * @return
     * The totalReviews
     */
    public String getTotalReviews() {
        return totalReviews;
    }

    /**
     *
     * @param totalReviews
     * The total_reviews
     */
    public void setTotalReviews(String totalReviews) {
        this.totalReviews = totalReviews;
    }

    /**
     *
     * @return
     * The operatorName
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     *
     * @param operatorName
     * The operator_name
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     *
     * @return
     * The operatorLocation
     */
    public String getOperatorLocation() {
        return operatorLocation;
    }

    /**
     *
     * @param operatorLocation
     * The operator_location
     */
    public void setOperatorLocation(String operatorLocation) {
        this.operatorLocation = operatorLocation;
    }

    /**
     *
     * @return
     * The operatorPhoneNum
     */
    public String getOperatorPhoneNum() {
        return operatorPhoneNum;
    }

    /**
     *
     * @param operatorPhoneNum
     * The operator_phone_num
     */
    public void setOperatorPhoneNum(String operatorPhoneNum) {
        this.operatorPhoneNum = operatorPhoneNum;
    }

    /**
     *
     * @return
     * The areaName
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     *
     * @param areaName
     * The area_name
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     *
     * @return
     * The bookType
     */
    public String getBookType() {
        return bookType;
    }

    /**
     *
     * @param bookType
     * The book_type
     */
    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    /**
     *
     * @return
     * The taxClassId
     */
    public String getTaxClassId() {
        return taxClassId;
    }

    /**
     *
     * @param taxClassId
     * The tax_class_id
     */
    public void setTaxClassId(String taxClassId) {
        this.taxClassId = taxClassId;
    }

    /**
     *
     * @return
     * The priceDetails
     */
    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    /**
     *
     * @param priceDetails
     * The price_details
     */
    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }


}