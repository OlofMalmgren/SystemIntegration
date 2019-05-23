package main;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "DailyTR")

public class DailyTR implements Serializable
{
    private static final long serialVersionUID = 2L;
    private float temperature;
    private String date;
    
    public DailyTR(){}
    
    public DailyTR(float temperature, String date)
    {
        this.temperature = temperature;
        this.date = date;
    }
@XmlElement
    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
@XmlElement
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
