/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;

/**
 *
 * @author Quentin
 */
public class TravelGoodClientTest {
    
    public TravelGoodClientTest() {
    }
    
    
    
    @Test
    public void oneHotel() throws DatatypeConfigurationException {
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
        
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest);
        
        GetFlightsInputType inputF  = new GetFlightsInputType();
        
        GetOutputType output = getFlightsAndHotels(input);
        
        System.out.println("ouput: "+ output.getHotelsList().size());
        System.out.println("ouput 0: "+ output.getHotelsList().get(0).getHotelInformations().size());
        //System.out.println("ouput 1: "+ output.getHotelsList().get(1).getHotelInformations().size());
        String expected = "Milan Hotel"; 
        String result = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected, result);
    }
    
    @Test
    public void threeHotels() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        GetHotelInputType hotel2 = CreateGetHotelInputType("London");
        GetHotelInputType hotel3 = CreateGetHotelInputType("Paris");
        hotelRequest.getHotelsList().add(hotel);
        hotelRequest.getHotelsList().add(hotel2);
        hotelRequest.getHotelsList().add(hotel3);
        input.getHotelRequests().add(hotelRequest); 
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input);
               
        String expected1 = "Milan Hotel";
        String expected2 = "London Hotel"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get() size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(1).getHotel().getName());  
        //System.out.println("GetHotelsInformations.get(1) size: " + output.getHotelsList().get(0).getHotelInformations().size());
        String result2 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(2).getHotelInformations().size(); 
        assertEquals(2, result3); 
    }
    
    @Test
    public void threeFlights() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();
            
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        GetFlightsInputType flight2 = CreateGetFlightsInputType("London", "New York", 26,10,2015);
        GetFlightsInputType flight3 = CreateGetFlightsInputType("Copenhagen", "Kuala Lumpur", 26,2,2016);
        
        System.out.println("Inputs created");
        flightRequest.getFlightsList().add(flight);
        flightRequest.getFlightsList().add(flight2);
        flightRequest.getFlightsList().add(flight3);
        input.getFlightRequests().add(flightRequest); 
        System.out.println("Inputs created bis");
          
        // Make the request
        GetOutputType output = getFlightsAndHotels(input);
        System.out.println("Request done");
          
        String expected1 = "Copenhagen";
        String expected2 = "London"; 
        String expected3 = "Copenhagen";
        
        System.out.println("getFlightsList size: " + output.getFlightsList().size());
        System.out.println("getFlightsList.get() size: " + output.getFlightsList().get(0).getFlightInformations().size());
        
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              

        String result2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected2, result2); 
        String result3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected3, result3);
    }
    
    @Test
    public void flightsAndHotels() throws DatatypeConfigurationException{
        
        System.out.println("Test starts");
        
        GetInputType inputData = new GetInputType();
        //flights
        FlightRequestType flightRequest = new FlightRequestType();        
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        GetFlightsInputType flight2 = CreateGetFlightsInputType("London", "New York", 26,10,2015);
        GetFlightsInputType flight3 = CreateGetFlightsInputType("Copenhagen", "Kuala Lumpur", 26,2,2016);
        flightRequest.getFlightsList().add(flight);
        flightRequest.getFlightsList().add(flight2);
        flightRequest.getFlightsList().add(flight3);
        inputData.getFlightRequests().add(flightRequest);
        //hotels
        HotelRequestType hotelRequest = new HotelRequestType();    
        GetHotelInputType hotel1 = CreateGetHotelInputType("Madrid");
        GetHotelInputType hotel2 = CreateGetHotelInputType("Paris");
        hotelRequest.getHotelsList().add(hotel1);
        hotelRequest.getHotelsList().add(hotel2);
        inputData.getHotelRequests().add(hotelRequest); 
        
        System.out.println("Inputs created");
        // Make the request
        GetOutputType output = getFlightsAndHotels(inputData);
        System.out.println("Request done");
        
        System.out.println("output.getFlightsList() size: " + output.getFlightsList().size());
        System.out.println("output.getHotelsList() size: " + output.getHotelsList().size());
        
        System.out.println("Checking Flights");
        String expectedFlight1 = "Copenhagen";
        String expectedFlight2 = "London"; 
        String expectedFlight3 = "Copenhagen";
        String resultFlight1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        System.out.println("1st flight start: " + resultFlight1);
        String resultFlight2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();
        String resultFlight3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart(); 
        assertEquals(expectedFlight1, resultFlight1); 
        assertEquals(expectedFlight2, resultFlight2);
        assertEquals(expectedFlight3, resultFlight3);
        
        System.out.println("Checking Hotels");
        String expected1 = "Hotel Madrid"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.getHotelsList().get(0).getHotelInformations() size: " + output.getHotelsList().get(0).getHotelInformations().size());
        System.out.println("2nd city 1st Hotel name: " + output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());
        System.out.println("2nd city 2nd Hotel name: " + output.getHotelsList().get(1).getHotelInformations().get(1).getHotel().getName());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println("1st Hotel name: " + result1);
        assertEquals(expected1, result1); 
        int result2 = output.getHotelsList().get(1).getHotelInformations().size(); 
        System.out.println("2nd city hotel amount: " + result2);
        assertEquals(2, result2);
        
    }
    
    private GetHotelInputType CreateGetHotelInputType(String city) throws DatatypeConfigurationException{
        GetHotelInputType input = new GetHotelInputType();
        input.setCity(city);
        
        GregorianCalendar _arrivalDate = new GregorianCalendar(2016, 11, 04);
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar arrivalDate = df.newXMLGregorianCalendar(_arrivalDate);
        input.setArrivalDate(arrivalDate);
        
        GregorianCalendar _departureDate = new GregorianCalendar(2016, 11, 14);
        XMLGregorianCalendar departureDate = df.newXMLGregorianCalendar(_departureDate);      
        input.setDepartureDate(departureDate);
        
        return input;
    }
    
    private GetFlightsInputType CreateGetFlightsInputType(String start, String destination, Integer day, Integer month, Integer year) throws DatatypeConfigurationException{
        GetFlightsInputType input = new GetFlightsInputType();
        input.setDestination(destination);
        input.setStart(start);
        
        DatatypeFactory df = DatatypeFactory.newInstance();
        GregorianCalendar _departureDate = new GregorianCalendar(2016, 11, 14);
        XMLGregorianCalendar departureDate = df.newXMLGregorianCalendar(_departureDate);
        departureDate.setDay(day);
        departureDate.setMonth(month);
        departureDate.setYear(year);
        input.setDate(departureDate);
        
        return input;
    }
    
    private static GetOutputType getFlightsAndHotels(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.GetInputType part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.getFlightsAndHotels(part1);
    }
}
