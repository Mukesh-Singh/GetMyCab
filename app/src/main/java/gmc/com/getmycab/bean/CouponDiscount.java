package gmc.com.getmycab.bean;

/**
 * Created by Baba on 9/13/2015.
 */
public class CouponDiscount {
    private String couponId;
    private String code;
    private String type;
    private String discount;
    private String logged;
    private String total;
    private String dateStart;
    private String dateEnd;
    private String usesTotal;
    private String usesCustome;
    private String status;
    private String dateAdded;
    private String tourType;
    private String usedStatus;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getLogged() {
        return logged;
    }

    public void setLogged(String logged) {
        this.logged = logged;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getUsesTotal() {
        return usesTotal;
    }

    public void setUsesTotal(String usesTotal) {
        this.usesTotal = usesTotal;
    }

    public String getUsesCustome() {
        return usesCustome;
    }

    public void setUsesCustome(String usesCustome) {
        this.usesCustome = usesCustome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public String getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(String usedStatus) {
        this.usedStatus = usedStatus;
    }
}
