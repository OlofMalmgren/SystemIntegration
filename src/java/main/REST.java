package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("/REST")

public class REST 
{   
    @GET
    @Path("/latestTemp/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public float getTemp(@PathParam("hallID") int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        float temp = getDataFromSql.getLatestTemp(hallID);
        return temp;
    }
    
    @GET
    @Path("/latestConsumption/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public float getConsumption(@PathParam("hallID") int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        float Consumption = getDataFromSql.getLatestConsumption(hallID);
        return Consumption;
    }
    
    @GET
    @Path("/latestCost/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public float getCost(@PathParam("hallID") int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        float Cost = getDataFromSql.getLatestCost(hallID);
        return Cost;
    }
    
    @GET
    @Path("/recentReport/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRecentReport(@PathParam("hallID") int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        String Report = "";
        float ID = getDataFromSql.getLatestTemp(hallID);
        float Consumption = getDataFromSql.getLatestConsumption(hallID);
        float Cost = getDataFromSql.getLatestCost(hallID);
        Report += "Temperature: " + ID + "\nConsumption: " + Consumption + "\nCost: " + Cost;
        
        return Report;
    }
    
    
    @GET
    @Path("/newTemp/{hallID}/{newTemp}")
    public void putNewTemp(@PathParam("hallID") int hallID, @PathParam("newTemp") float newTemp) throws SQLException, ClassNotFoundException, IOException
    {
        getDataFromSql.changeTemp(hallID, newTemp);
    }
    
    @GET
    @Path("/newCost/{hallID}/{newCost}")
    public void putNewCost(@PathParam("hallID") int hallID, @PathParam("newCost") float newCost) throws SQLException, ClassNotFoundException, IOException
    {
        getDataFromSql.changeCost(hallID, newCost);
    }

    @GET
    @Path("/dayTempReportXML/{hallID}")
    @Produces(MediaType.APPLICATION_XML)
    public List<DailyTR> getdayTempReportXML(@PathParam("hallID") int hallID) throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.dayTempReportXML(hallID);
    }
    
    @GET
    @Path("/dayElectricityReportXML/{hallID}")
    @Produces(MediaType.APPLICATION_XML)
    public List<DailyER> getdayElectricityReportXML(@PathParam("hallID") int hallID) throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.dayElectricityReportXML(hallID);
    }
    
    @GET
    @Path("/dayElectricityReportMax/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getdayElectricityReportMax(@PathParam("hallID") int hallID) throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.dayElectricityReportMax(hallID);
    }
    
    @GET
    @Path("/dayElectricityReportMin/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getdayElectricityReportMin(@PathParam("hallID") int hallID) throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.dayElectricityReportMin(hallID);
    }
    
    @GET
    @Path("/dayElectricityReportAvg/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getdayElectricityReportAvg(@PathParam("hallID") int hallID) throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.dayElectricityReportAvg(hallID);
    }
    
    @GET
    @Path("/dayElectricityReportHours/{hallID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getdayElectricityReporthours(@PathParam("hallID") int hallID) throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.dayElectricityReportHours(hallID);
    }
    
    @GET
    @Path("/highestHallConsumption")
    @Produces(MediaType.TEXT_PLAIN)
    public String gethighestHallConsumption() throws ClassNotFoundException, SQLException, IOException
    {
        return getDataFromSql.highestHallConsumption();
    }
}
