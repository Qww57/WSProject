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
 * @author Quentin, Daniel Sanz
 */
public class ScenarioTestCases {
    
    public ScenarioTestCases() {
        
    }
    
    @Test
    public void testP1() throws DatatypeConfigurationException {
        System.out.println("Test P1 Starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        //Creating a get request with one flight
        GetInputType inputFlightOne = new GetInputType();
        GetFlightsInputType flight1 = CreateGetFlightsInputType(CreateDate(26, 10, 2015), "Copenhagen", "London");
        AddFlight(inputFlightOne, flight1);
        //search operation
        GetOutputType outputFlightOne = getFlightsAndHotels(inputFlightOne, receivedItinID);
        assertEquals(1, outputFlightOne.getFlightsList().size());
        assertEquals("Copenhagen", outputFlightOne.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart());
        String flightOneBookingNumber = outputFlightOne.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        System.out.println(flightOneBookingNumber);
        //Add flight one to itinerary
        PlanInputType planFlightInputOne = new PlanInputType();
        planFlightInputOne.getFlightsBookingNumber().add(flightOneBookingNumber);
        ItineraryListType planFlightOutputOne = planFlightsAndHotels(planFlightInputOne, receivedItinID);
        System.out.println("flights list size: " + planFlightOutputOne.getFlightsItineraryInformation().size());
        System.out.println("hotels list size: " + planFlightOutputOne.getHotelsItineraryInformation().size());
        System.out.println("hotel content" + planFlightOutputOne.getHotelsItineraryInformation().get(0).getBookingNumber());
        assertEquals(1, planFlightOutputOne.getFlightsItineraryInformation().size());
        
        //Creating a get request with one hotel
        GetInputType inputHotelOne = new GetInputType();
        GetHotelInputType hotel1 = CreateGetHotelInputType("Milan", CreateDate(26, 11, 2015), CreateDate(29, 11, 2015));
        AddHotel(inputHotelOne, hotel1);
        //search operation
        GetOutputType outputHotelOne = getFlightsAndHotels(inputHotelOne, receivedItinID);
        assertEquals(1, outputHotelOne.getFlightsList().size());
        assertEquals("Milan Hotel", outputHotelOne.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName());
        String hotelOneBookingNumber = outputHotelOne.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        //Add flight one to itinerary
        PlanInputType planHotelInputOne = new PlanInputType();
        planHotelInputOne.getHotelsBookingNumber().add(hotelOneBookingNumber);
        ItineraryListType planHotelOutputOne = planFlightsAndHotels(planHotelInputOne, receivedItinID);
        System.out.println("flights list size: " + planHotelOutputOne.getFlightsItineraryInformation().size());
        System.out.println("hotels list size: " + planHotelOutputOne.getHotelsItineraryInformation().size());
        assertEquals(1, planHotelOutputOne.getHotelsItineraryInformation().size());
        
        //Creating a get request with second flight
        GetInputType inputFlightTwo = new GetInputType();
        GetFlightsInputType flight2 = CreateGetFlightsInputType(CreateDate(26, 10, 2015), "London", "New York");
        AddFlight(inputFlightTwo, flight2);
        //search operation
        GetOutputType outputFlightTwo = getFlightsAndHotels(inputFlightTwo, receivedItinID);
        assertEquals(1, outputFlightTwo.getFlightsList().size());
        assertEquals("London", outputFlightTwo.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart());
        String flightTwoBookingNumber = outputFlightTwo.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        System.out.println(flightTwoBookingNumber);
        //Add flight two to itinerary
        PlanInputType planFlightInputTwo = new PlanInputType();
        planFlightInputTwo.getFlightsBookingNumber().add(flightTwoBookingNumber);
        ItineraryListType planFlightOutputTwo = planFlightsAndHotels(planFlightInputTwo, receivedItinID);
        //temp
        System.out.println("flights list size: " + planFlightOutputTwo.getFlightsItineraryInformation().size());
        System.out.println("hotels list size: " + planFlightOutputTwo.getHotelsItineraryInformation().size());
        System.out.println("flight booking number on itinerary: " + planFlightOutputTwo.getFlightsItineraryInformation().get(0).getBookingNumber());
        assertEquals(2, planFlightOutputTwo.getFlightsItineraryInformation().size());
        
        //Creating a get request with third flight
        GetInputType inputFlightThree = new GetInputType();
        GetFlightsInputType flight3 = CreateGetFlightsInputType(CreateDate(30, 10, 2015), "New York", "London");
        AddFlight(inputFlightThree, flight3);
        //search operation
        GetOutputType outputFlightThree = getFlightsAndHotels(inputFlightThree, receivedItinID);
        assertEquals(1, outputFlightThree.getFlightsList().size());
        assertEquals("New York", outputFlightThree.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart());
        String flightThreeBookingNumber = outputFlightThree.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        //Add flight three to itinerary
        PlanInputType planFlightInputThree = new PlanInputType();
        planFlightInputThree.getFlightsBookingNumber().add(flightThreeBookingNumber);
        ItineraryListType planFlightOutputThree = planFlightsAndHotels(planFlightInputThree, receivedItinID);
        //temp
        System.out.println("size flight 3: " + planFlightOutputThree.getFlightsItineraryInformation().size());
        assertEquals(3, planFlightOutputThree.getFlightsItineraryInformation().size());
        
        //Creating a get request with second hotel
        GetInputType inputHotelTwo = new GetInputType();
        GetHotelInputType hotel2 = CreateGetHotelInputType("Copenhagen", CreateDate(4, 5, 2016), CreateDate(9, 5, 2016));
        AddHotel(inputHotelTwo, hotel2);
        //search operation
        GetOutputType outputHotelTwo = getFlightsAndHotels(inputHotelTwo, receivedItinID);
        assertEquals(1, outputHotelTwo.getFlightsList().size());
        assertEquals("Hilton Hotel", outputHotelTwo.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName());
        String hotelTwoBookingNumber = outputHotelTwo.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        //Add hotel two to itinerary
        PlanInputType planHotelInputTwo = new PlanInputType();
        planHotelInputTwo.getHotelsBookingNumber().add(hotelTwoBookingNumber);
        ItineraryListType planHotelOutputTwo = planFlightsAndHotels(planHotelInputTwo, receivedItinID);
        assertEquals(2, planHotelOutputTwo.getHotelsItineraryInformation().size());
        
        
        
        

    
    }
    
    @Test
    public void testP2() {
    
    }
    
     /*
     * Scenario B:
     * Planing an itinerary with many three booking, getting the their status as unconfirmed
     * booking them. The second one should fail and then we have to check the status.
     */
    
    @Test 
    public void testB() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
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
        assertEquals("cancelled", hotelStatus);
        
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
    public void testC1() throws DatatypeConfigurationException {
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        GetFlightsInputType flight2 = CreateGetFlightsInputType("London", "New York", 26,10,2015);
        GetFlightsInputType flight3 = CreateGetFlightsInputType("Copenhagen", "Kuala Lumpur", 26,2,2016);
        
        flightRequest.getFlightsList().add(flight);
        flightRequest.getFlightsList().add(flight2);
        flightRequest.getFlightsList().add(flight3);
        input.getFlightRequests().add(flightRequest); 
        
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);       

        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        System.out.println("Request done");
          
        String expected1 = "Copenhagen";
        String expected2 = "London"; 
        String expected3 = "Copenhagen"; 
        String expected4 = "Milan";   
        
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);              
        String result2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected2, result2); 
        String result3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected3, result3);
        String result4 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();     
        assertEquals(expected4, result4);
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber1 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getFlightsList().get(2).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber3 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        plan.getFlightsBookingNumber().add(bookingNumber);
        plan.getFlightsBookingNumber().add(bookingNumber1);
        plan.getFlightsBookingNumber().add(bookingNumber2);
        plan.getHotelsBookingNumber().add(bookingNumber3);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Planning");
        
        System.out.println("Get status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("unconfirmed", status1);
        
        String status2 = planOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("unconfirmed", status2);
        
        String status3 = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("unconfirmed", status3);
        
        
        // Booking the flight
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        System.out.println("Booking");
        
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("confirmed", status);
        
        status1 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("confirmed", status1);
        
        status2 = bookOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("confirmed", status2);
        
        status3 = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("confirmed", status3);
        
        // Cancelling the itinerary
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
        System.out.println("Canceling");
        
        status = cancelOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
        
        status1 = cancelOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status1);
        
        status2 = cancelOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status2);
        
        status3 = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("cancelled", status3);
    }
    
    /**
     * @author Daniel Brand
     * @throws DatatypeConfigurationException 
     */
    @Test
    public void testC2() throws DatatypeConfigurationException {
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
        String city = "Error city";
        XMLGregorianCalendar date2 = CreateDate(26, 10, 2015);
        XMLGregorianCalendar date3 = CreateDate(30, 10, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date2, date3);
        AddHotel(input, hotel);
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        
        String bookingNumber1 = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber3 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(bookingNumber1);
        plan.getHotelsBookingNumber().add(bookingNumber2);
        plan.getFlightsBookingNumber().add(bookingNumber3);

        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Booking
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        
        String status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("confirmed", status);
        
        String status2 = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("confirmed", status2);
        
        String status3 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("confirmed", status3);
        
        // Cancelling
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
            
        status = cancelOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("cancelled", status);

        status2 = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " +status2);
        assertEquals("confirmed", status2);
            
        status3 = cancelOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("cancelled", status3);
    }
}
