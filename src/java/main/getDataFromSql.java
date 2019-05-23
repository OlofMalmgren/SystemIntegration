package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Float.MAX_VALUE;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class getDataFromSql 
{
private static Connection setupDBconnection() throws SQLException, ClassNotFoundException, FileNotFoundException, IOException
    {
        Properties p = new Properties();
        p.load(new FileInputStream("C:\\Users\\olofm\\Documents\\NetBeansProjects\\ElHallWebApp\\newproperties.properties"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(p.getProperty("connectionstring"), p.getProperty("name"), p.getProperty("password"));
        return con;
    }
    
public static float getLatestTemp(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM elhall where elhallnamnID=?");
        stmt.setString(1, Integer.toString(hallID));
        ResultSet rs1 = stmt.executeQuery();
        int I = 0;
        while(rs1.next())
        {
        I = rs1.getInt(1);
        }
        PreparedStatement stmt2 = con.prepareStatement("SELECT Temperature FROM elhall where ID=?");
        stmt2.setString(1, Integer.toString(I));
        ResultSet rs2 = stmt2.executeQuery();
        float T = 0;
        while(rs2.next())
        {
           T = rs2.getFloat("Temperature");
           
        }
        return T;
}

public static float getLatestConsumption(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        
        Connection con = setupDBconnection();
        PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM elhall where elhallnamnID=?");
        stmt.setString(1, Integer.toString(hallID));
        ResultSet rs1 = stmt.executeQuery();
        int I = 0;
        while(rs1.next())
        {
        I = rs1.getInt(1);
        }
        PreparedStatement stmt2 = con.prepareStatement("SELECT Consumption FROM elhall where ID=?");
        stmt2.setString(1, Integer.toString(I));
        ResultSet rs2 = stmt2.executeQuery();
        float Cons = 0;
        while(rs2.next())
        {
           Cons = rs2.getFloat("Consumption");
        }

        return Cons;
}
        
        
public static float getLatestCost(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM elhall where elhallnamnID=?");
        stmt.setString(1, Integer.toString(hallID));
        ResultSet rs1 = stmt.executeQuery();
        int I = 0;
        while(rs1.next())
        {
        I = rs1.getInt(1);
        }
        PreparedStatement stmt2 = con.prepareStatement("select Cost from elhall inner join cost on elhall.elhallcostID=cost.costID where ID=?");
        stmt2.setString(1, Integer.toString(I));
        ResultSet rs = stmt2.executeQuery();
        float C = 0;
        while(rs.next())
        {
        C = rs.getFloat("Cost");
        }

        return C;
}
public static void changeTemp(int hallID, float newTemp) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM elhall where elhallnamnID=?");
        stmt.setString(1, Float.toString(hallID));
        ResultSet rs1 = stmt.executeQuery();
        int I = 0;
        while(rs1.next())
        {
        I = rs1.getInt(1);
        }
        PreparedStatement stmt2 = con.prepareStatement("update elhall set Temperature = ? where ID=?");
        stmt2.setString(1, Float.toString(newTemp));
        stmt2.setString(2, Integer.toString(I));
        stmt2.executeUpdate();

}
public static void changeCost(int hallID, float newCost) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        boolean costExists = false;
        float costBuff = 0;
        int latestID = 0;
        PreparedStatement stmt = con.prepareStatement("SELECT MAX(ID) FROM elhall where elhallnamnID=?");
        stmt.setString(1, Integer.toString(hallID));
        ResultSet rs1 = stmt.executeQuery();
        while(rs1.next())
            {
                latestID = rs1.getInt(1);
            }
        Statement stmtCost = con.createStatement();
        ResultSet rs = stmtCost.executeQuery("SELECT * FROM cost");
        while(rs.next())
        {
            costBuff = rs.getFloat("Cost");
            if(costBuff == newCost)
            {
                costExists = true;
            }
        }
        if(costExists == true)
        {
            PreparedStatement stmtCostID = con.prepareStatement("SELECT costID FROM cost where Cost like ?");
            stmtCostID.setString(1, Float.toString(newCost));
            ResultSet rsCostID = stmtCostID.executeQuery();
            int CostID = 0;
            while(rsCostID.next())
            {
                CostID = rsCostID.getInt(1);
            }
            PreparedStatement stmtChangeCostID = con.prepareStatement("update elhall set elhallcostID = ? where ID=?");
            stmtChangeCostID.setString(1, Float.toString(CostID));
            stmtChangeCostID.setString(2, Integer.toString(latestID));
            stmtChangeCostID.executeUpdate();
        }
        else
        {
            PreparedStatement insertCost = con.prepareStatement("insert into cost (Cost) values (?)");
            insertCost.setString(1, Float.toString(newCost));
            insertCost.executeUpdate();
            Statement stmtCostNewID = con.createStatement();
            ResultSet rsCostNewID = stmtCostNewID.executeQuery("SELECT MAX(costID) FROM cost");
            int newCostID = 0;
            while(rsCostNewID.next())
            {
                newCostID = rsCostNewID.getInt(1);
            }
            PreparedStatement stmtChangeCostID2 = con.prepareStatement("update elhall set elhallcostID = ? where ID=?");
            stmtChangeCostID2.setString(1, Float.toString(newCostID));
            stmtChangeCostID2.setString(2, Integer.toString(latestID));
            stmtChangeCostID2.executeUpdate();
        }
}
public static void RandomizeDB() throws SQLException, ClassNotFoundException, IOException
        {
        Connection con = setupDBconnection();
        PreparedStatement stmt = con.prepareStatement("insert into elhall(Temperature, Consumption, Date, elhallnamnID, elhallcostID) values (?, ?, ?, ?, ?);");
        int counter = 0;
        while(counter < 24)
        {
            float temp = (float) (Math.random() * (30 - 20)) + 20;
            int consumption = (int) (Math.random() * (5500 - 4500)) + 4500;
            int hID = 3;
            int cID = 3;
            String date;
            if(counter < 10)
            {
            date = "2019-05-02 0" + counter + ":00:00";
            }
            else
            {
            date = "2019-05-02 " + counter + ":00:00";    
            }
            counter++;
            stmt.setString(1, Float.toString(temp));
            stmt.setString(2, Integer.toString(consumption));
            stmt.setString(3, date);
            stmt.setString(4, Integer.toString(cID));
            stmt.setString(5, Integer.toString(hID));
            stmt.executeUpdate();
            
        }
        }
        

            
public static List<DailyTR> dayTempReportXML(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        List<DailyTR> TempReport = new ArrayList<>();
        PreparedStatement stmt2 = con.prepareStatement("select MAX(Date) from elhall where elhallnamnID=?");
        stmt2.setString(1, Integer.toString(hallID));
        ResultSet rs = stmt2.executeQuery();
        String latestDate = "";
        while(rs.next())
        {
            latestDate = rs.getString(1);
        }
        String[] splitStr = latestDate.split("\\s+");
        PreparedStatement stmt3 = con.prepareStatement("select Temperature, Date from elhall where cast(Date as date) = ? and elhallnamnID=?");
        stmt3.setString(1, splitStr[0]);
        stmt3.setString(2, Integer.toString(hallID));
        rs = stmt3.executeQuery();
        
        while(rs.next())
        {
            float T = rs.getFloat("Temperature");
            String D = rs.getString("Date");
            DailyTR tempObj = new DailyTR();
            tempObj.setTemperature(T);
            tempObj.setDate(D);
            TempReport.add(tempObj);
        } 
     
        return TempReport;

}
            

           
public static List<DailyER> dayElectricityReportXML(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        List<DailyER> TempReport = new ArrayList<>();
        PreparedStatement stmt2 = con.prepareStatement("select MAX(Date) from elhall where elhallnamnID=?");
        stmt2.setString(1, Integer.toString(hallID));
        ResultSet rs = stmt2.executeQuery();
        String latestDate = "";
        while(rs.next())
        {
            latestDate = rs.getString(1);
        }
        String[] splitStr = latestDate.split("\\s+");
        PreparedStatement stmt3 = con.prepareStatement("select Consumption, elhallcostID, Date from elhall where cast(Date as date) = ? and elhallnamnID = ?");
        stmt3.setString(1, splitStr[0]);
        stmt3.setString(2, Integer.toString(hallID));
        rs = stmt3.executeQuery();
        while(rs.next())
        {
            float Cons = rs.getFloat("Consumption");
            int Cst = rs.getInt("elhallcostID");
            String D = rs.getString("Date");
            
            PreparedStatement stmtCost = con.prepareStatement("select Cost from elhall inner join cost on elhall.elhallcostID=cost.costID where elhallcostID=? and date = ? and elhallnamnID = ?");
            stmtCost.setString(1, Integer.toString(Cst));
            stmtCost.setString(2, D);
            stmtCost.setString(3, Integer.toString(hallID));
            ResultSet rs2 = stmtCost.executeQuery();
            float tempCost = 0;
            while(rs2.next())
            {
                tempCost = rs2.getFloat(1);
            }
            DailyER DER = new DailyER();
            DER.setConsumption(Cons);
            DER.setCost(tempCost);
            DER.setDate(D);
            TempReport.add(DER);
        }
        return TempReport;
    }
           
public static String dayElectricityReportMax(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        PreparedStatement stmt2 = con.prepareStatement("select MAX(Date) from elhall where elhallnamnID=?");
        stmt2.setString(1, Integer.toString(hallID));
        ResultSet rs = stmt2.executeQuery();
        String latestDate = "";
        while(rs.next())
        {
            latestDate = rs.getString(1);
        }
        String[] splitStr = latestDate.split("\\s+");
        PreparedStatement stmt3 = con.prepareStatement("SELECT MAX(Consumption), Date FROM elhall where cast(Date as date) = ? and elhallnamnID = ?");
        stmt3.setString(1, splitStr[0]);
        stmt3.setString(2, Integer.toString(hallID));
        rs = stmt3.executeQuery();
        float cMax = 0;
        while(rs.next())
        {
        cMax = rs.getFloat(1);
        }
        return Float.toString(cMax);
    }
public static String dayElectricityReportMin(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        PreparedStatement stmt2 = con.prepareStatement("select Date from elhall where elhallnamnID=?");
        stmt2.setString(1, Integer.toString(hallID));
        ResultSet rs = stmt2.executeQuery();
        String latestDate = "";
        while(rs.next())
        {
            latestDate = rs.getString(1);
        }
        String[] splitStr = latestDate.split("\\s+");
        PreparedStatement stmt3 = con.prepareStatement("SELECT MIN(Consumption) FROM elhall where cast(Date as date) = ? and elhallnamnID = ?");
        stmt3.setString(1, splitStr[0]);
        stmt3.setString(2, Integer.toString(hallID));
        rs = stmt3.executeQuery();
        float cMin = 0;
        while(rs.next())
        {
        cMin = rs.getFloat(1);
        }
        return Float.toString(cMin);
    }
public static String dayElectricityReportAvg(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        PreparedStatement stmt2 = con.prepareStatement("select Date from elhall where elhallnamnID=?");
        stmt2.setString(1, Integer.toString(hallID));
        ResultSet rs = stmt2.executeQuery();
        String latestDate = "";
        while(rs.next())
        {
            latestDate = rs.getString(1);
        }
        String[] splitStr = latestDate.split("\\s+");
        PreparedStatement stmt3 = con.prepareStatement("SELECT AVG(Consumption) FROM elhall where cast(Date as date) = ? and elhallnamnID = ?");
        stmt3.setString(1, splitStr[0]);
        stmt3.setString(2, Integer.toString(hallID));
        rs = stmt3.executeQuery();
        float cAvg = 0;
        while(rs.next())
        {
        cAvg = rs.getFloat(1);
        }
        return Float.toString(cAvg);
    }

public static String dayElectricityReportHours(int hallID) throws SQLException, ClassNotFoundException, IOException
    {
        Connection con = setupDBconnection();
        String ResultString = "";
        PreparedStatement stmt2 = con.prepareStatement("select Date from elhall where elhallnamnID=?");
        stmt2.setString(1, Integer.toString(hallID));
        ResultSet rs = stmt2.executeQuery();
        String latestDate = "";
        while(rs.next())
        {
            latestDate = rs.getString(1);
        }
        String[] splitStr = latestDate.split("\\s+");
        PreparedStatement stmt3 = con.prepareStatement("select Consumption, elhallcostID, Date from elhall where cast(Date as date) = ? and elhallnamnID = ?");
        stmt3.setString(1, splitStr[0]);
        stmt3.setString(2, Integer.toString(hallID));
        rs = stmt3.executeQuery();
        float cheapest = MAX_VALUE;
        String cheapestDate = "";
        float pricey = -MAX_VALUE;
        String priceyDate = "";
        while(rs.next())
        {
            float Cons = rs.getFloat("Consumption");
            int Cst = rs.getInt("elhallcostID");
            String D = rs.getString("Date");
            
            PreparedStatement stmtCost = con.prepareStatement("select Cost from elhall inner join cost on elhall.elhallcostID=cost.costID where elhallcostID=? and date = ? and elhallnamnID = ?");
            stmtCost.setString(1, Integer.toString(Cst));
            stmtCost.setString(2, D);
            stmtCost.setString(3, Integer.toString(hallID));
            ResultSet rs2 = stmtCost.executeQuery();
            float tempCost = 0;
            while(rs2.next())
            {
                tempCost = rs2.getFloat(1);
            }
            if(cheapest > tempCost*Cons)
            {
                cheapest = tempCost*Cons;
                cheapestDate = D + " with consumption: " + Cons + " and electricity cost: " + tempCost;
            }
            if(pricey < tempCost*Cons)
            {
                pricey = tempCost*Cons;
                priceyDate = D + " with consumption: " + Cons + " and electricity cost: " + tempCost;
            }

        }
        ResultString += "Most expensive hour: " + priceyDate + "\nCheapest hour: " + cheapestDate;
        return ResultString;
    }
                
public static String highestHallConsumption() throws SQLException, ClassNotFoundException, IOException
           {
        Connection con = setupDBconnection();
        String date = "";
        PreparedStatement stmtDate = con.prepareStatement("select MAX(Date) from elhall");
        ResultSet rs = stmtDate.executeQuery();
        while(rs.next())
        {
            date = rs.getString(1);
        }
        String[] splitStr = date.split("\\s+");
        PreparedStatement stmtGroupConsumption = con.prepareStatement("select Namn, sum(Consumption) from elhall inner join elhallnamn on elhall.elhallnamnID=elhallnamn.hallID where cast(Date as date) = ? group by elhallnamnID");
        stmtGroupConsumption.setString(1, splitStr[0]);
        rs = stmtGroupConsumption.executeQuery();
        String resultString = "";
        String mostConsumed = "";
        float mostCons = -MAX_VALUE;
        while(rs.next())
        {
            String name = rs.getString("Namn");
            float Sum = rs.getFloat(2);
            if(mostCons < Sum)
            {
                mostCons = Sum;
                mostConsumed = "\n\nHall " + name + " consumed the most electricity with a total consumption of: " + Sum;
            }
            resultString += "Elhall: " + name + " consumed: " + Sum +"\n";
        }
            resultString += mostConsumed;
            return resultString;
        }
}
