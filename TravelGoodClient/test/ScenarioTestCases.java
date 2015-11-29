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
    
    @Test
    public void ScenarioTest_B() {
    
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
