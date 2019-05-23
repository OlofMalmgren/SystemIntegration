package main;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "DailyER")

public class DailyER implements Serializable
{
    private static final long serialVersionUID = 3L;
    private float consumption;
    private float cost;
    private String date;
    
    public DailyER(){}
    
    
    public DailyER(float consumption, float cost, String date)
    {
        this.consumption = consumption;
        this.cost = cost;
        this.date = date;
    }
@XmlElement
    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }
@XmlElement
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
@XmlElement
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
