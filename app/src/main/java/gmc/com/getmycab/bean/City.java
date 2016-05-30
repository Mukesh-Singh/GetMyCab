package gmc.com.getmycab.bean;

/**
 * Created by Baba on 9/6/2015.
 */
public class City {
    private String citycod;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCitycod() {
        return citycod;
    }

    public void setCitycod(String citycod) {
        this.citycod = citycod;
    }

    private String cityName;

    @Override
    public String toString() {
        return this.cityName;
    }

    @Override
    public int hashCode() {
        return cityName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof City) {
            City c=(City)o;

            if (cityName.toLowerCase().equalsIgnoreCase(c.getCityName().toLowerCase().trim()))
                return true;
        }
        return false;
    }
}
