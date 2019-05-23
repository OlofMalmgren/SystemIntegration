package main;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "HallData")


public class HallData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private float temperature;
    private float consumption;;
    private String date;
    private int elhallnamnID;
    
    public HallData(){}
    
    public HallData(float temperature, float consumption, String date, int elhallnamnID)
    {
        this.temperature = temperature;
        this.consumption = consumption;
        this.date = date;
        this.elhallnamnID = elhallnamnID;
    }
@XmlElement
    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
@XmlElement
    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

@XmlElement
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
@XmlElement
    public int getElhallnamnID() {
        return elhallnamnID;
    }

    public void setElhallnamnID(int elhallnamnID) {
        this.elhallnamnID = elhallnamnID;
    }
    
    
    
}
