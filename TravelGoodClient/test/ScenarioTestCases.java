/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import static ConstructorPackage.Constructors.*;
import static ConstructorPackage.TravelGoodOperations.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.assertEquals;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;

/**
 *
 * @author Quentin
 */
public class ScenarioTestCases {
    
    public ScenarioTestCases() {
        
    }
    
    @Test
    public void ScenarioTest_P1() {
        
    
    }
    
    @Test
    public void ScenarioTest_P2() {
    
    }
    
        /*
     * Scenario B:
     * Planing an itinerary with many three booking, getting the their status as unconfirmed
     * booking them. The second one should fail and then we have to check the status.
     */
    
    @Test 
    public void ScenarioTest_B() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyFlights");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        //Creating a get request with many flights and one hotel
        GetInputType input = new GetInputType();
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight1 = CreateGetFlightsInputType(date, "Copenhagen", "London");
        GetFlightsInputType flight2 = CreateGetFlightsInputType(date, "London", "New York");
        GetFlightsInputType flight3 = CreateGetFlightsInputType(date, "London", "New York");
        AddFlight(input, flight1);
        AddFlight(input, flight2);
        AddFlight(input, flight3);
        
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the get request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        System.out.println(resultStart);
        String resultStart1 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("London", resultStart1);
        System.out.println(resultStart1);      
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println(resultCity);
        
        String flightBookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String flightBookingNumber1 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        String flightBookingNumber2 = output.getFlightsList().get(2).getFlightInformations().get(0).getBookingNumber();
        String hotelBookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
               
        // Plan itinerary using the ID number with a wrong booking number for the second flight
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(flightBookingNumber);
        plan.getFlightsBookingNumber().add("hello");
        plan.getFlightsBookingNumber().add(flightBookingNumber2);
        plan.getHotelsBookingNumber().add(hotelBookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        System.out.println("Planning: ");
        
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Flight status 2: " + status1);
        assertEquals("unconfirmed", status1);
                
        String status2 = planOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Flight Status 3: " + status2);
        assertEquals("unconfirmed", status2); 
        
        // Booking the flight but the second one should fail
        System.out.println("Booking: ");
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        
        hotelStatus = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        // assertEquals("cancelled", hotelStatus);
        
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + status);
        assertEquals("cancelled", status);
        
        status1 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Flight Status 2: " + status1);
        assertEquals("unconfirmed", status1); 
        
        status2 = bookOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Flight Status 3: " + status2);
        assertEquals("unconfirmed", status2); 
    }
    
    
    @Test
    public void ScenarioTest_C1() {
    
    }
    
    /**
     * @author Daniel Brand
     * @throws DatatypeConfigurationException 
     */
    @Test
    public void ScenarioTest_C2() throws DatatypeConfigurationException {
        System.out.println("Test starts - ScenarioTest_C2");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create two flight request
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        XMLGregorianCalendar date1 = CreateDate(30, 10, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "London", "New York");
        GetFlightsInputType flight2 = CreateGetFlightsInputType(date1, "New York", "London");
        AddFlight(input, flight);
        AddFlight(input, flight2);
        
        // Create one hotel request
        String city = "Error City";
        XMLGregorianCalendar date2 = CreateDate(26, 10, 2015);
        XMLGregorianCalendar date3 = CreateDate(30, 10, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date2, date3);
        AddHotel(input, hotel);
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber3 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        plan.getHotelsBookingNumber().add(bookingNumber2);
        plan.getHotelsBookingNumber().add(bookingNumber3);

        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Booking
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); 
        
        String status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        String status2 = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status2);
        
        String status3 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " +status3);
        assertEquals("confirmed", status3);
        
        // Cancel
    }
}
