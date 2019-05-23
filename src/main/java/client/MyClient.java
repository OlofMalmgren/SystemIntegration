package client;

import com.google.gson.Gson;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import static java.lang.Float.MAX_VALUE;
import java.util.List;


public class MyClient 
{
    private static ClientConfig config = new DefaultClientConfig();
    private static Client client = Client.create(config);
    private static WebResource service = client.resource(
UriBuilder.fromUri("http://localhost:8080/ElHallWebApp").build());


public static void main(String[] args) throws IOException 
{
    boolean runMenu = true;
    int menuChoice;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));    
    
    while(runMenu)
    {
        System.out.println("");
        System.out.println("1. Get current temperature.");
        System.out.println("2. Get current electric consumption.");
        System.out.println("3. Get cost of electricity.");
        System.out.println("4. Get latest value report");
        System.out.println("5. Set new temperature.");
        System.out.println("6. Set new cost.");
        System.out.println("7. Get daily temperature report.");
        System.out.println("8. Get daily electricity report.");
        System.out.println("9. Check which hall consumes most electricity.");
        System.out.println("10. Exit menu.");
        System.out.println("\nInput: ");
        menuChoice = Integer.parseInt(reader.readLine());
        if(menuChoice == 1)
        {
            System.out.println("\nChoose which hall to read from\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            ReadTemperature(buff);
        }
        if(menuChoice == 2)
        {
            System.out.println("\nChoose which hall to read from\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            ReadConsumption(buff);
        }
        if(menuChoice == 3)
        {
            System.out.println("\nChoose which hall to read from\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            ReadCost(buff);
        }
        if(menuChoice == 4)
        {
            System.out.println("\nChoose which hall to read from\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            ReadLatestReport(buff);
        }
        if(menuChoice == 5)
        {
            System.out.println("\nChoose which hall to change temperature in.\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            System.out.println("\nInput new temperature.\nInput: ");
            float buff2 = Float.parseFloat(reader.readLine());
            ChangeTemperature(buff, buff2);
        }
        if(menuChoice == 6)
        {
            System.out.println("\nChoose which hall to change cost in.\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            System.out.println("\nInput new cost.\nInput: ");
            float buff2 = Float.parseFloat(reader.readLine());
            ChangeCost(buff, buff2);
        }
        if(menuChoice == 7)
        {
            System.out.println("\nChoose which hall to read from\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            DailyTemperatureReport(buff);
        }
        if(menuChoice == 8)
        {
            System.out.println("\nChoose which hall to read from\nInput: ");
            int buff = Integer.parseInt(reader.readLine());
            DailyElectricityReport(buff);
        }
        if(menuChoice == 9)
        {
            HighestHallConsumption();
        }
        if(menuChoice == 10)
        {
            System.out.println("Bye.");
            runMenu = false;
        }
    }
       
}
public static void ReadTemperature(int hallID)
    {
        String xmlString = service.path("rest/REST/latestTemp/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
    System.out.println("Temperature: " + xmlString);
    }
public static void ReadConsumption(int hallID)
    {
        String xmlString = service.path("rest/REST/latestConsumption/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Consumption: " + xmlString);
    }
public static void ReadCost(int hallID)
    {
        String xmlString = service.path("rest/REST/latestCost/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Cost: " + xmlString);
    }
public static void ReadLatestReport(int hallID)
    {
        String xmlString = service.path("rest/REST/recentReport/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println(xmlString);
    }
public static void ChangeTemperature(int hallID , float newTemp)
    {
        service.path("rest/REST/newTemp/" + hallID + "/" + newTemp).head();
        System.out.println("Temperature changed to: ");
        ReadTemperature(hallID);
    }
public static void ChangeCost(int hallID , float newTemp)
    {
        service.path("rest/REST/newCost/" + hallID + "/" + newTemp).head();
        System.out.println("Cost changed to: ");
        ReadCost(hallID);
    }

public static void DailyTemperatureReport(int hallID)
    {
        DailyTR[] DTR = service.path("rest/REST/dayTempReportXML/" + hallID)
                .accept(MediaType.APPLICATION_XML).get(DailyTR[].class);
        float highest = -MAX_VALUE;
        float lowest = MAX_VALUE;
        int counter = 0;
        float sum = 0;
        for (DailyTR R : DTR)
        {
            System.out.println("Temperature: " + R.getTemperature() + " date: " + R.getDate());
            if(R.getTemperature() > highest)
            {
                highest = R.getTemperature();
            }
            if(R.getTemperature() < lowest)
            {
                lowest = R.getTemperature();
            }
            counter++;
            sum += R.getTemperature();
        }
        float avg = sum/counter;
        System.out.println("Highest measured temperature: " + highest + " Lowest measured temperature: " + lowest + " Average temperature: " + avg);
        
    }


public static void DailyElectricityReport(int hallID)
    {
        DailyER[] DER = service.path("rest/REST/dayElectricityReportXML/" + hallID)
            .accept(MediaType.APPLICATION_XML).get(DailyER[].class);
        for (DailyER R : DER)
        {
            System.out.println("Consumption: " + R.getConsumption() + " Cost: " + R.getCost() + " Date: " + R.getDate());
        }
        String plainMax = service.path("rest/REST/dayElectricityReportMax/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Max consumption: " + plainMax);
        
        String plainMin = service.path("rest/REST/dayElectricityReportMin/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Min consumption: " + plainMin);
        
        String plainAvg = service.path("rest/REST/dayElectricityReportAvg/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Average consumption: " + plainAvg);
        
        String plainhours = service.path("rest/REST/dayElectricityReportHours/" + hallID)
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println(plainhours);
    }
public static void HighestHallConsumption()
    {
        String xmlString = service.path("rest/REST/highestHallConsumption")
            .accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println(xmlString);
    }
}