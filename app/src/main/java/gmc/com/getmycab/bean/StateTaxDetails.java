package gmc.com.getmycab.bean;

import java.util.List;

/**
 * Created by Baba on 9/12/2015.
 */
public class StateTaxDetails {
    private String stateTax;
    private List<StateData> stateDatas;

    public String getStateTax() {
        return stateTax;
    }

    public void setStateTax(String stateTax) {
        this.stateTax = stateTax;
    }

    public List<StateData> getStateDatas() {
        return stateDatas;
    }

    public void setStateDatas(List<StateData> stateDatas) {
        this.stateDatas = stateDatas;
    }


}
