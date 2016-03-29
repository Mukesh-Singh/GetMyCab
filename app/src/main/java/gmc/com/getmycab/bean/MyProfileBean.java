package gmc.com.getmycab.bean;

/**
 * Created by Baba on 9/11/2015.
 */
public class MyProfileBean {
    private String customerId;
    private String firstname="";
    private String lastname="";
    private String email="";
    private String telephone="";
    private String address="";
    private String state="";
    private String country="";
    private String city="";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city!=null && !city.equalsIgnoreCase("null"))
            this.city = city;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if (firstname!=null && !firstname.equalsIgnoreCase("null"))
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    } 

    public void setLastname(String lastname) {
        if (lastname!=null && !lastname.equalsIgnoreCase("null"))
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email!=null && !email.equalsIgnoreCase("null"))
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone!=null && !telephone.equalsIgnoreCase("null"))
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address!=null && !address.equalsIgnoreCase("null"))
        this.address = address;
    }

    public String getState() {

        return state;
    }

    public void setState(String state) {
        if (state!=null && !state.equalsIgnoreCase("null"))
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country!=null && !country.equalsIgnoreCase("null"))
        this.country = country;
    }
}
