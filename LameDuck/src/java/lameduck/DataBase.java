/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.FlightType;

/**
 *
 * @author Ali C, Daniel Brand, Raj, Rustam
 */
public class DataBase {
    
    //initialization of database of flight list
    public static List<FlightType> initializeFlightList() throws DatatypeConfigurationException{
        List<FlightType> listOfFlights= new ArrayList<FlightType>();
        FlightType flight1 =CreateFlight("Copenhagen", "London", SetGregorianDateTime(14,25,26,10,2015), SetGregorianDateTime(17,05,26,10,2015), "Ryanair");
        listOfFlights.add(flight1);
        FlightType flight2 =CreateFlight("London", "New York", SetGregorianDateTime(20,00,26,10,2015), SetGregorianDateTime(8,30,27,10,2015), "British Airways");
        listOfFlights.add(flight2);
        FlightType flight3 =CreateFlight("Copenhagen", "Kuala Lumpur", SetGregorianDateTime(7,25,26,2,2016), SetGregorianDateTime(17,05,26,2,2016), "Malasian Airways");
        listOfFlights.add(flight3);
        FlightType flight4 =CreateFlight("New York", "London", SetGregorianDateTime(14,25,30,10,2015), SetGregorianDateTime(23,05,27,10,2015), "Iberia");
        listOfFlights.add(flight4);
        FlightType flight5 =CreateFlight("Barcelona", "New York", SetGregorianDateTime(23,50,26,12,2015), SetGregorianDateTime(10,10,27,12,2015), "SAS");
        listOfFlights.add(flight5);
        return listOfFlights;
    }
    
    //XML Gregorian Date and Time Setter
    private static XMLGregorianCalendar SetGregorianDateTime(Integer hours, Integer minutes, Integer day, Integer month, Integer year) throws DatatypeConfigurationException {
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar gregorianDate = df.newXMLGregorianCalendar(gc);
        gregorianDate.setMinute(minutes);
        gregorianDate.setHour(hours);
        gregorianDate.setDay(day);
        gregorianDate.setMonth(month);
        gregorianDate.setYear(year);
        
        return gregorianDate;
    }
    
    //FlightType creator
    private static FlightType CreateFlight(String Start, String Destination, XMLGregorianCalendar StartDateTime, XMLGregorianCalendar DestinationDateTime, String carrier ){
        FlightType flight = new FlightType();
        flight.setStart(Start);
        flight.setDestination(Destination);
        flight.setStartDateTime(StartDateTime);
        flight.setDestinationDateTime(StartDateTime);
        flight.setCarrier(carrier);
        
        return flight;
    }
    
    // Create a hashmap to put the flight and a booking number
    static public HashMap<FlightType, String> InitializeBookingHashtable(List<FlightType> listFlight){
        HashMap<FlightType, String> bookingHashtable = new  HashMap<FlightType, String>();
        for(int i=0; i < listFlight.size(); i++){
            String bookingNumber = Integer.toString(19457 + i);
            bookingHashtable.put(listFlight.get(i), bookingNumber);
        }
        return bookingHashtable;
    }
   
    // Create a hasmap to put if the flight is reserved or not, flight is regestir using his name  
    static public HashMap<FlightType, Boolean> InitializeAvailableFlightHashtable(List<FlightType> listFlight){
        HashMap<FlightType, Boolean> availableFlights = new  HashMap<>();
        for (FlightType listFlight1 : listFlight) {
            availableFlights.put(listFlight1, true);
        }
        return availableFlights;
    }
    
    // Function used to revert hashmaps
    public static<K,V> HashMap<V,K> reverse(Map<K,V> map) {
        HashMap<V,K> rev = new HashMap<>();
        for(Map.Entry<K,V> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }
    /*
    // Used to find the Flight when only having the name of the hotel
    static public HashMap<String, FlightType> helpFlightHashMap(){
        HashMap<String, FlightType> hashMap = new HashMap<>();          
        List<FlightType> listFlights = InitializeFlighList();
        for (FlightType listFlight : listFlights) {
            hashMap.put(listFlight.getName(), listFlight);
        }
        return hashMap;
    }
    */
}
